package com.drugvilla.in.adapter.slidder;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.drugvilla.in.R;
import com.drugvilla.in.model.Document;

import com.drugvilla.in.model.dashboard.BannerData;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AndroidImageAdapter extends PagerAdapter {

    private final List<BannerData> imageModelArrayList;
    private final LayoutInflater inflater;
    private final Context context;
    private boolean value = false;

    public AndroidImageAdapter(Context context, List<BannerData> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final ImageView imageViewProduct = imageLayout.findViewById(R.id.imageProduct);
        final ProgressBar progressBar = imageLayout.findViewById(R.id.progress);

            imageView.setVisibility(View.VISIBLE);
            imageViewProduct.setVisibility(View.GONE);
            setImage(imageView, position);

        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.setZoomView(context,imageModelArrayList.get(position).getImage());
            }
        });

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    private void setImage(ImageView imageView, int position) {
        Picasso.with(context).load(imageModelArrayList.get(position).getImage())
                .error(R.color.transparent)
                .placeholder(R.color.transparent)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
