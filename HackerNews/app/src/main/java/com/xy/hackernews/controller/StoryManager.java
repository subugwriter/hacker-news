package com.xy.hackernews.controller;

import com.xy.hackernews.model.http.ServiceFactory;
import com.xy.hackernews.model.models.Story;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by xy on 25/11/2017.
 */
//manager class to handle all application logic, including data computation, DB and internet interaction, and cache runtime data.
public class StoryManager/* implements StoryManagerInterface*/ {
    public static final String TAG = StoryManager.class.getSimpleName();

    public StoryManager() {
    }

    /*
     * get top stories
     */
    public Observable<List<Story>> getTopStories(final int start, final int count) {
        return ServiceFactory.getHackerNewsService().getTopStoryIds()
                .flatMapObservable(new Function<List<Integer>, ObservableSource<Story>>() {
                    @Override
                    public ObservableSource<Story> apply(List<Integer> storyIds) throws Exception {
                        return Observable.fromIterable(storyIds)
                                .skip(start - 1)
                                .take(count)
                                .concatMapEager(new Function<Integer, ObservableSource<Story>>() {
                                    @Override
                                    public ObservableSource<Story> apply(Integer storyId) throws Exception {
                                        return getStoryContent(storyId);
                                    }
                                });
                    }
                })
                .filter(new Predicate<Story>() {
                    @Override
                    public boolean test(Story story) throws Exception {
                        return story.getType().equals("story");
                    }
                })
                .toList()
                .toObservable();
    }

    /*
    get story content
     */
    public Observable<Story> getStoryContent(final int storyId) {
        Single<Story> obj = ServiceFactory.getHackerNewsService().getStoryContent(storyId);
        if (obj != null) {
            return obj.toObservable().cast(Story.class);
        } else {
            return null;
        }
    }
}
