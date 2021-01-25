package com.drugvilla.in.adapter.blog.comment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.RowCommentsBinding;
import com.drugvilla.in.listener.CommentListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.comments.CommentsData;
import com.drugvilla.in.model.blog.comments.ReplyData;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private Context context;
    private List<CommentsData> dataList;
    private List<ReplyData> listReply = new ArrayList<>();
    private boolean showReply = false;
    private CommentListener listener;
    private LinearLayoutManager layoutManager;
    private OnSelectedListener onSelectedListener;


    public CommentAdapter(Context context, List<CommentsData> list, CommentListener listener, OnSelectedListener onSelectedListener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
        this.onSelectedListener = onSelectedListener;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.MyViewHolder holder, final int position) {

        final CommentsData model = dataList.get(position);
        if (model.getReplyDataList() != null && !model.getReplyDataList().isEmpty()) {
            holder.binding.tvViewReplies.setVisibility(View.VISIBLE);
            holder.binding.viewReply.setVisibility(View.VISIBLE);
            listReply.clear();
            listReply.addAll(model.getReplyDataList());
        } else {
            holder.binding.tvViewReplies.setVisibility(View.GONE);
            holder.binding.viewReply.setVisibility(View.GONE);
            listReply.clear();
        }

        // TODO : set rating also

        holder.binding.tvViewReplies.setText("View replies(" + listReply.size() + ")");
        holder.binding.tvComment.setText(model.getComment());
        holder.binding.tvName.setText(model.getName());
        holder.binding.tvTime.setText(model.getDate() + "," + model.getTime());
        holder.binding.tvUser.setText(model.getName().substring(0, 1));
        CommonUtils.getRandomColors(holder.binding.tvUser);
        holder.binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.binding.ivMenu);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                listener.onClick(item, position);
                                notifyDataSetChanged();
                                break;
                            case R.id.edit:
                                listener.onClick(item, position);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        holder.binding.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedListener.onClick(view, position);
            }
        });
        holder.binding.tvViewReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showReply) {
                    showReply = true;
                    holder.binding.rvreplies.setVisibility(View.VISIBLE);
                    holder.binding.tvViewReplies.setText("Hide replies(" + listReply.size() + ")");
                    setAdapter();
                } else {
                    holder.binding.rvreplies.setVisibility(View.GONE);
                    holder.binding.tvViewReplies.setText("View replies(" + listReply.size() + ")");
                    showReply = false;
                }
            }

            @SuppressLint("WrongConstant")
            private void setAdapter() {
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.binding.rvreplies.setLayoutManager(layoutManager);
                holder.binding.rvreplies.setHasFixedSize(true);
                CommentReplyAdapter adapter = new CommentReplyAdapter(context, listReply, new CommentListener() {
                    @Override
                    public void onClick(MenuItem view, int position) {
                        // TODO : layout changes ( call delete comment and edit reply comment popup also and call api's id user is the one who reply on comments )

                        switch (view.getItemId()) {
                            case R.id.delete:
                                //listener.onClick(view,position);
                                break;
                            case R.id.edit:
                               // listener.onClick(view,position);
                                break;
                        }
                    }
                });
                holder.binding.rvreplies.setAdapter(adapter);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowCommentsBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

