package com.xy.hackernews.core;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by xy on 25/11/2017.
 */
/*
* state manager to manage all cached data
*/
public class StateManager<T> {
    private String mKey;
    private T mState = null;

    public StateManager(String key) {
        mKey = key;
    }

    public void setState(T state) {
        mState = state;
    }

    public void saveState(Bundle bundle) {
        if ((bundle == null)) {
            throw new IllegalArgumentException("must pass in a valid bundle!");
        } else {
            if (mState != null) {
                if (mState instanceof Serializable) {
                    bundle.putSerializable(mKey, (Serializable) mState);
                } else {
                    throw new IllegalStateException(mState + " must be serializable!");
                }
            }
        }
    }

    public T restoreState(Bundle bundle) {
        mState = bundle == null ? null : (T) bundle.getSerializable(mKey);
        return mState;
    }
}
