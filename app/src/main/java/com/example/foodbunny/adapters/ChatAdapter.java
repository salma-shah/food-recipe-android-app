package com.example.foodbunny.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.R;
import com.example.foodbunny.objects.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messageList;

    // view types: user & chatbot
    private final int viewTypeUser = 1;
    private final int viewTypeChatBot = 2;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).isUser()) {
            return viewTypeUser;
        } else {
            return viewTypeChatBot;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == viewTypeUser) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_msg, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatbot_msg, parent, false);
            return new ChatBotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder.getItemViewType() == viewTypeUser) {
            ((UserViewHolder) holder).tvUserMsg.setText(message.getMessage());
        } else {
            ((ChatBotViewHolder) holder).tvChatBotMsg.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMsg;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserMsg = itemView.findViewById(R.id.tvUserMsg);
        }
    }

    static class ChatBotViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatBotMsg;

        ChatBotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatBotMsg = itemView.findViewById(R.id.tvChatBotMsg);
        }
    }
}

