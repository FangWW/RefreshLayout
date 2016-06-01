# RefreshLayout
RecyclerView SwipeRefreshLayout下拉刷新上拉更多 + 简写万能adapter + 增加自定义headview

      
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