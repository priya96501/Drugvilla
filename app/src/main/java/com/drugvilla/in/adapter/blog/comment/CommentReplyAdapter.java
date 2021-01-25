package com.drugvilla.in.adapter.blog.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogLeaveCommentBinding;
import com.drugvilla.in.databinding.RowRepliesBinding;
import com.drugvilla.in.listener.CommentListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.comments.ReplyData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.MyViewHolder> {
    private final Context context;
    private final List<ReplyData> dataList;
    private CommentListener listener;

    CommentReplyAdapter(Context context, List<ReplyData> data,CommentListener listener) {
        this.context = context;
        this.dataList = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentReplyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_replies, parent, false);
        return new CommentReplyAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CommentReplyAdapter.MyViewHolder holder, final int position) {


        ReplyData model = dataList.get(position);
        if (holder.binding != null) {
            holder.binding.tvName.setText(model.getName());
            holder.binding.tvComment.setText(model.getComment());
            holder.binding.tvTime.setText(model.getDate() + "," + model.getTime());
            holder.binding.tvReplyTo.setText("Reply to " + dataList.get(position).getReply_to());
            holder.binding.tvUser.setText(model.getName().substring(0, 1));
            CommonUtils.getRandomColors(holder.binding.tvUser);
            holder.binding.tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheetDialog(position);
                }


            });

            // TODO : layout changes ( call delete comment and edit reply comment popup also and call api's id user is the one who reply on comments )
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
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowRepliesBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void showBottomSheetDialog(final int position) {
        final DialogLeaveCommentBinding comentBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_leave_comment, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, comentBinding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        comentBinding.ratingBar.setVisibility(View.GONE);
        comentBinding.etEmail.setText(SharedPref.getStringPreferences(context, AppConstant.USER_EMAIL));
        comentBinding.etname.setText(SharedPref.getStringPreferences(context, AppConstant.USER_NAME));
        comentBinding.tvHeading.setText("Reply to " + dataList.get(position).getReply_to());
        comentBinding.btnPostComment.setText(context.getResources().getString(R.string.post_reply));

        comentBinding.btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(comentBinding.etname.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etname, Objects.requireNonNull(comentBinding.tvNameError),
                            context.getResources().getString(R.string.empty_name));
                } else if (comentBinding.etname.getText().toString().length() < 2) {
                    CommonUtils.setErrorMessage(comentBinding.etname, Objects.requireNonNull(comentBinding.tvNameError),
                            context.getResources().getString(R.string.invalid_name));
                } else if (Objects.requireNonNull(comentBinding.etEmail.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etEmail, Objects.requireNonNull(comentBinding.tvEmailError),
                            context.getResources().getString(R.string.empty_email));
                } else if (!RegexUtils.isEmailValid(comentBinding.etEmail.getText().toString().trim())) {
                    CommonUtils.setErrorMessage(comentBinding.etEmail, Objects.requireNonNull(comentBinding.tvEmailError),
                            context.getResources().getString(R.string.invalid_email));
                } else if (Objects.requireNonNull(comentBinding.etComment.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etComment, Objects.requireNonNull(comentBinding.tvCommentError),
                            context.getResources().getString(R.string.empty_comment));
                } else if (comentBinding.etComment.getText().toString().length() < 5) {
                    CommonUtils.setErrorMessage(comentBinding.etComment, Objects.requireNonNull(comentBinding.tvCommentError),
                            context.getResources().getString(R.string.invalid_comment));
                } else {
                    dialog.dismiss();

                    replyOnCommentApi(dataList.get(position).getComment_id(), dataList.get(position).getBlog_id(),
                            comentBinding.etname.getText().toString(), comentBinding.etEmail.getText().toString(), comentBinding.etComment.getText().toString(),
                            dataList.get(position).getName());
                }

            }
        });
    }


    private void replyOnCommentApi(String comment_id, String blogId, String name, String email, String comment, String replyTo) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.replyOnComment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId, name, email, comment, comment_id, replyTo)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        notifyDataSetChanged();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }

    }
}