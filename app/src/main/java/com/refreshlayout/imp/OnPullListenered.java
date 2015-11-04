/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout.imp;

import java.util.List;

/**
 * 刷新完成
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public interface OnPullListenered {
    void onPullDownToRefreshed(List dataList);

    void onPullUpToLoadMoreed(List dataList);
}
