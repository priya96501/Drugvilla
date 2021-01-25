package com.drugvilla.in.ui.blog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.MyAdapter;
import com.drugvilla.in.databinding.ActivityAllBlogsBinding;
import com.drugvilla.in.listener.FragmentSelectionListener;
import com.drugvilla.in.ui.blog.fragment.AllArticleFragment;
import com.drugvilla.in.ui.blog.fragment.CategoryFragment;
import com.drugvilla.in.ui.blog.fragment.FavouriteFragment;
import com.google.android.material.tabs.TabLayout;

public class AllBlogs extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityAllBlogsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_blogs);
        context = AllBlogs.this;
        setToolbar();
        setTabLayout();
    }
    private void setTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Categories"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Favourites"));
        MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), binding.tabLayout.getTabCount(), new FragmentSelectionListener() {
            @Override
            public Fragment onClick(int position) {
                switch (position) {
                    case 0:
                        return new CategoryFragment();
                    case 1:
                        return new AllArticleFragment();
                    case 2:
                        return new FavouriteFragment();
                    default:
                        return null;
                }
            }
        });
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition()); }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.all_blogs));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        // binding.wbBlogs.loadUrl(AppConstant.BLOG_URL);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }
}