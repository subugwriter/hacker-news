package com.xy.hackernews.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.xy.hackernews.R;
import com.xy.hackernews.RecyclerViewItemCountGreaterThanAssertion;
import com.xy.hackernews.TestData;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.xy.hackernews.view.CommentsActivity.EXT_COMMENT_ID_LIST;
import static com.xy.hackernews.view.CommentsActivity.EXT_STORY_TITLE;
import static com.xy.hackernews.view.CommentsActivity.STATE_COMMENTS;
import static org.junit.Assert.assertTrue;

/**
 * Created by xy on 25/11/2017.
 */

public class CommentsActivityTest {

    @Rule
    public ActivityTestRule<CommentsActivity> rule =
            new ActivityTestRule<>(CommentsActivity.class, false, false);

    private void launchActivity() {
        Intent intent = new Intent(InstrumentationRegistry.getContext(), CommentsActivity.class);
        intent.putExtra(EXT_STORY_TITLE, "story name");
        intent.putExtra(EXT_COMMENT_ID_LIST, new ArrayList<>(TestData.realCommentIds));
        rule.launchActivity(intent);
    }

    @Test
    public void test_create() throws Exception {
        launchActivity();
        onView(withId(R.id.comments_list)).check(new RecyclerViewItemCountGreaterThanAssertion(0));
    }

    @Test
    public void test_onSaveInstanceState() {
        launchActivity();
        final Bundle outState = new Bundle();
        final CommentsActivity activity = rule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onSaveInstanceState(outState);
                assertTrue(outState.containsKey(STATE_COMMENTS));
            }
        });
    }
}
