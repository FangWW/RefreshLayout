/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview.imp;

import java.util.List;

/**
 * 刷新接口
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public interface OnPullListener {
    /**
     * 当前第几页
     *
     * @return
     */
    int getPage();

    /**
     * 当前数据
     *
     * @return
     */
    List getDatas();

    /**
     * 刷新
     */
    void onPullDownToRefresh();

    /**
     * 下一次的页数
     * 默认十页为分页   如果不是十的倍数就是最后一页
     *
     * @param pager
     */
    void onPullUpToLoadMore(int pager);

    void setOnPullListenered(OnPullListenered onPullListenered);
}
