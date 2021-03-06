package com.i502tech.gitclub.code.repository;

import android.arch.lifecycle.MutableLiveData;

import com.i502tech.gitclub.api.HttpUtils;
import com.i502tech.gitclub.api.RetrofitManager;
import com.i502tech.gitclub.api.http.api.BaseResponse;
import com.i502tech.gitclub.api.http.api.subscriber.ResponseListener;
import com.i502tech.gitclub.base.mvvm.BaseRepository;
import com.i502tech.gitclub.base.mvvm.Resource;
import com.i502tech.gitclub.code.bean.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * description $desc$
 * created by jerry on 2019/4/11.
 */
@Singleton
public class ArticleRepository extends BaseRepository {
    public MutableLiveData<Resource<List<Article>>> articleLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<Integer>> sumLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<List<Article>>> queryLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<List<Article>>> articleTypeLiveData = new MutableLiveData<>();

    public void getArticleList(String page, String size) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("size", size);
        HttpUtils.ApiFunc(RetrofitManager.mApiService.getArticleList(map),
                listBaseResponse -> articleLiveData.setValue(listBaseResponse));
    }

    public void getArticleTotals() {
        HttpUtils.ApiFunc(RetrofitManager.mApiService.getArticleTotals(),
                integerBaseResponse -> sumLiveData.setValue(integerBaseResponse));
    }

    public void query(String page, String size, final String query) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("size", size);
        map.put("query", query);
        HttpUtils.ApiFunc(RetrofitManager.mApiService.query(map),
                listBaseResponse -> queryLiveData.setValue(listBaseResponse));
    }

    public void getMyStarArticles(String page, String size, String user_id){
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("size", size);
        map.put("userId", user_id);
        HttpUtils.ApiFunc(RetrofitManager.mApiService.getMyStarArticles(map),
                listBaseResponse -> articleTypeLiveData.setValue(listBaseResponse));
    }

    public void getMyContributeArticles(String page, String size, String user_id){
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("size", size);
        map.put("userId", user_id);
        HttpUtils.ApiFunc(RetrofitManager.mApiService.getMyContributeArticles(map),
                listBaseResponse -> articleTypeLiveData.setValue(listBaseResponse));
    }
}

