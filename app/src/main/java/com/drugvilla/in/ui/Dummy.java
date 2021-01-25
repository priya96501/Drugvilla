package com.drugvilla.in.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.dummy.OuterAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.databinding.ActivityDummyBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.ui.labs.LabDashboard;
import com.drugvilla.in.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class Dummy extends AppCompatActivity {
    private List<Document> labsdata = new ArrayList<>();
    private Context context;
    private ActivityDummyBinding binding;
    // featured labs
    private final String[] heading = {"SHOP BY CATEGORY", "Popular Tests", "","Featured Labs","", "Popular Health Packages"};
    private final String[] description = {"", "Get best deals on popular Tests", "", "A true devotion to healing","", "Find right health package for you"};
    private final int[] type = {TYPE_CATEGORY,TYPE_TEST,TYPE_STATIC_VIEW, TYPE_LABS,TYPE_STATIC_VIEW, TYPE_PACKAGES};
    public static final int TYPE_CATEGORY = 0;
    public static final int TYPE_TEST = 1;
    public static final int TYPE_LABS = 2;
    public static final int TYPE_STATIC_VIEW = 3;
    public static final int TYPE_PACKAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dummy);
        context = Dummy.this;
        setToolbar();
        setAdapter();
    }
    private List<Document> PrepareDataMessage2() {
        List<Document> data4 = new ArrayList<>();
        for (int i = 0; i < type.length; i++) {
            Document document = new Document();
            document.setType(type[i]);
            document.setText2(heading[i]);
            document.setText3(description[i]);
            data4.add(document);
        }
        labsdata.addAll(data4);
        return data4;
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvDummy.setLayoutManager(layoutManager);
        binding.rvDummy.hasFixedSize();
        binding.rvDummy.setItemAnimator(new DefaultItemAnimator());
        labsdata.clear();
        labsdata = PrepareDataMessage2();
        OuterAdapter productAdapter = new OuterAdapter(context, labsdata);
        binding.rvDummy.setAdapter(productAdapter);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.find_labs));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setBadgeValue(3);
    }
}
