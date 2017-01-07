package com.twsela.driver.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by Shamyyoun on 2/3/2016.
 */
public class ViewUtil {
    public static final int DEFAULT_DURATION = 250;

    /**
     * method used to show or hide view in default duration
     */
    public static void fadeView(View view, boolean show) {
        fadeView(view, show, DEFAULT_DURATION, View.GONE);
    }

    /**
     * method used to show or hide view
     */
    public static void fadeView(View view, boolean show, int duration) {
        fadeView(view, show, duration, View.GONE);
    }

    /**
     * method used to show or hide view with INVISIBLE constant in default duration
     */
    public static void hideView(final View view, int invisibleConstant) {
        fadeView(view, false, DEFAULT_DURATION, invisibleConstant);
    }

    /**
     * method used to show or hide view with INVISIBLE constant
     */
    public static void hideView(final View view, int duration, int invisibleConstant) {
        fadeView(view, false, duration, invisibleConstant);
    }

    /**
     * method used to shows or hides a view with a smooth animation in specific duration
     */
    private static void fadeView(final View view, final boolean show, int duration, final int invisibleConstant) {
        if (view != null) {
            view.animate().setDuration(duration).alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (show)
                                view.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (!show)
                                view.setVisibility(invisibleConstant);
                        }
                    });
        }
    }

    /**
     * method, used to fade in view with passed duration
     *
     * @param view
     * @param duration
     */
    public static void fadeInView(final View view, int duration) {
        if (view != null) {
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(duration);
            animation.setFillAfter(true);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }

    /**
     * Apply alpha to a view in code
     *
     * @param view  to apply alpha on it
     * @param alpha value to apply
     */
    public static void alpha(View view, float alpha) {
        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    /**
     * Apply alpha to a view in code with a duration to the animation
     *
     * @param view     to apply alpha on it
     * @param alpha    value to apply
     * @param duration value for the animation
     */
    public static void alpha(View view, float alpha, long duration) {
        AlphaAnimation animation = new AlphaAnimation(view.getAlpha(), alpha);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    /**
     * method, used to show first view and hide other views
     *
     * @param viewToShow
     * @param viewsToHide
     */
    public static void showOneView(View viewToShow, View... viewsToHide) {
        viewToShow.setVisibility(View.VISIBLE);
        for (View view : viewsToHide) {
            view.setVisibility(View.GONE);
        }
    }
}
