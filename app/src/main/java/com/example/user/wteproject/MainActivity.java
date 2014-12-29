package com.example.user.wteproject;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.*;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

import domain.Information;
import domain.User;
import http.HttpDelegate;


public class MainActivity extends ActionBarActivity {
    TextView t;
    public final HttpDelegate delegate = new HttpDelegate();
    public final String BASE_URL = "http://trim-mix-807.appspot.com";
    public Information info ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());

        Button btn;
        btn = (Button) findViewById(R.id.Login);
        t = (TextView) findViewById(R.id.textView);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    boolean res = userLogin("root");
                    /*int num = info.user.hasRate;
                    String str;
                    str = Integer.toString(num);*/
                    t.setText(Boolean.toString(res));
                } catch (IOException | URISyntaxException | JSONException e) {
                    e.printStackTrace();
                }
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
    private boolean userLogin(String id) throws IOException, URISyntaxException, JSONException {
        Gson gson = new Gson();
        User user = new User();
        user.setId(id);
        String jsonString = gson.toJson(user);
        String result = delegate.doPost(BASE_URL+"/login" , jsonString);
        if(result!=null){
            info = gson.fromJson(result,Information.class);
            Intent intent = new Intent(this,MainActivity2.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
