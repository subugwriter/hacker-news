package com.xy.hackernews.controller;

import com.xy.hackernews.TestData;
import com.xy.hackernews.model.http.HackerNewsService;
import com.xy.hackernews.model.http.ServiceFactory;
import com.xy.hackernews.model.models.Comment;

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
public class CommentManagerTest {
    private CommentManager commentManager;

    @Before
    public void setUp() throws Exception {
        HackerNewsService hackerNewsService = Mockito.mock(HackerNewsService.class);

        //use power mockito to mock static function
        PowerMockito.mockStatic(ServiceFactory.class);
        BDDMockito.given(ServiceFactory.getHackerNewsService()).willReturn(hackerNewsService);
//        //mock top story id list
//        when(hackerNewsService.getTopStoryIds()).thenReturn(Single.just(storyIds));
//        //mock stories
//        when(hackerNewsService.getStoryContent(storyIds.get(0)))
//                .thenReturn(Single.just(stories.get(0)));
//        when(hackerNewsService.getStoryContent(storyIds.get(1)))
//                .thenReturn(Single.just(stories.get(1)));
//        when(hackerNewsService.getStoryContent(storyIds.get(2)))
//                .thenReturn(Single.just(stories.get(2)));

        //mock comments
        Mockito.when(hackerNewsService.getCommentContent(TestData.commentIds.get(0)))
                .thenReturn(Single.just(TestData.comments.get(0)));
        Mockito.when(hackerNewsService.getCommentContent(TestData.commentIds.get(1)))
                .thenReturn(Single.just(TestData.comments.get(1)));
        Mockito.when(hackerNewsService.getCommentContent(TestData.commentIds.get(2)))
                .thenReturn(Single.just(TestData.comments.get(2)));

        commentManager = new CommentManager();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_comments() throws Exception {
        commentManager.getComments(TestData.commentIds)
                .test()
                .assertComplete()
                .assertResult(TestData.comments);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_comments_empty_list() throws Exception {
        List<Integer> emptyCommentIds = new ArrayList<>();
        List<Comment> emptyComments = new ArrayList<>();
        commentManager.getComments(emptyCommentIds)
                .test()
                .assertComplete()
                .assertResult(emptyComments);
    }

    //TODO
//    @SuppressWarnings("unchecked")
//    @Test
//    public void test_get_comments_with_invalid_id() throws Exception {
//        ArrayList<Integer> newCommentIds = new ArrayList<>(commentIds);
//        newCommentIds.add(20000004);
//        commentManager.getComments(newCommentIds)
//                .test()
//                .assertError(IllegalAccessError.class);
//                //.assertComplete()
//                //.assertResult(comments);
//    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_comment_content() throws Exception {
        for (int i = 0; i < TestData.commentIds.size(); i++) {
            commentManager.getCommentContent(TestData.commentIds.get(i))
                    .test()
                    .assertComplete()
                    .assertResult(TestData.comments.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_comment_content_invalid_id() throws Exception {
        assertEquals(null, commentManager.getCommentContent(20000004));
    }
}
