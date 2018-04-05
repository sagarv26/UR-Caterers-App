package com.bumptech.glide.disklrucache;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable = new C02391();
    private final File directory;
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    private int redundantOpCount;
    private long size = 0;
    private final int valueCount;

    class C02391 implements Callable<Void> {
        C02391() {
        }

        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.journalWriter == null) {
                } else {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                }
            }
            return null;
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private final boolean[] written;

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        private InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        InputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(index));
                        return fileInputStream;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        public String getString(int index) throws IOException {
            InputStream in = newInputStream(index);
            return in != null ? DiskLruCache.inputStreamToString(in) : null;
        }

        public File getFile(int index) throws IOException {
            File dirtyFile;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                dirtyFile = this.entry.getDirtyFile(index);
                if (!DiskLruCache.this.directory.exists()) {
                    DiskLruCache.this.directory.mkdirs();
                }
            }
            return dirtyFile;
        }

        public void set(int index, String value) throws IOException {
            Throwable th;
            Writer writer = null;
            try {
                Writer writer2 = new OutputStreamWriter(new FileOutputStream(getFile(index)), Util.UTF_8);
                try {
                    writer2.write(value);
                    Util.closeQuietly(writer2);
                } catch (Throwable th2) {
                    th = th2;
                    writer = writer2;
                    Util.closeQuietly(writer);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                Util.closeQuietly(writer);
                throw th;
            }
        }

        public void commit() throws IOException {
            DiskLruCache.this.completeEdit(this, true);
            this.committed = true;
        }

        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    abort();
                } catch (IOException e) {
                }
            }
        }
    }

    private final class Entry {
        File[] cleanFiles;
        private Editor currentEditor;
        File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder fileBuilder = new StringBuilder(key).append('.');
            int truncateTo = fileBuilder.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                fileBuilder.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.setLength(truncateTo);
            }
        }

        public String getLengths() throws IOException {
            StringBuilder result = new StringBuilder();
            for (long size : this.lengths) {
                result.append(' ').append(size);
            }
            return result.toString();
        }

        private void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < strings.length) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        public File getCleanFile(int i) {
            return this.cleanFiles[i];
        }

        public File getDirtyFile(int i) {
            return this.dirtyFiles[i];
        }
    }

    public final class Value {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;

        private Value(String key, long sequenceNumber, File[] files, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.files = files;
            this.lengths = lengths;
        }

        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public File getFile(int index) {
            return this.files[index];
        }

        public String getString(int index) throws IOException {
            return DiskLruCache.inputStreamToString(new FileInputStream(this.files[index]));
        }

        public long getLength(int index) {
            return this.lengths[index];
        }
    }

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize) throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            File backupFile = new File(directory, JOURNAL_FILE_BACKUP);
            if (backupFile.exists()) {
                File journalFile = new File(directory, JOURNAL_FILE);
                if (journalFile.exists()) {
                    backupFile.delete();
                } else {
                    renameTo(backupFile, journalFile, false);
                }
            }
            DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            if (cache.journalFile.exists()) {
                try {
                    cache.readJournal();
                    cache.processJournal();
                    return cache;
                } catch (IOException journalIsCorrupt) {
                    System.out.println("DiskLruCache " + directory + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing");
                    cache.delete();
                }
            }
            directory.mkdirs();
            cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            cache.rebuildJournal();
            return cache;
        }
    }

    private void readJournal() throws IOException {
        int lineCount;
        StrictLineReader reader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            String magic = reader.readLine();
            String version = reader.readLine();
            String appVersionString = reader.readLine();
            String valueCountString = reader.readLine();
            String blank = reader.readLine();
            if (MAGIC.equals(magic) && VERSION_1.equals(version) && Integer.toString(this.appVersion).equals(appVersionString) && Integer.toString(this.valueCount).equals(valueCountString) && "".equals(blank)) {
                lineCount = 0;
                while (true) {
                    readJournalLine(reader.readLine());
                    lineCount++;
                }
            } else {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
            }
        } catch (EOFException e) {
            this.redundantOpCount = lineCount - this.lruEntries.size();
            if (reader.hasUnterminatedLine()) {
                rebuildJournal();
            } else {
                this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            }
            Util.closeQuietly(reader);
        } catch (Throwable th) {
            Util.closeQuietly(reader);
        }
    }

    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(32);
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        String key;
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(32, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        }
        key = line.substring(keyBegin, secondSpace);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry);
        } else if (secondSpace != -1 || firstSpace != READ.length() || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            int t;
            if (entry.currentEditor == null) {
                for (t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (t = 0; t < this.valueCount; t++) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                }
                i.remove();
            }
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
        try {
            writer.write(MAGIC);
            writer.write("\n");
            writer.write(VERSION_1);
            writer.write("\n");
            writer.write(Integer.toString(this.appVersion));
            writer.write("\n");
            writer.write(Integer.toString(this.valueCount));
            writer.write("\n");
            writer.write("\n");
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer.write("DIRTY " + entry.key + '\n');
                } else {
                    writer.write("CLEAN " + entry.key + entry.getLengths() + '\n');
                }
            }
            writer.close();
            if (this.journalFile.exists()) {
                renameTo(this.journalFile, this.journalFileBackup, true);
            }
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
        } catch (Throwable th) {
            writer.close();
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    public synchronized Value get(String key) throws IOException {
        Value value = null;
        synchronized (this) {
            checkNotClosed();
            Entry entry = (Entry) this.lruEntries.get(key);
            if (entry != null) {
                if (entry.readable) {
                    for (File file : entry.cleanFiles) {
                        if (!file.exists()) {
                            break;
                        }
                    }
                    this.redundantOpCount++;
                    this.journalWriter.append(READ);
                    this.journalWriter.append(' ');
                    this.journalWriter.append(key);
                    this.journalWriter.append('\n');
                    if (journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    value = new Value(key, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
                }
            }
        }
        return value;
    }

    public Editor edit(String key) throws IOException {
        return edit(key, -1);
    }

    private synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        Editor editor = null;
        synchronized (this) {
            checkNotClosed();
            Entry entry = (Entry) this.lruEntries.get(key);
            if (expectedSequenceNumber == -1 || (entry != null && entry.sequenceNumber == expectedSequenceNumber)) {
                if (entry == null) {
                    entry = new Entry(key);
                    this.lruEntries.put(key, entry);
                } else if (entry.currentEditor != null) {
                }
                editor = new Editor(entry);
                entry.currentEditor = editor;
                this.journalWriter.append(DIRTY);
                this.journalWriter.append(' ');
                this.journalWriter.append(key);
                this.journalWriter.append('\n');
                this.journalWriter.flush();
            }
        }
        return editor;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        this.executorService.submit(this.cleanupCallable);
    }

    public synchronized long size() {
        return this.size;
    }

    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }
        int i;
        if (success) {
            if (!entry.readable) {
                i = 0;
                while (i < this.valueCount) {
                    if (!editor.written[i]) {
                        editor.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    } else if (!entry.getDirtyFile(i).exists()) {
                        editor.abort();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        for (i = 0; i < this.valueCount; i++) {
            File dirty = entry.getDirtyFile(i);
            if (!success) {
                deleteIfExists(dirty);
            } else if (dirty.exists()) {
                File clean = entry.getCleanFile(i);
                dirty.renameTo(clean);
                long oldLength = entry.lengths[i];
                long newLength = clean.length();
                entry.lengths[i] = newLength;
                this.size = (this.size - oldLength) + newLength;
            }
        }
        this.redundantOpCount++;
        entry.currentEditor = null;
        if ((entry.readable | success) != 0) {
            entry.readable = true;
            this.journalWriter.append(CLEAN);
            this.journalWriter.append(' ');
            this.journalWriter.append(entry.key);
            this.journalWriter.append(entry.getLengths());
            this.journalWriter.append('\n');
            if (success) {
                long j = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j;
                entry.sequenceNumber = j;
            }
        } else {
            this.lruEntries.remove(entry.key);
            this.journalWriter.append(REMOVE);
            this.journalWriter.append(' ');
            this.journalWriter.append(entry.key);
            this.journalWriter.append('\n');
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean z;
        checkNotClosed();
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            z = false;
        } else {
            int i = 0;
            while (i < this.valueCount) {
                File file = entry.getCleanFile(i);
                if (!file.exists() || file.delete()) {
                    this.size -= entry.lengths[i];
                    entry.lengths[i] = 0;
                    i++;
                } else {
                    throw new IOException("failed to delete " + file);
                }
            }
            this.redundantOpCount++;
            this.journalWriter.append(REMOVE);
            this.journalWriter.append(' ');
            this.journalWriter.append(key);
            this.journalWriter.append('\n');
            this.lruEntries.remove(key);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            z = true;
        }
        return z;
    }

    public synchronized boolean isClosed() {
        return this.journalWriter == null;
    }

    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator i$ = new ArrayList(this.lruEntries.values()).iterator();
            while (i$.hasNext()) {
                Entry entry = (Entry) i$.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            remove((String) ((java.util.Map.Entry) this.lruEntries.entrySet().iterator().next()).getKey());
        }
    }

    public void delete() throws IOException {
        close();
        Util.deleteContents(this.directory);
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        return Util.readFully(new InputStreamReader(in, Util.UTF_8));
    }
}
