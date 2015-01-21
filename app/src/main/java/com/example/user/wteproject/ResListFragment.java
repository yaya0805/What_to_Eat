package com.example.user.wteproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MyListAdapter;
import domain.Information;
import domain.Restaurant;
import http.HttpDelegate;
import service.SysApplication;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_PAGE = "page";

    public final String BASE_URL = "http://trim-mix-807.appspot.com";

    // TODO: Rename and change types of parameters
    private String title;
    private int page;

    private OnFragmentInteractionListener mListener;
    private Information info;
    private ListView listView ;
    private ListAdapter listAdapter;


    private static PopupWindow popupWindow;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param page Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResListFragment newInstance(String title, int page ,Information info) {
        ResListFragment fragment = new ResListFragment();
        Bundle args = new Bundle();
        args.putSerializable("info",info);
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public ResListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = MainActivity.info;
            title = getArguments().getString(ARG_TITLE);
            page = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for (int i=0;i<info.getResList().size();i++){
            HashMap<String ,Object> item = new HashMap<String ,Object >();
            item.put("name", info.getResList().get(i).getName());
            item.put("address", info.getResList().get(i).getAddress());
            item.put("rate",info.getResList().get(i).getRate());
            Log.d("name", info.getResList().get(i).getName());
            Log.d("address",info.getResList().get(i).getAddress());
            Log.d("rate",String.valueOf(info.getResList().get(i).getRate()));
            list.add(item);
        }
        final String[] selcet = {"name", "address","rate"};
        final int[] ids = {R.id.text1,R.id.text2};
        listView = (ListView) view.findViewById(R.id.resListView);
        listAdapter = new MyListAdapter(getActivity(),list,R.layout.list_rest
                ,selcet,ids);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View popupView = inflater.inflate(R.layout.popup_window, null);
                popupWindow = new PopupWindow(popupView,1000,1000);

                final Restaurant currentRes = info.getResList().get(position);

                Gson gson = new Gson();
                Log.d("currentRes in res list:",gson.toJson(currentRes));

                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                TextView titleView = (TextView) popupView.findViewById(R.id.titleView);
                TextView adrView = (TextView) popupView.findViewById(R.id.textView8);

                titleView.setText(info.getResList().get(position).getName());
                adrView.setText(info.getResList().get(position).getAddress());


                CheckBox breakfastCheck = (CheckBox) popupView.findViewById(R.id.checkBox);
                CheckBox lunchCheck = (CheckBox) popupView.findViewById(R.id.checkBox3);
                CheckBox dinnerCheck = (CheckBox) popupView.findViewById(R.id.checkBox2);
                CheckBox night_snackCheck = (CheckBox) popupView.findViewById(R.id.checkBox4);
                CheckBox riceCheck = (CheckBox) popupView.findViewById(R.id.checkBox5);
                CheckBox noodleCheck = (CheckBox) popupView.findViewById(R.id.checkBox6);
                CheckBox otherCheck = (CheckBox) popupView.findViewById(R.id.checkBox7);

                RatingBar ratingBar = (RatingBar) popupView.findViewById(R.id.ratingBar2);


                breakfastCheck.setChecked(currentRes.getType_breakfast());
                breakfastCheck.setEnabled(false);
                lunchCheck.setChecked(currentRes.getType_lunch());
                lunchCheck.setEnabled(false);
                dinnerCheck.setChecked(currentRes.getType_dinner());
                dinnerCheck.setEnabled(false);
                night_snackCheck.setChecked(currentRes.getType_night_snack());
                night_snackCheck.setEnabled(false);
                riceCheck.setChecked(currentRes.getKind_rice());
                riceCheck.setEnabled(false);
                noodleCheck.setChecked(currentRes.getKind_noodle());
                noodleCheck.setEnabled(false);
                otherCheck.setChecked(currentRes.getKind_other());
                otherCheck.setEnabled(false);

                ratingBar.setRating(currentRes.getRate());
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

                    @Override
                    public void onRatingChanged(final RatingBar ratingBar, final float rating, boolean fromUser) {
                        Log.i("change", "happens");
                        if (fromUser) {
                            new AlertDialog.Builder(getActivity()).setTitle("評價功能功能").setMessage("確定要給評價:" + String.valueOf(rating) + "?")
                                    .setCancelable(false).setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing;
                                }
                            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    float newRate = info.resList.get(position).comment(rating);
                                    ratingBar.setRating(newRate);

                                    Log.d("it will ", "enter refresh");
                                    refreshRate(info.resList.get(position));
                                }
                            }).show();
                        }
                    }
                });

                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                //popupWindow.showAtLocation(view,0,30,200);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);



            }

        });

            return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
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
        //public void onFragmentInteraction(Uri uri);
    }
    public void refreshRate(final Restaurant res){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpDelegate delegate = new HttpDelegate();
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(res);
                    Log.d("this is in the refresh",jsonString);
                    String result = delegate.doPost(BASE_URL+"/restaurants" , jsonString);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null,null,null);
    }

    public static boolean dismissPopup(){
        Log.d("popupwindow:",String.valueOf(popupWindow==null));
        if(popupWindow==null) return true;
        else {
            popupWindow.dismiss();
            return false;
        }
    }


}
