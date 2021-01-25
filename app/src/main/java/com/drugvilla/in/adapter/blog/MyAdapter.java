package com.drugvilla.in.adapter.blog;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.drugvilla.in.listener.FragmentSelectionListener;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    private int totalTabs;
    private FragmentSelectionListener listener;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs,FragmentSelectionListener listener) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.listener = listener;
    }

    // this is for fragment tabs
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listener.onClick(position);
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}