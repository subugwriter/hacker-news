package com.xy.hackernews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.xy.hackernews.R;
import com.xy.hackernews.controller.StoryManager;
import com.xy.hackernews.core.BaseActivity;
import com.xy.hackernews.core.StateManager;
import com.xy.hackernews.model.models.Story;
import com.xy.hackernews.view.widget.EndlessRecyclerOnScrollListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xy on 25/11/2017.
 */
public class TopStoriesActivity extends BaseActivity {
    public static final String STATE_TOP_STORIES = "STATE_TOP_STORIES";

    private EndlessRecyclerOnScrollListener mScrollListener = null;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private RecyclerView mRecyclerView = null;
    private TopStoriesListAdapter mTopStoriesListAdapter = null;

    //cached data
    StateManager<List<Story>> mTopStories = new StateManager<List<Story>>(STATE_TOP_STORIES);
    /**
     * Collects all subscriptions to unsubscribe later
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //StoryManager.getInstance().onSaveInstanceState(outState);
        mTopStories.saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);

        mRecyclerView = (RecyclerView) findViewById(R.id.top_stories_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter
        mTopStoriesListAdapter = new TopStoriesListAdapter(this);
        mRecyclerView.setAdapter(mTopStoriesListAdapter);

        // enable pull up for endless loading
        mScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // load more
                loadData(current_page);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        // enable pull down to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.top_stories_swipe_refresh);
        final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // load 1st page
                loadData(1);
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        List<Story> topStories = mTopStories.restoreState(savedInstanceState);
        if (topStories != null && topStories.size() > 0) {
            mTopStoriesListAdapter.setData(topStories);
        } else {
            ;
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    // directly call onRefresh() method
                    onRefreshListener.onRefresh();
                }
            });
        }
    }

//    protected StoryManagerInterface getManager(){
//        return new StoryManager();
//    }

    private void loadData(final int current_page) {
        int startItem;
        int pageSize = 15;
        startItem = (current_page - 1) * pageSize + 1;
        mCompositeDisposable.add(new StoryManager().getTopStories(startItem, pageSize)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
                .subscribe(new Consumer<List<Story>>() {
                    @Override
                    public void accept(final List<Story> topStories) throws Exception {
                        //update list
                        if (current_page == 1) {
                            mTopStoriesListAdapter.setData(topStories);
                            mTopStories.setState(topStories);
                        } else {
                            mTopStoriesListAdapter.addData(topStories);
                            mTopStories.setState(mTopStoriesListAdapter.getData());
                        }
                        // after refresh is done, remember to call the following code
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                        }
                        mScrollListener.setLoading(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) /*throws Exception*/ {
                        //show error
                        Toast.makeText(TopStoriesActivity.this, getString(R.string.loading_error), Toast.LENGTH_LONG).show();
                        // after refresh is done, remember to call the following code
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                        }
                        mScrollListener.setLoading(false);
                    }
                })
        );
    }

    @Override
    protected void onDestroy() {
        // DO NOT CALL .dispose()
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}
