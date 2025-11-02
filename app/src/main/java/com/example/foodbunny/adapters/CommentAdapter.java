package com.example.foodbunny.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.R;
import com.example.foodbunny.objects.Comment;
import com.example.foodbunny.objects.Recipe;
import com.example.foodbunny.objects.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tvUsername.setText(comment.getUser().getName());
        holder.tvComment.setText(comment.getComment());

        // formating the data posted
//        String datePosted = comment.getDatePosted();
//        String displayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(datePosted);
        holder.tvCommentPostedDate.setText(comment.getDatePosted());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void updateData(List<Comment> newList) {
        Log.d("CommentAdapter", "Updating list, new size: " + newList.size());
        commentList = newList;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCommentPostedDate, tvComment;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCommentPostedDate = itemView.findViewById(R.id.tvCommentPostedDate);
            tvComment = itemView.findViewById(R.id.tvComment);

        }
    }
}
