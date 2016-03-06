package com.example.user.wteproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import domain.Information;
import service.SysApplication;


public class MainActivity extends ActionBarActivity  {

    private Fragment fg;
    FragmentPagerAdapter adapterViewPager;
    public static Information info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_action_bar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        info = (Information) getIntent().getExtras().getSerializable("info");


        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),info);
        vpPager.setAdapter(adapterViewPager);

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                fg = adapterViewPager.getItem(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
            public void onFragmentInteraction(Uri uri){
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
        if(id == R.id.action_add){
            Intent intent = new Intent(this,newResActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("info",info);
            intent.putExtras(bundle);
            startActivity(intent);
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        View view = findViewById(R.id.page_bg);
        view.setBackgroundResource(0);
        System.gc();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent keyEvent){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).setTitle("Where to Eat").setMessage("確定要離開嗎?")
                    .setCancelable(false).setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing;
                }
            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SysApplication.getInstance().exit();
                }
            }).show();

        }
        return super.onKeyDown(keyCode,keyEvent);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private Information info;

        public MyPagerAdapter(FragmentManager fragmentManager , Information info) {
            super(fragmentManager);
            this.info = info;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position){
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ResListFragment.newInstance("List", 0 ,info);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return DecisionFragment.newInstance("Decision", 1 ,info);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Restaurant List";
                case 1:
                    return "Decision";
                default:
                    return null;
            }
        }

    }

}
