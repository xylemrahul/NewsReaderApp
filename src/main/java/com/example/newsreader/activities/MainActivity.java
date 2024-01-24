package com.example.newsreader.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsreader.R;
import com.example.newsreader.adapters.AdapterListNews;
import com.example.newsreader.models.Article;
import com.example.newsreader.viewmodels.MainViewModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MainActivity context;
    MainViewModel viewModel;
    AdapterListNews adapterListNews;
    List<Article> articleList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        articleList = new ArrayList<>();
        adapterListNews = new AdapterListNews(articleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterListNews);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getArticleLiveData().observe(context, articleListUpdateObserver);

        viewModel.loadJSONFromAsset(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setQueryHint(getString(R.string.search_in_everything));
        if (searchView != null)
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (viewModel != null) viewModel.searchNews(query);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        return true;
    }

    Observer<List<Article>> articleListUpdateObserver = new Observer<List<Article>>() {
        @Override
        public void onChanged(List<Article> article) {
            articleList.clear();
            if (article != null) {
                articleList.addAll(article);
            }
            adapterListNews.notifyDataSetChanged();
        }
    };

}
