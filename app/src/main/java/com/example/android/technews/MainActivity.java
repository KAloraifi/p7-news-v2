package com.example.android.technews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&q=tech&api-key=test";
    private ArticleAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    private static final int ARTICLE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = findViewById(R.id.list);
        mAdapter = new ArticleAdapter(MainActivity.this, new ArrayList<Article>());
        mEmptyView = findViewById(R.id.empty_view);
        mProgressBar = findViewById(R.id.progress_bar);

        articleListView.setAdapter(mAdapter);
        articleListView.setEmptyView(mEmptyView);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = mAdapter.getItem(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = currentArticle.getWebUrl();
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        LoaderManager loaderManager = getSupportLoaderManager();
        if (isOnline()) {
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet);
        }
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setText(R.string.no_result);

        // Clear the adapter of previous article data.
        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear adapter data on resettting loader
        mAdapter.clear();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
