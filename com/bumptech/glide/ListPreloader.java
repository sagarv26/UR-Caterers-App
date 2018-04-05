package com.bumptech.glide;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Util;
import java.util.List;
import java.util.Queue;

public class ListPreloader<T> implements OnScrollListener {
    private boolean isIncreasing;
    private int lastEnd;
    private int lastFirstVisible;
    private int lastStart;
    private final int maxPreload;
    private final PreloadSizeProvider<T> preloadDimensionProvider;
    private final PreloadModelProvider<T> preloadModelProvider;
    private final PreloadTargetQueue preloadTargetQueue;
    private int totalItemCount;

    public interface PreloadModelProvider<U> {
        List<U> getPreloadItems(int i);

        GenericRequestBuilder getPreloadRequestBuilder(U u);
    }

    public interface PreloadSizeProvider<T> {
        int[] getPreloadSize(T t, int i, int i2);
    }

    private static final class PreloadTargetQueue {
        private final Queue<PreloadTarget> queue;

        public PreloadTargetQueue(int size) {
            this.queue = Util.createQueue(size);
            for (int i = 0; i < size; i++) {
                this.queue.offer(new PreloadTarget());
            }
        }

        public PreloadTarget next(int width, int height) {
            PreloadTarget result = (PreloadTarget) this.queue.poll();
            this.queue.offer(result);
            result.photoWidth = width;
            result.photoHeight = height;
            return result;
        }
    }

    class C04251 implements PreloadModelProvider<T> {
        C04251() {
        }

        public List<T> getPreloadItems(int position) {
            return ListPreloader.this.getItems(position, position + 1);
        }

        public GenericRequestBuilder getPreloadRequestBuilder(T item) {
            return ListPreloader.this.getRequestBuilder(item);
        }
    }

    class C04262 implements PreloadSizeProvider<T> {
        C04262() {
        }

        public int[] getPreloadSize(T item, int adapterPosition, int perItemPosition) {
            return ListPreloader.this.getDimensions(item);
        }
    }

    private static class PreloadTarget extends BaseTarget<Object> {
        private int photoHeight;
        private int photoWidth;

        private PreloadTarget() {
        }

        public void onResourceReady(Object resource, GlideAnimation<? super Object> glideAnimation) {
        }

        public void getSize(SizeReadyCallback cb) {
            cb.onSizeReady(this.photoWidth, this.photoHeight);
        }
    }

    @Deprecated
    public ListPreloader(int maxPreload) {
        this.isIncreasing = true;
        this.preloadModelProvider = new C04251();
        this.preloadDimensionProvider = new C04262();
        this.maxPreload = maxPreload;
        this.preloadTargetQueue = new PreloadTargetQueue(maxPreload + 1);
    }

    public ListPreloader(PreloadModelProvider<T> preloadModelProvider, PreloadSizeProvider<T> preloadDimensionProvider, int maxPreload) {
        this.isIncreasing = true;
        this.preloadModelProvider = preloadModelProvider;
        this.preloadDimensionProvider = preloadDimensionProvider;
        this.maxPreload = maxPreload;
        this.preloadTargetQueue = new PreloadTargetQueue(maxPreload + 1);
    }

    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
        this.totalItemCount = totalCount;
        if (firstVisible > this.lastFirstVisible) {
            preload(firstVisible + visibleCount, true);
        } else if (firstVisible < this.lastFirstVisible) {
            preload(firstVisible, false);
        }
        this.lastFirstVisible = firstVisible;
    }

    @Deprecated
    protected int[] getDimensions(T t) {
        throw new IllegalStateException("You must either provide a PreloadDimensionProvider or override getDimensions()");
    }

    @Deprecated
    protected List<T> getItems(int start, int end) {
        throw new IllegalStateException("You must either provide a PreloadModelProvider or override getItems()");
    }

    @Deprecated
    protected GenericRequestBuilder getRequestBuilder(T t) {
        throw new IllegalStateException("You must either provide a PreloadModelProvider, or override getRequestBuilder()");
    }

    private void preload(int start, boolean increasing) {
        if (this.isIncreasing != increasing) {
            this.isIncreasing = increasing;
            cancelAll();
        }
        preload(start, (increasing ? this.maxPreload : -this.maxPreload) + start);
    }

    private void preload(int from, int to) {
        int start;
        int end;
        if (from < to) {
            start = Math.max(this.lastEnd, from);
            end = to;
        } else {
            start = to;
            end = Math.min(this.lastStart, from);
        }
        end = Math.min(this.totalItemCount, end);
        start = Math.min(this.totalItemCount, Math.max(0, start));
        int i;
        if (from < to) {
            for (i = start; i < end; i++) {
                preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(i), i, true);
            }
        } else {
            for (i = end - 1; i >= start; i--) {
                preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(i), i, false);
            }
        }
        this.lastStart = start;
        this.lastEnd = end;
    }

    private void preloadAdapterPosition(List<T> items, int position, boolean isIncreasing) {
        int numItems = items.size();
        int i;
        if (isIncreasing) {
            for (i = 0; i < numItems; i++) {
                preloadItem(items.get(i), position, i);
            }
            return;
        }
        for (i = numItems - 1; i >= 0; i--) {
            preloadItem(items.get(i), position, i);
        }
    }

    private void preloadItem(T item, int position, int i) {
        int[] dimensions = this.preloadDimensionProvider.getPreloadSize(item, position, i);
        if (dimensions != null) {
            this.preloadModelProvider.getPreloadRequestBuilder(item).into(this.preloadTargetQueue.next(dimensions[0], dimensions[1]));
        }
    }

    private void cancelAll() {
        for (int i = 0; i < this.maxPreload; i++) {
            Glide.clear(this.preloadTargetQueue.next(0, 0));
        }
    }
}
