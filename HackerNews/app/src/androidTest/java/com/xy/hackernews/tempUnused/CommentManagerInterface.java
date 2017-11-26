package com.xy.hackernews.tempUnused;

import com.xy.hackernews.model.models.Comment;

import java.util.List;

import io.reactivex.Observable;

/**
 * only for testing purpose.
 */

public interface CommentManagerInterface {

    /*
    get list a comments
     */
    Observable<List<Comment>> getComments(List<Integer> commentIds/*, final int start, final int count*/);

    //get comment content
    Observable<Comment> getCommentContent(final int commentId);
}
