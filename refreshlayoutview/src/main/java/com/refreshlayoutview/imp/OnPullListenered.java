/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview.imp;

import java.util.List;

/**
 * 刷新完成
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public interface OnPullListenered {
    /**
     * 传递刷新数据
     *
     * @param dataList
     */
    void onPullDownToRefreshed(List dataList);

    /**
     * 传递加载更多数据
     *
     * @param dataList
     */
    void onPullUpToLoadMoreed(List dataList);
}
