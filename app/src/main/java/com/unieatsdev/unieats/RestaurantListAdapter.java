package com.unieatsdev.unieats;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dhruvtekchandani on 2/6/18.
 */

public class RestaurantListAdapter extends BaseAdapter{


    private Activity context;

    private List<RestaurantInformation> restaurantList;



    public RestaurantListAdapter(Activity context, List<RestaurantInformation> restaurantList ){
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        LayoutInflater inflater = myContext.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.rest_list_content,null,true);
//        TextView restaurantTextView = (TextView) listViewItem.findViewById(R.id.restaurantName);
//        TextView foodTypeTextView = (TextView) listViewItem.findViewById(R.id.foodType);
//        TextView averageCostTextView = (TextView) listViewItem.findViewById(R.id.averageCost);
//        final ImageView restaurantImageView = (ImageView) listViewItem.findViewById(R.id.restaurantImage);

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_list_content, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//      final ProgressBar pbRestList = listViewItem.findViewById(R.id.pbRestList);
        final RestaurantInformation restaurantInformation = restaurantList.get(position);
        viewHolder.restaurantTextView.setText(restaurantInformation.getRestName());
        viewHolder.foodTypeTextView.setText(restaurantInformation.getFoodAvailable());
        viewHolder.averageCostTextView.setText("Avg Cost: "+"$"+restaurantInformation.getRestAvgPrice() + " per person");
       // Picasso.with(myContext).load(restaurantInformation.getRestImageName()).networkPolicy(NetworkPolicy.OFFLINE).into(restaurantImageView);


        Picasso.with(context)
                .load(restaurantInformation.getRestImageName())
                .placeholder(R.drawable.placeholder2)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(viewHolder.restaurantImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.pbRestList.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(context)
                                .load(restaurantInformation.getRestImageName())
                                .placeholder(R.drawable.placeholder2)
                                .into(viewHolder.restaurantImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        viewHolder.pbRestList.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                });

        //Picasso.with(myContext).setIndicatorsEnabled(true);
        return convertView;
    }

    static class ViewHolder {
        TextView restaurantTextView;
        TextView foodTypeTextView;
        TextView averageCostTextView;
        ImageView restaurantImageView;
        ProgressBar pbRestList;

        ViewHolder(View v) {
            restaurantTextView = v.findViewById(R.id.restaurantName);
            foodTypeTextView = v.findViewById(R.id.foodType);
            averageCostTextView = v.findViewById(R.id.averageCost);
            restaurantImageView = v.findViewById(R.id.restaurantImage);
            pbRestList = v.findViewById(R.id.pbRestList);
        }
    }

}
