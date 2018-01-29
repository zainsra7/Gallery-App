package com.l134046zain.assingment1;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Zan on 10/1/2016.
 */
public class Images
{
    String Path;
    boolean isSelected;



    public String getPath() {

        return Path;
    }


    Images(String p)
    {
        Log.d("LifeCycle", "ImagesConstructor: ");


        Path=p;
        isSelected=false;
    }

    public  boolean IsSelected(){return isSelected;}


    public void ToggleSelection()
    {
        isSelected = !isSelected;
    }




}
