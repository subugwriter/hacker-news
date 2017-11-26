package com.xy.hackernews.tempUnused;

import android.accounts.NetworkErrorException;

import com.xy.hackernews.TestData;
import com.xy.hackernews.model.models.Story;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by xy on 25/11/2017.
 */
public class StubStoryManager implements StoryManagerInterface {
    public StubStoryManager() {
    }

    /*
     * get top stories
     */
    public Observable<List<Story>> getTopStories(final int start, final int count) {
        return Single.create(new SingleOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(SingleEmitter<List<Integer>> e) throws Exception {
                e.onSuccess(TestData.commentIds);
            }
        })
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
     * get story content
     */
    public Observable<Story> getStoryContent(final int storyId) {
        Single<Story> obj = Single.create(new SingleOnSubscribe<Story>() {
            @Override
            public void subscribe(SingleEmitter<Story> e) throws Exception {
                if (TestData.storyIds.contains(storyId)) {
                    int index = TestData.storyIds.indexOf(storyId);
                    e.onSuccess(TestData.stories.get(index));
                } else {
                    e.onError(new NetworkErrorException("story id is invalid"));
                }
            }
        });
        return obj.toObservable();
    }
}
