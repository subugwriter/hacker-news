package com.xy.hackernews.view;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.xy.hackernews.R;
import com.xy.hackernews.RecyclerViewItemCountAssertion;
import com.xy.hackernews.TestData;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.xy.hackernews.InstrumentedTestUtil.clickChildViewWithId;
import static com.xy.hackernews.InstrumentedTestUtil.waitFor;
import static com.xy.hackernews.view.TopStoriesActivity.STATE_TOP_STORIES;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertTrue;

/**
 * Created by xy on 25/11/2017.
 */

public class TopStoriesActivityTest {

    @Rule
    public ActivityTestRule<TopStoriesActivity> rule =
            new ActivityTestRule<>(TopStoriesActivity.class/*, false, false*/);

    private void launchActivity() {
        Intent intent = new Intent(InstrumentationRegistry.getContext(), TopStoriesActivity.class);
        rule.launchActivity(intent);
    }

    @Test
    public void test_create() throws Exception {
        onView(isRoot()).perform(waitFor(5000));
        onView(withId(R.id.top_stories_list)).check(new RecyclerViewItemCountAssertion(12));
    }

    @Test
    public void test_scroll_to_load_more() {
        //TODO
//        onView(withId(R.id.top_stories_list)).perform(scrollToPosition(12));
//        //Thread.sleep(10000);
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                onView(withId(R.id.top_stories_list)).check(withItemCount(24));
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 10000);

//        onView(withId(R.id.top_stories_list)).perform(waitFor(10000));
//        onView(withId(R.id.top_stories_list)).check(withItemCount(24));
    }

    @Test
    public void test_click_item() {
        onView(isRoot()).perform(waitFor(5000));
        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));

        onView(withId(R.id.top_stories_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        intended(expectedIntent);
        Intents.release();
    }

    @Test
    public void test_click_comment() {
        onView(isRoot()).perform(waitFor(5000));
        onView(withId(R.id.top_stories_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, clickChildViewWithId(R.id.comments)));
    }

    @Test
    public void test_onSaveInstanceState() {
        onView(isRoot()).perform(waitFor(5000));
        final Bundle outState = new Bundle();
        final TopStoriesActivity activity = rule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onSaveInstanceState(outState);
                assertTrue(outState.containsKey(STATE_TOP_STORIES));
            }
        });
    }
}
