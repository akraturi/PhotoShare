package com.parse.starter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private List mArrayList;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setTitle("Users List");

        mListView=(ListView) findViewById(R.id.listview);

        mArrayList=new ArrayList<String>();
       /* mArrayList.add("Sample user1");
        mArrayList.add("Sample user2");
        mArrayList.add("Sample usser3");*/
        final ArrayAdapter mArrayAdapter=new ArrayAdapter(UserActivity.this,android.R.layout.simple_list_item_1,mArrayList);
        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserActivity.this,UserFeedActivity.class);
                intent.putExtra("username",ParseUser.getCurrentUser().getUsername());
                startActivity(intent);
            }
        });



        //fectching the users from the server except the logged in user
        ParseQuery<ParseUser> users= ParseUser.getQuery();
        users.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        users.whereEqualTo("app","insta");
        users.addAscendingOrder("username");
        users.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0) {
                    Log.i("users:",objects.toString());
                    for (ParseUser user : objects) {
                        mArrayList.add(user.getUsername());
                    }
                    Log.i("Sucess:", "fetched sucessfully");
                    //mListView.setAdapter(mArrayAdapter);
                    mArrayAdapter.notifyDataSetChanged();
                }
                else
                {
                    e.printStackTrace();
                }
            }

        });
        /*if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {

        }*/
       // if(checkSelfPermission())
        //getPhoto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_photo)
        {
            getPhoto();
        }
        else if(item.getItemId()==R.id.log_out)
        {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    finish();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
    public void getPhoto()
    {
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if(grantResults.length>0 && permissions[0].equals(PackageManager.PERMISSION_GRANTED)){
                getPhoto();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get the data from the activity result
        Uri uri=data.getData();
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            try{
                Bitmap bitmap =MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                uploadImage(bitmap);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);

    }
    // Uploading the image to the parse server
    public void uploadImage(Bitmap bitmap)
    {   //create a new byte array output stream
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        //compress the bitmap into the correct picture format
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        // get the byte array of corresponding image
        byte bytearr[]=stream.toByteArray();
        //create a new parse file
        ParseFile parseFile= new ParseFile("anand",bytearr);
        //create a new parse object and put the image in it
        ParseObject object=new ParseObject("Image");
        //put proper details of the user
        object.put("username",ParseUser.getCurrentUser().getUsername());
        object.put("picture",parseFile);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String msg="";
                if(e==null)
                {
                    msg="Photo sucessfully uploaded to the server";
                }
                else
                {
                    msg="Failed to upload the photo to the server";
                }
                Toast.makeText(UserActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
