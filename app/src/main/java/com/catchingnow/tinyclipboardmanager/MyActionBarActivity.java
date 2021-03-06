package com.catchingnow.tinyclipboardmanager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

/**
 * Created by heruoxin on 15/2/28.
 */
public class MyActionBarActivity extends ActionBarActivity {

    //Fix LG support V7 bug:
    //https://code.google.com/p/android/issues/detail?id=78154
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //check softKeyboard show or hide
    //http://stackoverflow.com/questions/25216749/softkeyboard-open-and-close-listener-in-an-activity-in-android
    private boolean isKeyboardShow = false;

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();

            if (heightDiff <= contentViewTop) {
                if (!isKeyboardShow) return;
                onHideKeyboard();
                isKeyboardShow = false;

            } else {
                if (isKeyboardShow) return;
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);
                isKeyboardShow = true;

            }
        }
    };

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    protected void onShowKeyboard(int keyboardHeight) {
    }

    protected void onHideKeyboard() {
    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(android.R.id.content);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        super.onNewIntent(intent);
    }

    @Override
    public Intent getIntent() {
        Intent intent = super.getIntent();
        if (intent == null) {
            return new Intent();
        } else {
            return intent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(16);
        } else {
            View mToolbarShadow = findViewById(R.id.my_toolbar_shadow);
            if (mToolbarShadow != null) {
                mToolbarShadow.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CBWatcherService.startCBService(this, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CBWatcherService.startCBService(this,true , 1);
    }
}
