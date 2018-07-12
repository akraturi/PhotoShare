package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.vision.text.Line;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        setTitle("Users feed");

        // Programatically adding views to the activity

        mLinearLayout=(LinearLayout) findViewById(R.id.linearlayout);
        mImageView=new ImageView(UserFeedActivity.this);

        downloadImage();


    }
    public void downloadImage()
    {
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Image");
        query.whereMatches("username",getIntent().getStringExtra("username"));
        Log.i("Logged user:",getIntent().getStringExtra("username"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.i("downloaded objects:",objects.toString());
                if(e==null&&objects.size()>0)
                {
                    for(ParseObject object:objects)
                    {
                        ParseFile parseFile=(ParseFile) object.get("picture");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data!=null)
                                {   Log.i("Recieved Bitmap:",data.toString());
                                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                    mImageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                    ));

                                    mImageView.setImageBitmap(bitmap);

                                    mLinearLayout.addView(mImageView);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
