package com.sidelance.weather.weather360.Commons;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sidelance.weather.weather360.MainActivity;
import com.sidelance.weather.weather360.R;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.InjectView;

public class Splash extends ActionBarActivity {

    /**
     * References to Views
     * */
    @InjectView(R.id.dialogImage) protected ImageView dialogIcon;
    @InjectView(R.id.loading_textView) protected TextView dialogTextView;


    /**
     * Timer required for duration
     * */
    private  static long timer = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    @Override
    protected void onResume() {
        super.onResume();

        RotateAnimation rotate = new RotateAnimation(0f, 350f, 15f, 15f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(timer);
        dialogIcon.startAnimation(rotate);

        startUpFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    public void startUpFinish(){

        dialogIcon.setAnimation(null);
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *
     * Method to spin image view
     *
     * @param id Image View displayed in fragment.
     * */
    public void spinImage(ImageView id){

        RotateAnimation rotate = new RotateAnimation(0f, 180f, 30f, 30f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(timer);
        id.startAnimation(rotate);

    }

    public void stopImageSpin(ImageView id){

        dialogIcon.setAnimation(null);
    }

    public void showDialog(){

        spinImage(dialogIcon);
    }

    public void hideDialog(){

        stopImageSpin(dialogIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
