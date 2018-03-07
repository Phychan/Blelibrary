package com.phychan.mylibrary.base;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.phychan.mylibrary.R;

/**
 * Created by 陈晖 on 2017/11/20.
 */

public abstract class BaseListActivity<T> extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    public RecyclerView recyclerview;
    SwipeRefreshLayout srlRecyclerview;

    protected BaseQuickAdapter<T, BaseViewHolder> adapter;

    int pageIndex = 0;

    int pageCount = 10;

    @Override
    protected int getContentViewId() {
        return R.layout.swiperefresh_recyclerview;
    }

    @Override
    protected void init() {
        initWedgit();
        initAdapter();

    }

    private void initWedgit() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        srlRecyclerview = (SwipeRefreshLayout) findViewById(R.id.srl_recyclerview);
        srlRecyclerview.setEnabled(canRefresh());
        if (canRefresh()){
            srlRecyclerview.setOnRefreshListener(this);
        }
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<T, BaseViewHolder>(getItemLayoutId()) {
            @Override
            protected void convert(BaseViewHolder helper, T item) {
                handleAdapterHelper(helper, item);
            }
        };
        recyclerview.setAdapter(adapter);
        adapter.setEnableLoadMore(canLoadMore());
        adapter.setOnLoadMoreListener(this,recyclerview);
    }

    protected abstract
    @LayoutRes
    int getItemLayoutId();

    protected abstract boolean canRefresh();

    protected abstract boolean canLoadMore();

    protected abstract void handleAdapterHelper(BaseViewHolder helper, T item);

    protected abstract void loadData(int pageIndex);

    protected void onRefreshListener() {
        pageIndex = 0;
        adapter.setEnableLoadMore(true);
        loadData(pageIndex);
    }

    protected void loadMore() {
        pageIndex++;
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        loadMore();
    }

    protected void okLoadMoreComplete(){
        srlRecyclerview.setRefreshing(false);
        adapter.setEnableLoadMore(false);
    }

    @Override
    public void onRefresh() {
        onRefreshListener();
    }
}
