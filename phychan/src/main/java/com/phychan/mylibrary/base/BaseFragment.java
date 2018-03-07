package com.phychan.mylibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wxl on 2017/10/10.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;

    protected BaseActivity activity;

    protected Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
        }
        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    /**
     * 获取布局文件
     *
     * @return 布局文件ID
     */
    protected abstract
    @LayoutRes
    int getContentViewId();

    /**
     * 初始化内容
     */
    protected abstract void init();
}
