package com.example.eduardopires.bluetooth_chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex1 on 11/12/2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_bubble, parent, false);
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.textView.setText(message.body);

        if (message.isMine) {
            holder.layout.setBackgroundResource(R.drawable.bubble2);
            holder.parentLayout.setGravity(Gravity.RIGHT);
        } else {
            holder.layout.setBackgroundResource(R.drawable.bubble1);
            holder.parentLayout.setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    protected class MessageViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout parentLayout;
        public LinearLayout layout;
        public TextView textView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.bubble_layout_parent);
            layout = (LinearLayout) itemView.findViewById(R.id.bubble_layout);
            textView = (TextView) itemView.findViewById(R.id.message_text);
        }
    }

    public void updateMessageList(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }
}
