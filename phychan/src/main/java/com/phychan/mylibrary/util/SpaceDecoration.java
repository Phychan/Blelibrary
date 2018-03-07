package com.phychan.mylibrary.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    /**
     * 只在前面加空格
     */
    public static final int TYPE_START = 1;
    /**
     * 只在后面加空格
     */
    public static final int TYPE_END = 2;
    /**
     * 前后都加空格，但两端只有一半
     */
    public static final int TYPE_BOTH_HALF = 3;
    /**
     * 只在中间加空格
     */
    public static final int TYPE_MIDDLE = 4;
    /**
     * 都加空格，两端为完整的
     */
    public static final int TYPE_BOTH = 5;
    /**
     * 只有两边加空格
     */
    public static final int TYPE_SIDE = 6;

    int type;
    int spaceWidth;

    public SpaceDecoration(Context context, int spaceWidth) {
        this(context, spaceWidth, TYPE_MIDDLE);
    }

    public SpaceDecoration(Context context, int spaceWidth, @TypeDef int type) {
        this.spaceWidth = UiUtils.dipToPx(context, spaceWidth);
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        boolean isVertical = true;
        int span = 1;
        if (layoutManager instanceof GridLayoutManager) {
            span = ((GridLayoutManager) layoutManager).getSpanCount();
            isVertical = ((GridLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
        } else if (layoutManager instanceof LinearLayoutManager) {
            isVertical = ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            span = ((GridLayoutManager) layoutManager).getSpanCount();
            isVertical = ((StaggeredGridLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
        }
        switch (type) {
            case TYPE_START:
                if (isVertical) {
                    outRect.top = spaceWidth;
                } else {
                    outRect.left = spaceWidth;
                }
                break;
            case TYPE_END:
                if (isVertical) {
                    outRect.bottom = spaceWidth;
                } else {
                    outRect.right = spaceWidth;
                }
                break;
            case TYPE_BOTH_HALF:
                if (isVertical) {
                    outRect.top = spaceWidth / 2;
                    outRect.bottom = spaceWidth / 2;
                } else {
                    outRect.left = spaceWidth / 2;
                    outRect.right = spaceWidth / 2;
                }
                break;
            case TYPE_MIDDLE: {
                int index = parent.getChildAdapterPosition(view);
                if (index >= span) {
                    if (isVertical) {
                        outRect.top = spaceWidth;
                    } else {
                        outRect.left = spaceWidth;
                    }
                } else {
                    outRect.set(0, 0, 0, 0);
                }
                break;
            }
            case TYPE_BOTH: {
                int index = parent.getChildAdapterPosition(view);
                if (isVertical) {
                    outRect.bottom = spaceWidth;
                    if (index < span) {
                        outRect.top = spaceWidth;
                    }
                } else {
                    outRect.right = spaceWidth;
                    if (index < span) {
                        outRect.left = spaceWidth;
                    }
                }
                break;
            }
            case TYPE_SIDE: {
                int index = parent.getChildAdapterPosition(view);
                if (isVertical) {
                    if (index < span) {
                        outRect.top = spaceWidth;
                    } else if (index >= parent.getAdapter().getItemCount() - span) {
                        outRect.bottom = spaceWidth;
                    }
                } else {
                    if (index < span) {
                        outRect.left = spaceWidth;
                    } else if (index >= parent.getAdapter().getItemCount() - span) {
                        outRect.right = spaceWidth;
                    }
                }
                break;
            }
        }
    }

    @IntDef({
            TYPE_START,
            TYPE_END,
            TYPE_BOTH_HALF,
            TYPE_MIDDLE,
            TYPE_BOTH,
            TYPE_SIDE,
    })
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface TypeDef {
    }

}
