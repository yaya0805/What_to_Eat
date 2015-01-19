package com.example.user.wteproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;

import domain.GeoResult;
import domain.Information;
import domain.Restaurant;
import http.HttpDelegate;


public class newResActivity extends ActionBarActivity {
    public final String BASE_URL = "http://trim-mix-807.appspot.com";

    private Information info;

    private Button addNewResBtn;
    private EditText adrText;
    private EditText nameText;
    private EditText phoneText;
    private CheckBox breakfastcheck;
    private CheckBox lunchCheck;
    private CheckBox dinnerCheck;
    private CheckBox night_snackCheck;
    private CheckBox riceCheck;
    private CheckBox noodleCheck;
    private CheckBox otherCheck;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_res);

        info = (Information) getIntent().getExtras().getSerializable("info");

        addNewResBtn = (Button) findViewById(R.id.addNewResBtn);
        adrText = (EditText) findViewById(R.id.adrText);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        breakfastcheck = (CheckBox) findViewById(R.id.breakfastcheck);
        lunchCheck = (CheckBox) findViewById(R.id.lunchcheck);
        dinnerCheck = (CheckBox) findViewById(R.id.dinnercheck);
        night_snackCheck = (CheckBox) findViewById(R.id.night_snackcheck);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        riceCheck = (CheckBox) findViewById(R.id.riceCheck);
        noodleCheck = (CheckBox) findViewById(R.id.noodleCheck);
        otherCheck = (CheckBox) findViewById(R.id.otherCheck);

        addNewResBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = nameText.getText().toString();
                String address = adrText.getText().toString();
                String phone = phoneText.getText().toString();
                final String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+address+"&sensor=false&language=zh-TW";

                // name ,id ,address ,phone
                Restaurant res = new Restaurant(name,-1,address,phone);

                res.setType_breakfast(breakfastcheck.isChecked());
                res.setType_dinner(dinnerCheck.isChecked());
                res.setType_lunch(lunchCheck.isChecked());
                res.setType_night_snack(night_snackCheck.isChecked());
                res.setKind_noodle(noodleCheck.isChecked());
                res.setKind_other(otherCheck.isChecked());
                res.setKind_rice(riceCheck.isChecked());

                if(info.isResExisting(res.getAddress())==false && !address.contains("#")
                        && !address.contains("%")) {
                    progressBar.setVisibility(View.VISIBLE);
                    postRes(url, res);
                }
                else if(address.contains("#") || address.contains("%")){
                    Toast.makeText(newResActivity.this, "不合法地址,請重新確認", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(newResActivity.this, "該餐廳已經存在", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_res, menu);
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

    private void postRes(final String url,final Restaurant res){
        new AsyncTask<Void,Void,Restaurant>() {
            @Override
            protected Restaurant doInBackground(Void... params) {
                HttpDelegate delegate = new HttpDelegate();
                Gson gson = new Gson();
                try {
                    String geoString = delegate.doGet(url);
                    GeoResult geoResult = gson.fromJson(geoString,GeoResult.class);
                    if(geoResult.getStatus().compareTo("OK")==0) {
                        res.setAddress(geoResult.getResults().get(0).getFormatted_address());
                        res.setLat(geoResult.getResults().get(0).getGeometry().getLocation().getLat());
                        res.setLng(geoResult.getResults().get(0).getGeometry().getLocation().getLng());
                        String jsonString = gson.toJson(res);
                        Log.d("msg",jsonString);
                        String result = delegate.doPost(BASE_URL+"/restaurants" , jsonString);
                        Restaurant newRes = gson.fromJson(result,Restaurant.class);
                        Log.d("result",result);
                        return newRes;
                    }
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(Restaurant result){
                if(result!=null) {
                    if(info.isResExisting(res.getAddress())){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(newResActivity.this, "該餐廳已經存在", Toast.LENGTH_LONG).show();
                    }
                    else {
                        info.saveRes(result);
                        Intent intent = new Intent(newResActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", info);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        newResActivity.this.finish();
                    }
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(newResActivity.this,"找不到該地址,請換一種形式輸入",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null,null,null);
    }
}
