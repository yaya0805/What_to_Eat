package com.example.user.wteproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

    // TODO: Rename and change types of parameters
    private String title;
    private int page;

    private Spinner waySpinner;
    private Spinner timeSpinner;

    private ArrayAdapter wayAdapter;
    private ArrayAdapter timeAdapter;

    private Button decideBtn ;

    private TextView longView ;
    private TextView latView;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param page Parameter 2.
     * @return A new instance of fragment DecisionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DecisionFragment newInstance(String title, int page) {
        DecisionFragment fragment = new DecisionFragment();
        Bundle args = new Bundle();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_decision, container, false);


        String[] ways = {"步行" ,"機車" ,"汽車"};
        String[] times = {"10分內","20分內","30分內","40分內"};
        final decisionModel model = new decisionModel();

        waySpinner = (Spinner) view.findViewById(R.id.waySpinner);
        timeSpinner = (Spinner) view.findViewById(R.id.arriveTimeSpinner);
        wayAdapter = new ArrayAdapter(getActivity(),R.layout.myspinner,ways);
        waySpinner.setAdapter(wayAdapter);
        timeAdapter = new ArrayAdapter(getActivity(),R.layout.myspinner,times);
        timeSpinner.setAdapter(timeAdapter);


        longView = (TextView) view.findViewById(R.id.longView);
        latView = (TextView) view.findViewById(R.id.latView);

        decideBtn = (Button) view.findViewById(R.id.decideBtn);
        decideBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("Button","has been pressed");
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    new AlertDialog.Builder(getActivity()).setTitle("抵達時間功能").setMessage("此功能需要GPS,您尚未開啟GPS,要前往設定畫面嗎?")
                            .setCancelable(false).setPositiveButton("Y",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("N",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(),"抵達時間功能將無法使用",Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                }
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Double longitude = location.getLongitude() * 1000000;
                            Double latitude = location.getLatitude() * 1000000;
                            Log.d("Location=", "X=" + longitude.intValue() + ", Y=" + latitude.intValue());
                            model.setLatitude(latitude);
                            model.setLongitude(longitude);
                            longView.setText("經度" + longitude);
                            latView.setText("緯度" + latitude);
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        });

        // check if gps has been launched.


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
        private Double longitude;
        private Double latitude;
        public void setLongitude(Double longitude){
            this.longitude = longitude;
        }
        public void setLatitude(Double latitude){
            this.latitude = latitude;
        }
    }

}
