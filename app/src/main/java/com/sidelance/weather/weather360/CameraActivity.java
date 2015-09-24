package com.sidelance.weather.weather360;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class CameraActivity extends Activity {


    /**
     * Reference to Views
     * */
    @InjectView(R.id.cameraButton) protected Button cameraButton;
    @InjectView(R.id.cameraImageView) protected ImageView cameraImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        ButterKnife.inject(this);

    }



    @OnClick(value = R.id.cameraButton) protected void onStartButtonClicked(){

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent trigger = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(trigger, 100);

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            cameraButton.setVisibility(View.GONE);

            cameraImage.setVisibility(View.VISIBLE);

            cameraImage.setImageBitmap(bitmap);
        }
    }


}
