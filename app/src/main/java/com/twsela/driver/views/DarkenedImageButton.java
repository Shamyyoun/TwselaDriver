package com.twsela.driver.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.twsela.driver.utils.DarkerOnTouchListener;

/**
 * Created by Shamyyoun on 5/13/16.
 */
public class DarkenedImageButton extends ImageButton {
    public DarkenedImageButton(Context context) {
        super(context);
        setOnTouchListener(new DarkerOnTouchListener(this));
    }

    public DarkenedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new DarkerOnTouchListener(this));
    }

    public DarkenedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(new DarkerOnTouchListener(this));
    }
}
