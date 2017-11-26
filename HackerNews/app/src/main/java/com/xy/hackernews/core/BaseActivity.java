package com.xy.hackernews.core;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by xy on 25/11/2017.
 */
public class BaseActivity extends AppCompatActivity {
//    protected ProgressDialog mLoadingBar;
//
//    /**
//     * show loadingBar
//     */
//    public void showLoadingBar(String message) {
//        showLoadingBar(message, false, false);
//    }
//
//    /**
//     * show loadingBar
//     */
//    public void showLoadingBar(String message, DialogInterface.OnCancelListener onCancelListener) {
//        showLoadingBar(message, false, true);
//        mLoadingBar.setOnCancelListener(onCancelListener);
//    }
//
//    /**
//     * show loadingBar
//     */
//    public void showLoadingBar(String message, boolean indeterminate, boolean cancelable) {
//        if (null == mLoadingBar) {
//            mLoadingBar = new ProgressDialog(this, R.style.progressTheme);
//        }
//
//        mLoadingBar.getWindow().setGravity(Gravity.CENTER);
//        mLoadingBar.setMessage(message);
//        mLoadingBar.setIndeterminate(indeterminate);
//        mLoadingBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.drawable_progress_bar));
//        mLoadingBar.setCancelable(cancelable);
//
//        mLoadingBar.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismissLoadingBar();
//                    return true;
//                }
//                return keyCode == KeyEvent.KEYCODE_MENU;
//            }
//        });
//        mLoadingBar.show();
//    }
//
//    /**
//     * dismiss current loadingBar
//     *
//     * @see #mLoadingBar
//     */
//    public void dismissLoadingBar() {
//        if (mLoadingBar != null && mLoadingBar.isShowing()) {
//            mLoadingBar.dismiss();
//            mLoadingBar = null;
//        }
//    }
}
