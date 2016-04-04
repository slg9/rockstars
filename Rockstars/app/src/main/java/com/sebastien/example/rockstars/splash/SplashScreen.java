package com.sebastien.example.rockstars.splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.activity.MainActivity;
import com.sebastien.example.rockstars.utils.Utils;

import org.json.JSONObject;

/**
 * Created by Sebastien on 01/04/2016.
 */
public class SplashScreen extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    //private ProgressDialog progress_dialog;
    private ProgressBar progress_bar;
    private TextView loading;
    private Context ctx = this;
    private String Web_Server_Url ="http://54.72.181.8/yolo/";
    private String Web_Server_Json ="contacts.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        progress_bar = (ProgressBar)findViewById(R.id.progressBar);
        loading = (TextView)findViewById(R.id.TXT_loading);

        new get_jsonTask().execute();
    }
    class get_jsonTask extends AsyncTask<String,String,JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject current_jobject = Utils.readFileFromSD("data.json");
            JSONObject jobj = Utils.get_json_from_server(Web_Server_Url, Web_Server_Url + Web_Server_Json, current_jobject);
            return jobj;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
            if(jobj!=null) {
                //We create a Json File from the Json Array got from server
                Utils.writeJSONFileToSD(ctx, jobj, "data");
                progress_bar.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }else{
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progress_bar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }


        }
    }
}
