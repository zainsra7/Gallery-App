package com.l134046zain.assingment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


import java.util.ArrayList;


public class CustomImageAdapter extends BaseAdapter {

    String TAG = "LifeCycle";

    public Context c;
    public AssetManager assetManager;


    ArrayList<String> Images;
    ArrayList<String> SelectedImages_Adapter;


    ArrayList<Images> CustomAdapter_Images_List=new ArrayList<>();


    LayoutInflater inflater;

    public CustomImageAdapter(Context con, ArrayList<Images> imag) {

        this.c = con;

        //CustomAdapter_Images_List=new ArrayList<>(imag);


        CustomAdapter_Images_List=imag;


        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assetManager=con.getAssets();

    }


    @Override
    public int getCount() {
        return CustomAdapter_Images_List.size();

    }

    @Override
    public Object getItem(int i) {
        return CustomAdapter_Images_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    //For Optimization , not to call FindViewById again and again
    class ViewHolder {
        ImageView image;
        TextView selectedTextView;

        ViewHolder(View v) {

            image = (ImageView) v.findViewById(R.id.picture);
            selectedTextView = (TextView) v.findViewById(R.id.SelectedText);
        }

    }

    public void remove(Images [] im)
    {


        for(int x=0;x<im.length;x++)
            CustomAdapter_Images_List.remove(im[x]);

        notifyDataSetChanged();
    }
    public void removeAll()
    {


        CustomAdapter_Images_List.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int i, final View convertView, final ViewGroup viewGroup) {



        View row = convertView;

        ViewHolder holder;

        if (convertView == null) {  //First time creation

            //Inflation , From XML to JAVA Object
            row = inflater.inflate(R.layout.single_row, null);

            holder = new ViewHolder(row);


            row.setTag(holder);




        }//end of if
        else {
            //Recycling

            holder = (ViewHolder) row.getTag();
        }


        Bitmap bitmap=null;
        try
        {
            InputStream is = assetManager.open("Images/" + CustomAdapter_Images_List.get(i).getPath());
             bitmap = BitmapFactory.decodeStream(is);
        }
        catch (IOException e)
        {

        }

        if (CustomAdapter_Images_List.get(i).IsSelected()) {
            //Here we need to make the TextView Visible

            holder.image.setImageBitmap(bitmap);
            holder.selectedTextView.setVisibility(TextView.VISIBLE);
        } else {
            holder.image.setImageBitmap(bitmap);
            holder.selectedTextView.setVisibility(TextView.INVISIBLE);
        }



        return row;

    }

}

