package com.example.newsreader.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsreader.models.Article;
import com.example.newsreader.models.News;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Article>> articleLiveData;
    private List<Article> articleList;

    public MainViewModel() {
        articleLiveData = new MutableLiveData<>();
        articleList = new ArrayList<>();
    }

    public MutableLiveData<List<Article>> getArticleLiveData() {
        return articleLiveData;
    }

    public void loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("news.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        Gson gson = new Gson();
        News news = gson.fromJson(json,News.class);
        fillNewsList(news);
    }

    private void getSearchedNews(String keyword) {
        //TODO
    }

    private void fillNewsList(News news) {
        articleList.addAll(news.getArticles());
        articleLiveData.setValue(articleList);
    }

    public void searchNews(String keyword) {
        getSearchedNews(keyword);
    }
}
