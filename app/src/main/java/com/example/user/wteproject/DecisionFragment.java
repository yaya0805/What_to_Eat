package com.example.user.wteproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import domain.Information;
import domain.Restaurant;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DecisionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DecisionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DecisionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_PAGE = "page";

    // speed in m/min
    private static final double AVG_SPEED = 4500/60;

    //Information
    private Information info;

    // TODO: Rename and change types of parameters
    private String title;
    private int page;

    private Spinner waySpinner;
    private Spinner timeSpinner;
    private Spinner ratingSpinner;

    private ArrayAdapter wayAdapter;
    private ArrayAdapter timeAdapter;
    private ArrayAdapter ratingAdapter;

    private Button decideBtn ;


    private CheckBox breakfastCheck;
    private CheckBox lunchCheck;
    private CheckBox dinnerCheck;
    private CheckBox night_snackCheck;
    private CheckBox riceCheck;
    private CheckBox noodleCheck;
    private CheckBox otherCheck;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private OnFragmentInteractionListener mListener;

    private PopupWindow mPopupWindow;

    private ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param page Parameter 2.
     * @return A new instance of fragment DecisionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DecisionFragment newInstance(String title, int page ,Information info) {
        DecisionFragment fragment = new DecisionFragment();
        Bundle args = new Bundle();
        args.putSerializable("info",info);
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public DecisionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE);
            title = getArguments().getString(ARG_TITLE);
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            info = MainActivity.info;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_decision, container, false);


        String[] ways = {"步行" ,"機車" ,"汽車"};
        String[] times = {"10分內","20分內","30分內","40分內"};
        String[] ratings = {"不限","1星以上","2星以上","3星以上","4星以上","5星以上"};
        final decisionModel model = new decisionModel();

        breakfastCheck = (CheckBox) view.findViewById(R.id.breakfastCheckin_decision);
        lunchCheck = (CheckBox) view.findViewById(R.id.lunchCheckin_decision);
        dinnerCheck = (CheckBox) view.findViewById(R.id.dinnerCheckin_decision);
        night_snackCheck = (CheckBox) view.findViewById(R.id.night_snackCheckin_decision);
        riceCheck = (CheckBox) view.findViewById(R.id.riceCheckin_decision);
        noodleCheck = (CheckBox) view.findViewById(R.id.noodleCheckin_decision);
        otherCheck = (CheckBox) view.findViewById(R.id.otherCheckin_decision);

        //waySpinner = (Spinner) view.findViewById(R.id.waySpinner);
        timeSpinner = (Spinner) view.findViewById(R.id.arriveTimeSpinner);
        //wayAdapter = new ArrayAdapter(getActivity(),R.layout.myspinner,ways);
        //waySpinner.setAdapter(wayAdapter);
        timeAdapter = new ArrayAdapter(getActivity(),R.layout.myspinner,times);
        timeSpinner.setAdapter(timeAdapter);
        ratingSpinner = (Spinner) view.findViewById(R.id.ratingspinner);
        ratingAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,ratings);
        ratingSpinner.setAdapter(ratingAdapter);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar3);



        decideBtn = (Button) view.findViewById(R.id.decideBtn);
        decideBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("Button","has been pressed");
                progressBar.setVisibility(View.VISIBLE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    new AlertDialog.Builder(getActivity()).setTitle("抵達時間功能").setMessage("此功能需要GPS,您尚未開啟GPS,要前往設定畫面嗎?")
                    .setPositiveButton("N",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(),"抵達時間功能將無法使用",Toast.LENGTH_SHORT).show();
                            model.timeAsked = 1e9;
                            model.latitude = 0.0;
                            model.longitude = 0.0;
                            Restaurant result = decide(model);
                            if(result!=null) {
                                progressBar.setVisibility(View.INVISIBLE);
                                showResult(result, view);
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(),"Sorry,沒有符合您條件的餐廳",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setCancelable(false).setNegativeButton("Y",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),0);

                        }
                    }).show();
                }
                else{
                    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Double longitude = location.getLongitude() /** 100000000*/;
                                Double latitude = location.getLatitude() /** 10000000*/;
                                Log.d("Location=", "X=" + longitude.intValue() + ", Y=" + latitude.intValue());
                                model.setLatitude(latitude);
                                model.setLongitude(longitude);
                                locationManager.removeUpdates(locationListener);

                                Restaurant result = decide(model);
                                if(result!=null) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    showResult(result, view);
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(),"Sorry,沒有符合您條件的餐廳",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        };
                        Criteria criteria = new Criteria();  //資訊提供者選取標準
                        String bestProvider;
                        bestProvider = locationManager.getBestProvider(criteria,true);    //選擇精準度最高的提供者
                        locationManager.requestLocationUpdates(bestProvider, 0, 1000, locationListener);

                    }
                    /*else{
                        model.timeAsked = 1e9;
                        model.latitude = 0.0;
                        model.longitude = 0.0;
                        Restaurant result = decide(model);
                        if(result!=null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            showResult(result, view);
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"Sorry,沒有符合您條件的餐廳",Toast.LENGTH_LONG).show();
                        }
                    }*/
                }

            }
        });

        // spinner listener
        timeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.timeAsked = (double)(position+1)*10;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratingSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.ratingAsked = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class decisionModel{
        public Double longitude;
        public Double latitude;
        public double timeAsked;
        public double ratingAsked;
        public void setLongitude(Double longitude){
            this.longitude = longitude;
        }
        public void setLatitude(Double latitude){
            this.latitude = latitude;
        }

    }

    private Restaurant decide(decisionModel model){


        List<Restaurant> list = new ArrayList<Restaurant>();
        List<Restaurant> resultList = new ArrayList<Restaurant>();
        Restaurant result = null;
        list = info.getResList();
        for (int i=0;i<list.size();i++){
            Restaurant tmp = list.get(i);
            Log.d("res_name in decide",tmp.getName()+" "+String.valueOf(tmp.getType_breakfast())+String.valueOf(breakfastCheck.isChecked()));
            if((tmp.getType_breakfast() == breakfastCheck.isChecked() && breakfastCheck.isChecked())
                || (tmp.getType_lunch() == lunchCheck.isChecked() && lunchCheck.isChecked())
                    || (tmp.getType_dinner() == dinnerCheck.isChecked() && dinnerCheck.isChecked())
                        || (tmp.getType_night_snack() == night_snackCheck.isChecked() && night_snackCheck.isChecked())
                    ||(tmp.getKind_rice() == riceCheck.isChecked() && riceCheck.isChecked())
                    ||(tmp.getKind_noodle() == noodleCheck.isChecked() && noodleCheck.isChecked())
                    ||(tmp.getKind_other() == otherCheck.isChecked() && otherCheck.isChecked())){
                double distance = tmp.getDistance(model.latitude,model.longitude)*1000;
                Log.d("distance",String.valueOf(distance)+" timeAsked:"+String.valueOf(model.timeAsked));
                Log.d("time needed",String.valueOf(distance/AVG_SPEED)+" "+String.valueOf(model.ratingAsked));
                if(distance/AVG_SPEED < model.timeAsked && tmp.getRate()>=model.ratingAsked) resultList.add(tmp);
            }
        }
        int random;
        if (resultList.size()>=1) {
            random = (int) (Math.random() * resultList.size());
            result = resultList.get(random);
        }

        return  result;
    }


    private void showResult(Restaurant res,View view){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View popupView = inflater.inflate(R.layout.popup_win_decide, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,1000,1000);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        TextView titleView = (TextView) popupView.findViewById(R.id.titleView);
        TextView adrView = (TextView) popupView.findViewById(R.id.textView8);

        titleView.setText(res.getName());
        adrView.setText(res.getAddress());
        

        CheckBox breakfastCheck = (CheckBox) popupView.findViewById(R.id.checkBox);
        CheckBox lunchCheck = (CheckBox) popupView.findViewById(R.id.checkBox2);
        CheckBox dinnerCheck = (CheckBox) popupView.findViewById(R.id.checkBox3);
        CheckBox night_snackCheck = (CheckBox) popupView.findViewById(R.id.checkBox4);
        CheckBox riceCheck = (CheckBox) popupView.findViewById(R.id.checkBox5);
        CheckBox noodleCheck = (CheckBox) popupView.findViewById(R.id.checkBox6);
        CheckBox otherCheck = (CheckBox) popupView.findViewById(R.id.checkBox7);

        RatingBar ratingBar = (RatingBar) popupView.findViewById(R.id.ratingBar2);

        breakfastCheck.setChecked(res.getType_breakfast());
        breakfastCheck.setEnabled(false);
        lunchCheck.setChecked(res.getType_lunch());
        lunchCheck.setEnabled(false);
        dinnerCheck.setChecked(res.getType_dinner());
        dinnerCheck.setEnabled(false);
        night_snackCheck.setChecked(res.getType_night_snack());
        night_snackCheck.setEnabled(false);
        riceCheck.setChecked(res.getKind_rice());
        riceCheck.setEnabled(false);
        noodleCheck.setChecked(res.getKind_noodle());
        noodleCheck.setEnabled(false);
        otherCheck.setChecked(res.getKind_other());
        otherCheck.setEnabled(false);

        ratingBar.setRating(res.getRate());
        ratingBar.setIsIndicator(true);

        popupWindow.setOutsideTouchable(true);
        //popupWindow.showAtLocation(view,0,30,200);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

}
