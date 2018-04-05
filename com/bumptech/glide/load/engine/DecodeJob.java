package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCache.Writer;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.util.LogTime;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class DecodeJob<A, T, Z> {
    private static final FileOpener DEFAULT_FILE_OPENER = new FileOpener();
    private static final String TAG = "DecodeJob";
    private final DiskCacheProvider diskCacheProvider;
    private final DiskCacheStrategy diskCacheStrategy;
    private final DataFetcher<A> fetcher;
    private final FileOpener fileOpener;
    private final int height;
    private volatile boolean isCancelled;
    private final DataLoadProvider<A, T> loadProvider;
    private final Priority priority;
    private final EngineKey resultKey;
    private final ResourceTranscoder<T, Z> transcoder;
    private final Transformation<T> transformation;
    private final int width;

    interface DiskCacheProvider {
        DiskCache getDiskCache();
    }

    static class FileOpener {
        FileOpener() {
        }

        public OutputStream open(File file) throws FileNotFoundException {
            return new BufferedOutputStream(new FileOutputStream(file));
        }
    }

    class SourceWriter<DataType> implements Writer {
        private final DataType data;
        private final Encoder<DataType> encoder;

        public SourceWriter(Encoder<DataType> encoder, DataType data) {
            this.encoder = encoder;
            this.data = data;
        }

        public boolean write(File file) {
            boolean success = false;
            OutputStream os = null;
            try {
                os = DecodeJob.this.fileOpener.open(file);
                success = this.encoder.encode(this.data, os);
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e2) {
                if (Log.isLoggable(DecodeJob.TAG, 3)) {
                    Log.d(DecodeJob.TAG, "Failed to find file to write to disk cache", e2);
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e4) {
                    }
                }
            }
            return success;
        }
    }

    private com.bumptech.glide.load.engine.Resource<T> loadFromCache(com.bumptech.glide.load.Key r6) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r5 = this;
        r2 = r5.diskCacheProvider;
        r2 = r2.getDiskCache();
        r0 = r2.get(r6);
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        r1 = 0;
    L_0x000d:
        return r1;
    L_0x000e:
        r1 = 0;
        r2 = r5.loadProvider;	 Catch:{ all -> 0x0029 }
        r2 = r2.getCacheDecoder();	 Catch:{ all -> 0x0029 }
        r3 = r5.width;	 Catch:{ all -> 0x0029 }
        r4 = r5.height;	 Catch:{ all -> 0x0029 }
        r1 = r2.decode(r0, r3, r4);	 Catch:{ all -> 0x0029 }
        if (r1 != 0) goto L_0x000d;
    L_0x001f:
        r2 = r5.diskCacheProvider;
        r2 = r2.getDiskCache();
        r2.delete(r6);
        goto L_0x000d;
    L_0x0029:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0035;
    L_0x002c:
        r3 = r5.diskCacheProvider;
        r3 = r3.getDiskCache();
        r3.delete(r6);
    L_0x0035:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.DecodeJob.loadFromCache(com.bumptech.glide.load.Key):com.bumptech.glide.load.engine.Resource<T>");
    }

    public DecodeJob(EngineKey resultKey, int width, int height, DataFetcher<A> fetcher, DataLoadProvider<A, T> loadProvider, Transformation<T> transformation, ResourceTranscoder<T, Z> transcoder, DiskCacheProvider diskCacheProvider, DiskCacheStrategy diskCacheStrategy, Priority priority) {
        this(resultKey, width, height, fetcher, loadProvider, transformation, transcoder, diskCacheProvider, diskCacheStrategy, priority, DEFAULT_FILE_OPENER);
    }

    DecodeJob(EngineKey resultKey, int width, int height, DataFetcher<A> fetcher, DataLoadProvider<A, T> loadProvider, Transformation<T> transformation, ResourceTranscoder<T, Z> transcoder, DiskCacheProvider diskCacheProvider, DiskCacheStrategy diskCacheStrategy, Priority priority, FileOpener fileOpener) {
        this.resultKey = resultKey;
        this.width = width;
        this.height = height;
        this.fetcher = fetcher;
        this.loadProvider = loadProvider;
        this.transformation = transformation;
        this.transcoder = transcoder;
        this.diskCacheProvider = diskCacheProvider;
        this.diskCacheStrategy = diskCacheStrategy;
        this.priority = priority;
        this.fileOpener = fileOpener;
    }

    public Resource<Z> decodeResultFromCache() throws Exception {
        if (!this.diskCacheStrategy.cacheResult()) {
            return null;
        }
        long startTime = LogTime.getLogTime();
        Resource<T> transformed = loadFromCache(this.resultKey);
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Decoded transformed from cache", startTime);
        }
        startTime = LogTime.getLogTime();
        Resource<Z> result = transcode(transformed);
        if (!Log.isLoggable(TAG, 2)) {
            return result;
        }
        logWithTimeAndKey("Transcoded transformed from cache", startTime);
        return result;
    }

    public Resource<Z> decodeSourceFromCache() throws Exception {
        if (!this.diskCacheStrategy.cacheSource()) {
            return null;
        }
        long startTime = LogTime.getLogTime();
        Resource<T> decoded = loadFromCache(this.resultKey.getOriginalKey());
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Decoded source from cache", startTime);
        }
        return transformEncodeAndTranscode(decoded);
    }

    public Resource<Z> decodeFromSource() throws Exception {
        return transformEncodeAndTranscode(decodeSource());
    }

    public void cancel() {
        this.isCancelled = true;
        this.fetcher.cancel();
    }

    private Resource<Z> transformEncodeAndTranscode(Resource<T> decoded) {
        long startTime = LogTime.getLogTime();
        Resource<T> transformed = transform(decoded);
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Transformed resource from source", startTime);
        }
        writeTransformedToCache(transformed);
        startTime = LogTime.getLogTime();
        Resource<Z> result = transcode(transformed);
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Transcoded transformed from source", startTime);
        }
        return result;
    }

    private void writeTransformedToCache(Resource<T> transformed) {
        if (transformed != null && this.diskCacheStrategy.cacheResult()) {
            long startTime = LogTime.getLogTime();
            this.diskCacheProvider.getDiskCache().put(this.resultKey, new SourceWriter(this.loadProvider.getEncoder(), transformed));
            if (Log.isLoggable(TAG, 2)) {
                logWithTimeAndKey("Wrote transformed from source to cache", startTime);
            }
        }
    }

    private Resource<T> decodeSource() throws Exception {
        try {
            long startTime = LogTime.getLogTime();
            A data = this.fetcher.loadData(this.priority);
            if (Log.isLoggable(TAG, 2)) {
                logWithTimeAndKey("Fetched data", startTime);
            }
            if (this.isCancelled) {
                return null;
            }
            Resource<T> decoded = decodeFromSourceData(data);
            this.fetcher.cleanup();
            return decoded;
        } finally {
            this.fetcher.cleanup();
        }
    }

    private Resource<T> decodeFromSourceData(A data) throws IOException {
        if (this.diskCacheStrategy.cacheSource()) {
            return cacheAndDecodeSourceData(data);
        }
        long startTime = LogTime.getLogTime();
        Resource<T> decoded = this.loadProvider.getSourceDecoder().decode(data, this.width, this.height);
        if (!Log.isLoggable(TAG, 2)) {
            return decoded;
        }
        logWithTimeAndKey("Decoded from source", startTime);
        return decoded;
    }

    private Resource<T> cacheAndDecodeSourceData(A data) throws IOException {
        long startTime = LogTime.getLogTime();
        this.diskCacheProvider.getDiskCache().put(this.resultKey.getOriginalKey(), new SourceWriter(this.loadProvider.getSourceEncoder(), data));
        if (Log.isLoggable(TAG, 2)) {
            logWithTimeAndKey("Wrote source to cache", startTime);
        }
        startTime = LogTime.getLogTime();
        Resource<T> result = loadFromCache(this.resultKey.getOriginalKey());
        if (Log.isLoggable(TAG, 2) && result != null) {
            logWithTimeAndKey("Decoded source from cache", startTime);
        }
        return result;
    }

    private Resource<T> transform(Resource<T> decoded) {
        if (decoded == null) {
            return null;
        }
        Resource<T> transformed = this.transformation.transform(decoded, this.width, this.height);
        if (decoded.equals(transformed)) {
            return transformed;
        }
        decoded.recycle();
        return transformed;
    }

    private Resource<Z> transcode(Resource<T> transformed) {
        if (transformed == null) {
            return null;
        }
        return this.transcoder.transcode(transformed);
    }

    private void logWithTimeAndKey(String message, long startTime) {
        Log.v(TAG, message + " in " + LogTime.getElapsedMillis(startTime) + ", key: " + this.resultKey);
    }
}
