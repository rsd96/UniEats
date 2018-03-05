package com.unieatsdev.unieats;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dhruvtekchandani on 2/12/18.
 */

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mMenuImageList;
    ImageAdapter(Context context, ArrayList<String> menuImageList){
        mContext = context;
        mMenuImageList = menuImageList;
    }

    @Override
    public int getCount() {
        return mMenuImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.menu_image_layout, null);


        final PhotoView photoView = frameLayout.findViewById(R.id.pv_menu);
        final ProgressBar pbar = frameLayout.findViewById(R.id.pb_menu);
        pbar.setVisibility(View.VISIBLE);
//        photoView.setScaleType(ImageView.ScaleType.CENTER);
//        photoView.setAdjustViewBounds(true);
        Picasso.with(mContext)
                .load(mMenuImageList.get(position))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        pbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(mContext)
                                .load(mMenuImageList.get(position))
                                .into(photoView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        pbar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                });
        //Picasso.with(mContext).setIndicatorsEnabled(true);
        container.addView(frameLayout, 0);
        return frameLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }
}
