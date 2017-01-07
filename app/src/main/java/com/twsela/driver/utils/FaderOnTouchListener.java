package com.twsela.driver.utils;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Shamyyoun on 10/4/2015.
 * A utility class to fade out an view on touch event
 */
public class FaderOnTouchListener implements View.OnTouchListener {
    private View view;

    public FaderOnTouchListener(final View view) {
        super();
        this.view = view;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (view != null) {
            if (action == MotionEvent.ACTION_DOWN) {
                ViewUtil.alpha(view, 0.7f);
            } else if (action == MotionEvent.ACTION_MOVE) {
                Rect r = new Rect();
                view.getLocalVisibleRect(r);
                if (!r.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    ViewUtil.alpha(view, 1);
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                ViewUtil.alpha(view, 1);
            }
        }
        return false;

    }
}
