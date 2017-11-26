package com.xy.hackernews.core;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by xy on 25/11/2017.
 */

public class StateManagerTest {

    private static final String TEST_STATE_KEY = "TEST_STATE_KEY";
    private static final String TEST_STATE = "TEST_STATE";
    private StateManager<String> stateManager;
    private Bundle bundle;

    class NonSerializableClass {
    }

    @Before
    public void setUp() throws Exception {
        stateManager = new StateManager<>(TEST_STATE_KEY);
        bundle = mock(Bundle.class);
        when(bundle.getSerializable(TEST_STATE_KEY)).thenReturn(TEST_STATE);
    }

    //TODO delete
    @Test
    public void test_null_state() throws Exception {
        //don't set any state before saving it
        when(bundle.getSerializable(TEST_STATE_KEY)).thenReturn(null);
        stateManager.saveState(bundle);
        assertEquals(null, stateManager.restoreState(bundle));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_null_bundle() throws Exception {
        stateManager.setState(TEST_STATE);
        stateManager.saveState(null);
    }

    @Test(expected = IllegalStateException.class)
    public void test_not_serializable_state() throws Exception {
        NonSerializableClass nonSerializableClass = new NonSerializableClass();
        StateManager<NonSerializableClass> wrongStateManager = new StateManager<>(TEST_STATE_KEY);
        wrongStateManager.setState(nonSerializableClass);
        wrongStateManager.saveState(bundle);
    }

    @Test
    public void test_valid_state() throws Exception {
        stateManager.setState(TEST_STATE);
        stateManager.saveState(bundle);
        assertEquals(TEST_STATE, stateManager.restoreState(bundle));
    }
}
