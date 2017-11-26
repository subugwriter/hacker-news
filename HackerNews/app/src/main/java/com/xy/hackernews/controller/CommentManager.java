package com.xy.hackernews.controller;

import com.xy.hackernews.model.http.ServiceFactory;
import com.xy.hackernews.model.models.Comment;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by xy on 25/11/2017.
 */
//manager class to handle all application logic, including data computation, DB and internet interaction, and cache runtime data.
public class CommentManager/* implements CommentManagerInterface*/{
    public static final String TAG = CommentManager.class.getSimpleName();

    public CommentManager(){
    }

    /*
     * get list a comments
     */
    public Observable<List<Comment>> getComments(List<Integer> commentIds/*, final int start, final int count*/) {
        return Observable.fromIterable(commentIds)
                //.skip(start-1)
                //.take(count)
                .concatMapEager(new Function<Integer, ObservableSource<Comment>>() {
                    @Override
                    public ObservableSource<Comment> apply(Integer commentId) throws Exception {
                        return getCommentContent(commentId);
                    }
                })
                .toList()
                .toObservable();
    }

    /*
     * get comment content
     */
    public Observable<Comment> getCommentContent(final int commentId) {
        Single<Comment> obj = ServiceFactory.getHackerNewsService().getCommentContent(commentId);
        if(obj != null) {
            return obj.toObservable().cast(Comment.class);
        } else {
            return null;
        }
    }
}
