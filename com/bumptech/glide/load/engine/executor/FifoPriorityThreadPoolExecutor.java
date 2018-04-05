package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FifoPriorityThreadPoolExecutor extends ThreadPoolExecutor {
    private static final String TAG = "PriorityExecutor";
    private final AtomicInteger ordering;
    private final UncaughtThrowableStrategy uncaughtThrowableStrategy;

    public static class DefaultThreadFactory implements ThreadFactory {
        int threadNum = 0;

        public Thread newThread(Runnable runnable) {
            Thread result = new Thread(runnable, "fifo-pool-thread-" + this.threadNum) {
                public void run() {
                    Process.setThreadPriority(10);
                    super.run();
                }
            };
            this.threadNum++;
            return result;
        }
    }

    static class LoadTask<T> extends FutureTask<T> implements Comparable<LoadTask<?>> {
        private final int order;
        private final int priority;

        public LoadTask(Runnable runnable, T result, int order) {
            super(runnable, result);
            if (runnable instanceof Prioritized) {
                this.priority = ((Prioritized) runnable).getPriority();
                this.order = order;
                return;
            }
            throw new IllegalArgumentException("FifoPriorityThreadPoolExecutor must be given Runnables that implement Prioritized");
        }

        public boolean equals(Object o) {
            if (!(o instanceof LoadTask)) {
                return false;
            }
            LoadTask<Object> other = (LoadTask) o;
            if (this.order == other.order && this.priority == other.priority) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.priority * 31) + this.order;
        }

        public int compareTo(LoadTask<?> loadTask) {
            int result = this.priority - loadTask.priority;
            if (result == 0) {
                return this.order - loadTask.order;
            }
            return result;
        }
    }

    public enum UncaughtThrowableStrategy {
        IGNORE,
        LOG {
            protected void handle(Throwable t) {
                if (Log.isLoggable(FifoPriorityThreadPoolExecutor.TAG, 6)) {
                    Log.e(FifoPriorityThreadPoolExecutor.TAG, "Request threw uncaught throwable", t);
                }
            }
        },
        THROW {
            protected void handle(Throwable t) {
                super.handle(t);
                throw new RuntimeException(t);
            }
        };

        protected void handle(Throwable t) {
        }
    }

    public FifoPriorityThreadPoolExecutor(int poolSize) {
        this(poolSize, UncaughtThrowableStrategy.LOG);
    }

    public FifoPriorityThreadPoolExecutor(int poolSize, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        this(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new DefaultThreadFactory(), uncaughtThrowableStrategy);
    }

    public FifoPriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAlive, TimeUnit timeUnit, ThreadFactory threadFactory, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        super(corePoolSize, maximumPoolSize, keepAlive, timeUnit, new PriorityBlockingQueue(), threadFactory);
        this.ordering = new AtomicInteger();
        this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
    }

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new LoadTask(runnable, value, this.ordering.getAndIncrement());
    }

    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && (r instanceof Future)) {
            Future<?> future = (Future) r;
            if (future.isDone() && !future.isCancelled()) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    this.uncaughtThrowableStrategy.handle(e);
                } catch (ExecutionException e2) {
                    this.uncaughtThrowableStrategy.handle(e2);
                }
            }
        }
    }
}
