package com.sidelance.weather.weather360.Commons;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sidelance.weather.weather360.R;

import butterknife.InjectView;

/**
 * CustomDialog Class
 */
public class CustomDialog {

    /**
     * Image View reference
     * */
    private ImageView iv;

    @InjectView(R.id.dialogImage) protected ImageView dialogIcon;
    @InjectView(R.id.loading_textView) protected TextView dialogTextView;


    public CustomDialog(ImageView iv) {
        this.iv = iv;
    }


}
