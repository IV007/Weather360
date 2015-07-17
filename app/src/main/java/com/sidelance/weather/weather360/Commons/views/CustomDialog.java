package com.sidelance.weather.weather360.commons.views;

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
