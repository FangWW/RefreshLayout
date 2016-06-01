# RefreshLayout
RecyclerView SwipeRefreshLayout下拉刷新上拉更多 + 简写万能adapter + 增加自定义headview

#### 下拉刷新回调onPullDownToRefresh()
#### 自动翻页onPullUpToLoadMore(int page) page为当前页数 并自动增加
#### 简写的万能适配器RecyclerView.Adapter<RVHolder> (作者忘了,没有找到==,能告诉我添加上 谢谢)



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

### 使用的第三方库 谢谢~
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'com.bigkoo:convenientbanner:2.0.5'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
