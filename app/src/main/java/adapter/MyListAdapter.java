package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.wteproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by user on 2015/1/17.
 */
public class MyListAdapter extends BaseAdapter{

    private ArrayList<HashMap<String,Object>> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int[] viewId;
    private String[] keyString;

    private ItemView itemView ;

    private class ItemView{
        TextView itemName;
        TextView itemAdr;
        RatingBar itemRating;
    }

    public MyListAdapter(Context c,ArrayList<HashMap<String,Object>> list ,int resource,String[] from ,int[] to){
        mContext = c;
        mList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        viewId = new int[to.length];
        System.arraycopy(from,0,keyString,0,from.length);
        System.arraycopy(to,0,viewId,0,to.length);
    }
    @Override
    public int getCount() {
        //return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return 0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        if(convertView != null){
            itemView = (ItemView) convertView.getTag();
        }
        else{
            convertView = mInflater.inflate(R.layout.list_rest,null);
            itemView = new ItemView();
            itemView.itemName = (TextView) convertView.findViewById(viewId[0]);
            itemView.itemAdr = (TextView) convertView.findViewById(viewId[1]);
            itemView.itemRating = (RatingBar) convertView.findViewById(viewId[2]);
            convertView.setTag(itemView);
        }
        HashMap<String,Object> appInfo = mList.get(position);
        if (appInfo!=null){
            String name = (String) appInfo.get(keyString[0]);
            String adr = (String) appInfo.get(keyString[1]);
            itemView.itemName.setText(name);
            itemView.itemAdr.setText(adr);
            itemView.itemRating.setIsIndicator(true);
            //itemView.itemRating.setOnRatingBarChangeListener(new ItemRating_Click(position));
        }

        return convertView;
    }
    private class ItemRating_Click implements RatingBar.OnRatingBarChangeListener{
        private int position;
        public ItemRating_Click(int position){
            this.position = position;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingBar.setRating(rating);
            Log.d("Rating_bar",String.valueOf(position));
        }
    }
}
