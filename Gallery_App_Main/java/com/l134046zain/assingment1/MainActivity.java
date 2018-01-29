
package com.l134046zain.assingment1;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button LoadImagesButton;
    
    String TAG="LifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        setSupportActionBar(myToolbar);


       LoadImagesButton= (Button) findViewById(R.id.button);

       LoadImagesButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent in=new Intent(getApplicationContext(),ImagesShow.class);

               Log.d(TAG, "onClick: Starting Activity");
               startActivity(in);
           }
       });

    }
}



