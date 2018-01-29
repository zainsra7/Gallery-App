package com.l134046zain.assingment1;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ImagesShow extends AppCompatActivity {

    String TAG = "LifeCycle";

    GridView gridView;
    ArrayList<String> Images; //Container to store all the images

    ArrayList<String> SelectedImages; //Container to store Those Images which are selected by the user


    public final static String EXTRA_MESSAGE = "com.l134046zain.assingment1.Images";

    boolean ImagewasLongClicked = false;


    ArrayList<Images> Images_list;
    ArrayList<Images> Reference_List;


    private static final int MY_PERMISSIONS_REQUEST_Set_Wallpaper = 244;


    boolean OptionMenu2 = false;



    ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_show);


        //Initializing Images Data
        AssetManager assetManager = getAssets();

        Images_list=new ArrayList<>();

        //Transferring Images from Assets Folder to ArrayList which is going to be fed to the Custom Adapter
        try {
            String[] temp = assetManager.list("Images");

            for (int x = 0; x < temp.length; x++) {

                Images im = new Images(temp[x]);
                Images_list.add(im);

            }


        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        //Setting GridView and Custom Adapter

        gridView = (GridView) findViewById(R.id.gridView);
         CustomImageAdapter customAdapter = new CustomImageAdapter(ImagesShow.this, Images_list);
        gridView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();



        //Setting Toolbar


        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_toolbar);

        myChildToolbar.setTitle("");
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button (back button)
        ab.setDisplayHomeAsUpEnabled(true);


        ab.setTitle("ImagesShow");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Images_list.get(i).ToggleSelection();
//                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();


                if(ImagewasLongClicked==false)
                {
                    //check whether the Image which is clicked is Already Selected (Unselect it then)
                    //or it's a new one in this case Select it. For cancelling the selection need to add a cancel button
                    //on action bar.


                    String ImagePath = Images_list.get(i).getPath();

                    Intent intent = new Intent(getApplicationContext(), SingleImageShow.class);

                    intent.putExtra(EXTRA_MESSAGE, ImagePath);

                    startActivity(intent);
                }
                else
                {
                   // Already Pressed LongClick so need to add Selections

                    // Add new Items which are selected into the Array list
                    //If User selects the previously selected Image again then deselect that image

                    Images_list.get(i).ToggleSelection();
                    ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                    ab.setSubtitle(CountSelectedImages()+ "/" + Images_list.size()+" items Selected");

                }

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImagewasLongClicked=true;
                    Images_list.get(i).ToggleSelection();
                    ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                    ab.setSubtitle(CountSelectedImages()+ "/" + Images_list.size()+" items Selected");
                    return true;
            }
        });

    }



    public int CountSelectedImages()
    {

        int count=0;
        for(int x=0;x<Images_list.size();x++)
        {
            if(Images_list.get(x).IsSelected())
                count++;
        }

        return count;

    }
    public void ClearWholeSelection()
    {
        for(int x=0;x<Images_list.size();x++)
        {
            if(Images_list.get(x).IsSelected()) {
                Images_list.get(x).ToggleSelection();
                ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
                ab.setSubtitle(CountSelectedImages()+ "/" + Images_list.size()+" items Selected");
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                    Log.d(TAG, "onRequestPermissionsResult Accepted: ");

                    String Path="";

                    for(int x=0;x<Images_list.size();x++)
                    {
                        if(Images_list.get(x).IsSelected())
                        {
                            Path=Images_list.get(x).getPath();
                            break;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult Path: "+Path);
                    ClearWholeSelection();

                    ab.setSubtitle(CountSelectedImages() + "/" + Images_list.size() + " items Selected");

                    WallpaperManager myWallpaperManager
                            = WallpaperManager.getInstance(getApplicationContext());
                    try {

                        AssetManager assetManager = getAssets();
                        InputStream is = assetManager.open("Images/" + Path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        myWallpaperManager.setBitmap(bitmap);

                        String temp="Permission Granted and Wallpaper has been Changed !";
                        DisplayDialogBox(temp);


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult Denied: ");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.actions, menu);

//        if(OptionMenu2==true) {
//            menu.getItem(0).setVisible(true);
//        }
        //^ for cancel button


        return true;
    }



    public void DisplayDialogBox(String text)
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(text);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())

        {

            case R.id.deleteImages:

                if (CountSelectedImages() > 0)
                {


                    if(CountSelectedImages()==Images_list.size())
                    {

                        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                        alertDialogBuilder1.setMessage("Are you sure you want to Delete All Images?");

                        //User has selected all the images , so delete all of them


                        alertDialogBuilder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ((CustomImageAdapter) gridView.getAdapter()).removeAll();

                                ab.setSubtitle(CountSelectedImages() + "/" + Images_list.size() + " items Selected");
                            }});

                        alertDialogBuilder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder1.create();
                        alertDialog.show();

                        return true;

                    }



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Are you sure you want to Delete ?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            //Need to make a Dialog Box too for confirmation


                            for (int x = 0,i=0; x < Images_list.size(); x++) {

                                if (Images_list.get(x).IsSelected())
                                {

                                    Images_list.remove(x);

                                    ab.setSubtitle(CountSelectedImages() + "/" + Images_list.size() + " items Selected");

                                }

                            }
                            ((CustomImageAdapter) gridView.getAdapter()).notifyDataSetChanged();
                            ClearWholeSelection();

                            if(Images_list.isEmpty())
                            {
                                String text="No more Images to Show ! Click Ok to Return to Main Screen ";
                                AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(ImagesShow.this);
                                alertDialogBuilderr.setMessage(text);

                                alertDialogBuilderr.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilderr.create();
                                alertDialog.show();

                            }




                        }
                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            ClearWholeSelection();

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();





                }
                else
                {
                    String text="Please Select an Image to Delete ! ";
                    DisplayDialogBox(text);
                    ImagewasLongClicked=false;
                }

                return true;


            case R.id.setWallpaper:

                if(CountSelectedImages()==1) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Are you sure you want to Set the Selected Image as Wallpaper ?");


                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    //Requesting Permission as Setting Wallper is a Normal_Permission so it won't be revoked
                                    //and is granted automatically
                                    ActivityCompat.requestPermissions(ImagesShow.this,
                                            new String[]{Manifest.permission.SET_WALLPAPER},
                                            1);
                                }
                            });


                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
        }
        else
        {
        ClearWholeSelection();

        String text="Please Select at least and only 1 Image to set as an Wallpaper ! ";
        DisplayDialogBox(text);
        ImagewasLongClicked=false;
        }

                return true;

            default:
                return super.onOptionsItemSelected(item);


        }//end of switch
        }

    }
