package com.example.user.wteproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import domain.Information;


public class MainActivity extends ActionBarActivity {

    private Button addNewResBtn;
    private Information info ;
    private ListAdapter listAdapter ;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewResBtn = (Button) findViewById(R.id.addNewResBtn);
        info = (Information) getIntent().getSerializableExtra("info");
        listView = (ListView) findViewById(R.id.resListView);

        List nameList = new ArrayList<String>();
        List adrList = new ArrayList<String>();
        for (int i=0;i<info.resList.size();i++){
            // test
            Toast.makeText(MainActivity.this,info.resList.get(i).getName(),Toast.LENGTH_LONG).show();
            nameList.add(info.resList.get(i).getName());
            adrList.add(info.resList.get(i).getAddress());
        }
        listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(listAdapter);

        addNewResBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,info.user.getId(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
}
