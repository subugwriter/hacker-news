package com.xy.hackernews.tempUnused;

import android.accounts.NetworkErrorException;

import com.xy.hackernews.TestData;
import com.xy.hackernews.model.models.Comment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

/**
 * Created by xy on 25/11/2017.
 */
public class StubCommentManager implements CommentManagerInterface {
    public StubCommentManager() {
    }

    /*
     * get list a comments
     */
    public Observable<List<Comment>> getComments(List<Integer> commentIds) {
        return Observable.fromIterable(commentIds)
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
        Single<Comment> obj = Single.create(new SingleOnSubscribe<Comment>() {
            @Override
            public void subscribe(SingleEmitter<Comment> e) throws Exception {
                if (TestData.commentIds.contains(commentId)) {
                    int index = TestData.commentIds.indexOf(commentId);
                    e.onSuccess(TestData.comments.get(index));
                } else {
                    e.onError(new NetworkErrorException("comment id is invalid"));
                }
            }
        });
        return obj.toObservable();
    }
}
