package com.drugvilla.in.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.wallet.TransactionAdapter;
import com.drugvilla.in.databinding.ActivityWalletBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.utils.ActivityController;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wallet extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityWalletBinding binding;
    private final String[] title = {"Coins Received", "Coins Paid", "Coins Paid", "Coins Received", "Coins Paid", "Coins Received", "Coins Paid", "Coins Paid", "Coins Received", "Coins Paid"};
    private final String[] date = {"20th Nov 2019", "20th Nov 2019", "22nd Nov 2019", "29th Nov 2019", "2nd Dec 2019", "20th Nov 2019", "20th Nov 2019", "22nd Nov 2019", "29th Nov 2019", "2nd Dec 2019"};
    private final String[] coins = {"150", "50", "75", "3", "1", "150", "50", "75", "3", "1"};
    private final String[] type = {"Referral Offer", "Product Deals", "Product Deals", "Exclusive Offer", "Exclusive Offer", "Referral Offer", "Product Deals", "Product Deals", "Exclusive Offer", "Exclusive Offer"};

    private List<Document> listData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        context = Wallet.this;
        setToolbar();
        setListener();
        // setEmptyLayout(true);   // when there are no transaction in wallet
        setAdapter();
    }


    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvTransactions.setLayoutManager(layoutManager);
        binding.rvTransactions.hasFixedSize();
        //  binding.rvTransactions.setItemAnimator(new DefaultItemAnimator());
        listData.clear();
        listData = PrepareDataMessage();
        TransactionAdapter adapter = new TransactionAdapter(context, listData);
        binding.rvTransactions.setAdapter(adapter);
    }

    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            Document document = new Document();
            document.setText(title[i]);
            document.setText2(coins[i]);
            document.setText3(date[i]);
            document.setText4(type[i]);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setListener() {
        binding.llMyCoins.setOnClickListener(this);
        binding.llReferEarn.setOnClickListener(this);
        binding.referEarn.setOnClickListener(this);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.drugvilla_title);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.ivRight.setImageResource(R.mipmap.info);
        binding.menubar.ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.referEarn:
                ActivityController.startActivity(context, RefferEarn.class);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivRight:
                showBottomSheetDialog();
                break;
            case R.id.llReferEarn:
                // setEmptyLayout(false);
                binding.rvTransactions.setVisibility(View.GONE);
                binding.referEarn.setVisibility(View.VISIBLE);
                break;
            case R.id.llMyCoins:
                //  setEmptyLayout(true);
                binding.rvTransactions.setVisibility(View.VISIBLE);
                binding.referEarn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.wallet_help, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        Objects.requireNonNull(ivCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // when there are no transaction in wallet
    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.llNoTransactions.llRoot.setVisibility(View.VISIBLE);
            binding.llNoTransactions.ivImage.setImageResource(R.drawable.notransaction);
            binding.llNoTransactions.btnSubmit.setVisibility(View.GONE);
            binding.llNoTransactions.tvHeading.setText(getResources().getString(R.string.no_transaction));
            binding.llNoTransactions.tvSubHeading.setVisibility(View.VISIBLE);
            binding.llNoTransactions.tvSubHeading.setText(getResources().getString(R.string.no_transaction_found));
        } else {
            binding.llNoTransactions.llRoot.setVisibility(View.GONE);
        }
    }
}
