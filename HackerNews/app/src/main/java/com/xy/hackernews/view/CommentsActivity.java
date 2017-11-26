package com.xy.hackernews.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.xy.hackernews.R;
import com.xy.hackernews.controller.CommentManager;
import com.xy.hackernews.core.BaseActivity;
import com.xy.hackernews.core.StateManager;
import com.xy.hackernews.model.models.Comment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xy on 25/11/2017.
 */
public class CommentsActivity extends BaseActivity {

    public static final String EXT_STORY_TITLE = "EXT_STORY_TITLE";
    public static final String EXT_COMMENT_ID_LIST = "EXT_COMMENT_ID_LIST";

    public static final String STATE_COMMENTS = "STATE_COMMENTS";

    //private EndlessRecyclerOnScrollListener mScrollListener = null;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private RecyclerView mRecyclerView = null;
    private CommentsListAdapter mCommentsListAdapter = null;

    ArrayList<Integer> mCommentIds = new ArrayList<>();

    //cached data
    StateManager<List<Comment>> mComments = new StateManager<List<Comment>>(STATE_COMMENTS);
    /**
     * Collects all subscriptions to unsubscribe later
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    public static void startWithTitleAndCommentIds(Context context, String title, List<Integer> commentIds) {
        if (context != null) {
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra(EXT_STORY_TITLE, title);
            intent.putExtra(EXT_COMMENT_ID_LIST, new ArrayList<>(commentIds));
            context.startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //StoryManager.getInstance().onSaveInstanceState(outState);
        mComments.saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        String title = getIntent().getStringExtra(EXT_STORY_TITLE);
        mCommentIds = (ArrayList<Integer>) getIntent().getSerializableExtra(EXT_COMMENT_ID_LIST);

        //set title
        getSupportActionBar().setTitle(title);

        mRecyclerView = (RecyclerView) findViewById(R.id.comments_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter
        mCommentsListAdapter = new CommentsListAdapter();
        mRecyclerView.setAdapter(mCommentsListAdapter);

        // enable pull down to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comments_swipe_refresh);
        final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // load data. Always load all the commands at one time since number of commands are relatively small
                loadData();
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        List<Comment> comments = mComments.restoreState(savedInstanceState);
        if (comments != null && comments.size() > 0) {
            mCommentsListAdapter.setData(comments);
        } else {
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


//    protected CommentManagerInterface getManager(){
//        return new CommentManager();
//    }

    private void loadData() {
        mCompositeDisposable.add(new CommentManager().getComments(mCommentIds)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
                .subscribe(new Consumer<List<Comment>>() {
                    @Override
                    public void accept(
                                            /*@io.reactivex.annotations.NonNull*/ final List<Comment> comments)//TODO
                            throws Exception {
                        //update list
                        mCommentsListAdapter.setData(comments);
                        mComments.setState(comments);
                        // after refresh is done, remember to call the following code
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) /*throws Exception*/ {
                        //show error
                        Toast.makeText(CommentsActivity.this, getString(R.string.loading_error), Toast.LENGTH_LONG).show();
                        // after refresh is done, remember to call the following code
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                        }
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
