/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview.imp;

import java.util.List;

/**
 * 刷新接口
 *
 * @Author FangJW
 * @Date 15/11/3
 */
public interface OnPullListener {
    int getPage();

    List getDatas();

    void onPullDownToRefresh();

    void onPullUpToLoadMore(int pager);

    void setOnPullListenered(OnPullListenered onPullListenered);
}
