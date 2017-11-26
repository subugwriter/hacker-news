package com.xy.hackernews.model.http;

import com.xy.hackernews.model.models.Comment;
import com.xy.hackernews.model.models.Story;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by xy on 25/11/2017.
 */
public interface HackerNewsService {
    String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    @GET("topstories.json")
    Single<List<Integer>> getTopStoryIds();

    @GET("item/{id}.json")
    Single<Story> getStoryContent(@Path("id") long storyId);

    @GET("item/{id}.json")
    Single<Comment> getCommentContent(@Path("id") long commentId);
}

