# RefreshLayout
RecyclerView SwipeRefreshLayout下拉刷新上拉更多 + 简写万能adapter + 增加自定义headview

>优点：
>1. 控件内部维护翻页page和size,初始时设置好一页数据size(默认20条)。再由此判断刷新或加载时如果服务端没有返回size条数据就是最后一行,然后控件内部禁止翻页加载更多状态,再次刷新时重新恢复可加载更多状态。
>2. 空,异常,正常三种显示状态切换。只有当刷新时,控件维护的List<T>为空时自动切换为空视图,当List<T>不为空时,自动切换为正常视图。加载更多时,由于List<T>中有数据,始终为正常视图。

[2017/12/25]

增加完整demo演示。数据来源 ONE api。

使用该项目app：[Mind:提高专注度](https://www.coolapk.com/apk/146583)

###  使用方式 
    compile 'com.w.support:refreshlayout-view:1.0'  (暂时没有上传jcenter最新版)
    
###  XML
    <com.refreshlayoutview.RefreshLayoutView
        android:id="@+id/rf_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

#### onPullDownToRefresh()上拉刷新回调
#### onPullUpToLoadMore(int page)下拉刷新回调 page自动增加1  默认10行一页
#### RVHolder extends RecyclerView.ViewHolder 简写适配器(原作者没找到了== 知道的告诉我一下 谢谢~)

⬇⬇⬇此处有图片(3.5M)加载慢⬇⬇⬇

![github](https://raw.githubusercontent.com/FangWW/RefreshLayout/master/app/ezgif.com-resize.gif "github")

⬆⬆⬆此处有图片(3.5M)加载慢⬆⬆⬆

###  设置分页大小(当服务端返回数量不等于pagesize时不能上拉)
      setPageSize(int pageSize)

###  不带headerview
      new RefreshLayoutAdapter<List>(this, R.layout.recycleritem_layout)

###  设置headerview
      new RefreshLayoutAdapter<List>(this, R.layout.recycleritem_layout, headerView)

###  设置能否上拉或者下拉
      setEnabledUP(boolean);
      setEnabledDown(boolean);

###  主动刷新和主动停止下拉
      onRefreshing();
      onStopRefreshing();

###  内容视图,空视图,错误视图切换
      showConetntView();
      showEmptyView();
      showErrorView();

###  设置空视图,错误视图
      setEmptyView(View emptyView);
      setEmptyView(@DrawableRes int imgRes, @StringRes int msg) 
      setErrorView(View errorView);
      setErrorView(@DrawableRes int imgRes, @StringRes int msg);

###  刷新传递数据
      postMsgDataRefreshed(list);
      
###  加载传递数据
      postMsgDataLoadedMore(list);
      
###  使用的第三方库 谢谢~
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'com.bigkoo:convenientbanner:2.0.5'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
