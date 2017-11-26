package com.xy.hackernews.tempUnused;

import com.xy.hackernews.model.models.Story;

import java.util.List;

import io.reactivex.Observable;

/**
 * only for testing purpose.
 */

public interface StoryManagerInterface {

    /*
    get top stories
     */
    Observable<List<Story>> getTopStories(final int start, final int count);

    /*
    get story content
     */
    Observable<Story> getStoryContent(final int storyId);
}
