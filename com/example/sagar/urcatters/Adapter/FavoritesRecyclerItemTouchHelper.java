package com.example.sagar.urcatters.Adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import com.example.sagar.urcatters.Adapter.FavListAdapter.MyViewHolder;

public class FavoritesRecyclerItemTouchHelper extends SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(ViewHolder viewHolder, int i, int i2);
    }

    public FavoritesRecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        return true;
    }

    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            Callback.getDefaultUIUtil().onSelected(((MyViewHolder) viewHolder).viewForeground);
        }
    }

    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, ((MyViewHolder) viewHolder).viewForeground, dX, dY, actionState, isCurrentlyActive);
    }

    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
        Callback.getDefaultUIUtil().clearView(((MyViewHolder) viewHolder).viewForeground);
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Callback.getDefaultUIUtil().onDraw(c, recyclerView, ((MyViewHolder) viewHolder).viewForeground, dX, dY, actionState, isCurrentlyActive);
    }

    public void onSwiped(ViewHolder viewHolder, int direction) {
        this.listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
