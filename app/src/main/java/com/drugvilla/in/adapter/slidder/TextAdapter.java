package com.drugvilla.in.adapter.slidder;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.viewpager.widget.PagerAdapter;

import com.drugvilla.in.R;

import com.drugvilla.in.model.Document;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class TextAdapter extends PagerAdapter {
    private final List<Document> list;
    private final LayoutInflater inflater;
    private final Context context;
    private String type;

    public TextAdapter(Context context, List<Document> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View layout = inflater.inflate(R.layout.layout_text, view, false);
        assert layout != null;
        final TextView textView = layout.findViewById(R.id.tvTitle);
        final TextView textView2 = layout.findViewById(R.id.title);
        final ImageView imageView = layout.findViewById(R.id.imageView);

        if (type.equalsIgnoreCase(AppConstant.TYPE_SLIDDER)) {
            textView.setVisibility(View.GONE);
            textView2.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView2.setText(list.get(position).getText2());
            imageView.setImageResource(list.get(position).getImage());
        } else {
            textView2.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(list.get(position).getText());
        }


        view.addView(layout, 0);
        return layout;
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
