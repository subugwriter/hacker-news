package com.xy.hackernews.controller;


import com.xy.hackernews.TestData;
import com.xy.hackernews.model.http.HackerNewsService;
import com.xy.hackernews.model.http.ServiceFactory;
import com.xy.hackernews.model.models.Story;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;

/**
 * Created by xy on 25/11/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class StoryManagerTest {
    private HackerNewsService hackerNewsService;
    private StoryManager storyManager;

    @Before
    public void setUp() throws Exception {
        hackerNewsService = Mockito.mock(HackerNewsService.class);

        //use power mockito to mock static function
        PowerMockito.mockStatic(ServiceFactory.class);
        BDDMockito.given(ServiceFactory.getHackerNewsService()).willReturn(hackerNewsService);

        //mock top story id list
        Mockito.when(hackerNewsService.getTopStoryIds()).thenReturn(Single.just(TestData.storyIds));
        //mock stories
        Mockito.when(hackerNewsService.getStoryContent(TestData.storyIds.get(0)))
                .thenReturn(Single.just(TestData.stories.get(0)));
        Mockito.when(hackerNewsService.getStoryContent(TestData.storyIds.get(1)))
                .thenReturn(Single.just(TestData.stories.get(1)));
        Mockito.when(hackerNewsService.getStoryContent(TestData.storyIds.get(2)))
                .thenReturn(Single.just(TestData.stories.get(2)));

//        //mock comments
//        when(hackerNewsService.getCommentContent(commentIds.get(0)))
//                .thenReturn(Single.just(comments.get(0)));
//        when(hackerNewsService.getCommentContent(commentIds.get(1)))
//                .thenReturn(Single.just(comments.get(1)));
//        when(hackerNewsService.getCommentContent(commentIds.get(2)))
//                .thenReturn(Single.just(comments.get(2)));

        storyManager = new StoryManager();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_top_stories() throws Exception {
        storyManager.getTopStories(1, 3)
                .test()
                .assertComplete()
                .assertResult(TestData.stories);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_top_stories_empty_list() throws Exception {
        List<Integer> emptyStoryIds = new ArrayList<>();
        List<Story> emptyStories = new ArrayList<>();
        //mock top story id list
        Mockito.when(hackerNewsService.getTopStoryIds()).thenReturn(Single.just(emptyStoryIds));

        storyManager.getTopStories(1, 3)
                .test()
                .assertComplete()
                .assertResult(emptyStories);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_top_stories_exceed_range() throws Exception {
        List<Story> expectedStories = new ArrayList<>(TestData.stories);

        storyManager.getTopStories(1, 5)
                .test()
                .assertComplete()
                .assertResult(TestData.stories);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_top_stories_1_item_subset() throws Exception {
        List<Story> expectedStories = new ArrayList<>(TestData.stories);
        expectedStories.remove(2);
        expectedStories.remove(1);

        storyManager.getTopStories(1, 1)
                .test()
                .assertComplete()
                .assertResult(expectedStories);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_top_stories_middle_subset() throws Exception {
        List<Story> expectedStories = new ArrayList<>(TestData.stories);
        expectedStories.remove(2);
        expectedStories.remove(0);

        storyManager.getTopStories(2, 1)
                .test()
                .assertComplete()
                .assertResult(expectedStories);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_story_content() throws Exception {
        for (int i = 0; i < TestData.storyIds.size(); i++) {
            storyManager.getStoryContent(TestData.storyIds.get(i))
                    .test()
                    .assertComplete()
                    .assertResult(TestData.stories.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_story_content_invalid_id() throws Exception {
        assertEquals(null, storyManager.getStoryContent(10000004));
    }
}
