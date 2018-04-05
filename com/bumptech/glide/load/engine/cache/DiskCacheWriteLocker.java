package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
    private final Map<Key, WriteLock> locks = new HashMap();
    private final WriteLockPool writeLockPool = new WriteLockPool();

    private static class WriteLock {
        int interestedThreads;
        final Lock lock;

        private WriteLock() {
            this.lock = new ReentrantLock();
        }
    }

    private static class WriteLockPool {
        private static final int MAX_POOL_SIZE = 10;
        private final Queue<WriteLock> pool;

        private WriteLockPool() {
            this.pool = new ArrayDeque();
        }

        WriteLock obtain() {
            synchronized (this.pool) {
                WriteLock result = (WriteLock) this.pool.poll();
            }
            if (result == null) {
                return new WriteLock();
            }
            return result;
        }

        void offer(WriteLock writeLock) {
            synchronized (this.pool) {
                if (this.pool.size() < 10) {
                    this.pool.offer(writeLock);
                }
            }
        }
    }

    DiskCacheWriteLocker() {
    }

    void acquire(Key key) {
        WriteLock writeLock;
        synchronized (this) {
            writeLock = (WriteLock) this.locks.get(key);
            if (writeLock == null) {
                writeLock = this.writeLockPool.obtain();
                this.locks.put(key, writeLock);
            }
            writeLock.interestedThreads++;
        }
        writeLock.lock.lock();
    }

    void release(Key key) {
        WriteLock writeLock;
        synchronized (this) {
            writeLock = (WriteLock) this.locks.get(key);
            int i;
            if (writeLock == null || writeLock.interestedThreads <= 0) {
                StringBuilder append = new StringBuilder().append("Cannot release a lock that is not held, key: ").append(key).append(", interestedThreads: ");
                if (writeLock == null) {
                    i = 0;
                } else {
                    i = writeLock.interestedThreads;
                }
                throw new IllegalArgumentException(append.append(i).toString());
            }
            i = writeLock.interestedThreads - 1;
            writeLock.interestedThreads = i;
            if (i == 0) {
                WriteLock removed = (WriteLock) this.locks.remove(key);
                if (removed.equals(writeLock)) {
                    this.writeLockPool.offer(removed);
                } else {
                    throw new IllegalStateException("Removed the wrong lock, expected to remove: " + writeLock + ", but actually removed: " + removed + ", key: " + key);
                }
            }
        }
        writeLock.lock.unlock();
    }
}
