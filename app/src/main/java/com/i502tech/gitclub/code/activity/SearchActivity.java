package com.i502tech.gitclub.code.activity;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.i502tech.gitclub.R;
import com.i502tech.gitclub.base.BaseActivity;
import com.i502tech.gitclub.base.mvvm.Resource;
import com.i502tech.gitclub.code.adapter.ArticleAdapter;
import com.i502tech.gitclub.code.bean.Article;
import com.i502tech.gitclub.code.viewmodel.ArticleViewModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowLayout;

    @Inject
    ArticleViewModel articleViewModel;

    TagAdapter<String> tagAdapter;
    private ArticleAdapter mAdapter;
    private int page = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void bindData() {
        setTitle("搜索");
        mAdapter = new ArticleAdapter(new ArrayList<Article>(), this);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerview.setAdapter(mAdapter);

        final List<String> hotList = Arrays.asList("自定义View", "Tab", "WebView", "图片加载", "相机",
                "图表", "列表", "数据库", "蓝牙", "视频", "网络请求", "人脸识别", "OpenGL", "Canvas", "音频", "完整项目");
        tagAdapter = new TagAdapter<String>(hotList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = getLayoutInflater().inflate(R.layout.item_search_tag, parent, false);
                RoundTextView roundTextView = view.findViewById(R.id.roundtext);
                roundTextView.setText(hotList.get(position));
                return view;
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }

    @Override
    protected void dataObserver() {
        articleViewModel.getQueryLiveData().observe(this, resource -> {
            if (resource.isSuccess()){
                if (page == 0) {
                    mAdapter.setNewData(resource.data);
                }else {
                    mAdapter.addData(resource.data);
                }
            }else {
                toast(resource.msg);
            }
        });
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mAdapter.setOnLoadMoreListener(() -> {
            page++;
            articleViewModel.query(String.valueOf(page), String.valueOf(10), searchEdit.getText().toString());
        }, recyclerview);

        searchEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //关闭软键盘
                hideSoftKeyboard();
                flowLayout.setVisibility(View.GONE);
                articleViewModel.query(String.valueOf(page), String.valueOf(10), searchEdit.getText().toString());
                return true;
            }
            return false;
        });
        flowLayout.setOnTagClickListener((view, position, parent) -> {
            hideSoftKeyboard();
            flowLayout.setVisibility(View.GONE);
            searchEdit.setText(tagAdapter.getItem(position));
            page = 0;
            articleViewModel.query(String.valueOf(page), String.valueOf(10), searchEdit.getText().toString());
            return true;
        });
    }

}
