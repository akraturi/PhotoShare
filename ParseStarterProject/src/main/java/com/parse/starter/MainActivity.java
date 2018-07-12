/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

  private EditText mUsernameEditText,mPasswordEditText;
  private Button mGetInButton;
  private TextView mGetInTextView;
  private RelativeLayout mRelativeLayout;
  private boolean mSignUpModeActive=true;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

    mUsernameEditText=(EditText)findViewById(R.id.editText);
    mPasswordEditText=(EditText)findViewById(R.id.editText2);

    mGetInButton=(Button)findViewById(R.id.button);
    mGetInTextView=(TextView)findViewById(R.id.textView);

    mRelativeLayout=(RelativeLayout) findViewById(R.id.relative_layout);

    mGetInButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         if(mSignUpModeActive) {
             signUP();
         }
         else
         {
             signIn();

         }

        }
    });
    mGetInTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSignUpModeActive)
            {   mSignUpModeActive=false;
                mGetInButton.setText("Login");
                mGetInTextView.setText("or Sign up");
            }
            else
            {
                mSignUpModeActive=true;
                mGetInButton.setText("Signup");
                mGetInTextView.setText("or Login");
            }
        }
    });

    mPasswordEditText.setOnKeyListener(this);

    mRelativeLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    });
    if(ParseUser.getCurrentUser()!=null)
    {
        showUsers();
    }


    // Demo code
    //create new parse object named Score will be saved to the cloud server
   /* ParseObject score=new ParseObject("Score");
    //put something into the parse object
    score.put("username","Amit");
    score.put("score",43);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null)
        {
          Log.i("sucess:","Data was saved to the cloud");
        }
        else
        {
          e.printStackTrace();
        }
      }
    });*/
   // getting data back from the server
    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.getFirstInBackground(new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e==null&&object!=null) {
          Log.i("grabbing success:", "We done it");
          Log.i("username:",object.getString("username"));
          Log.i("score",Integer.toString(object.getInt("score")));
          //updating the object values
          object.put("username","Amitagain");
          object.put("score",95);
          Log.i("saving...","We updated the object values");
          object.saveInBackground();
        }
      }
    });*/

    // Test saving some values on the server than grabing them and updating them

    /*ParseObject object=new ParseObject("Tweet");
    object.put("username","Amit");
    object.put("tweet","Hi there! i am doing fine");
    object.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null)
        {
          Log.i("saved:","saved to server sucessfully");
        }
      }
    });
    object.fetchInBackground(new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        Log.i("fetched:","Object fetched sucessfully");
        Log.i("username:",object.getString("username"));
        Log.i("tweet",object.getString("tweet"));
        Log.i("update::","updating the values....");
        object.put("username","Amit2");
        object.put("tweet","Hi again");
        Log.i("new column::","New column created");
        object.put("mood","hahahahaha");
        object.saveInBackground();
      }
    });*/

    /*ParseQuery<ParseObject> query= ParseQuery.getQuery("Student");
    query.whereMatches("name","amit").whereStartsWith("rn","BT");
    query.getFirstInBackground(new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e==null&&object!=null)
        {
          Log.i("sucess:","fetched sucessfully");
          Log.i("R.n of fetched amit:",object.getString("rn"));
          Log.i("updating","updating r.n of amit from nit uttarakhand/////,,,....");
          object.put("rn","7654");
          object.saveInBackground();

        }
        else
        {
          e.printStackTrace();
        }
      }
    });*/

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
  public void signUP() {

      if(mUsernameEditText.getText().toString().equals(""))
      {
          mUsernameEditText.setError("Username is required");
      }
      if(mPasswordEditText.getText().toString().equals(""))
      {
          mPasswordEditText.setError("Password is required");
      }
      else
      {
          ParseUser parseUser=new ParseUser();
          parseUser.setUsername(mUsernameEditText.getText().toString());
          parseUser.setPassword(mPasswordEditText.getText().toString());
          parseUser.put("app","insta");
          parseUser.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if(e==null)
                  {
                      Toast.makeText(MainActivity.this,"Sign up sucessfull!",Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                      Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                  }
              }
          });
      }

  }
  public void signIn()
  {
     ParseUser.logInInBackground(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), new LogInCallback() {
         @Override
         public void done(ParseUser user, ParseException e) {
             if(e==null&&user!=null)
             {
                 Toast.makeText(MainActivity.this,"Login sucess!",Toast.LENGTH_SHORT).show();
                 showUsers();
             }
             else
             {
                 Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
             }

         }
     });

  }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

       if(keyCode==event.KEYCODE_ENTER&& event.getAction()== event.ACTION_DOWN)
       {
           if(mSignUpModeActive)
           {
               signUP();
           }
           else
           {
               signIn();
               showUsers();
           }
       }
        return false;
    }
    public void showUsers()
    {
        Intent intent=new Intent(MainActivity.this,UserActivity.class);
        startActivity(intent);
    }
}