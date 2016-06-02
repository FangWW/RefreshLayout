# RefreshLayout
RecyclerView SwipeRefreshLayout下拉刷新上拉更多 + 简写万能adapter + 增加自定义headview

#### onPullDownToRefresh()上拉刷新回调
#### onPullUpToLoadMore(int page)下拉刷新回调 page自动增加1  默认10行一页
#### RVHolder extends RecyclerView.ViewHolder 简写适配器(原作者没找到了== 知道的告诉我一下 谢谢~)
      
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