package com.l134046zain.assingment1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class SingleImageShow extends AppCompatActivity {

    String ImagePath;
    ImageView image;

    Bitmap bitmap;
    AssetManager assetManager;
    InputStream is;
    String TAG="LifeCycle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_show);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myChildToolbar);


        myChildToolbar.setTitle("ImagesList");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);




        image=(ImageView) findViewById(R.id.imageView2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Path : Images/"+ImagePath,Toast.LENGTH_SHORT).show();

            }
        });

        assetManager=getAssets();

        Intent intent=getIntent();
        ImagePath=intent.getStringExtra(ImagesShow.EXTRA_MESSAGE);

        try {

            is = assetManager.open("Images/" + ImagePath);
            bitmap = BitmapFactory.decodeStream(is);

            image.setImageBitmap(bitmap);
                }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }


    }


}
