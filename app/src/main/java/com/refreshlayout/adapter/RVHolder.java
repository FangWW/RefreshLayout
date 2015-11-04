/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Duthor FangJW
 * @Date 15/11/4
 */
public class RVHolder extends RecyclerView.ViewHolder {


    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder = ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}