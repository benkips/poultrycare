package com.mabnets.www.poultry;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class msgadapter extends RecyclerView.Adapter {
    private ArrayList mglist;
    private Context context;
    private String vsender;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public msgadapter(ArrayList mglist, Context context, String vsender) {
        this.mglist = mglist;
        this.context = context;
        this.vsender = vsender;
    }

    public void updateData( ArrayList mglists) {
        this.mglist = mglists;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leftchats, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rightchats, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        chatitems chatitem=(chatitems)mglist.get(i);
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder)viewHolder).bind(chatitem);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(chatitem);
        }
    }

    @Override
    public int getItemCount() {
        if(mglist!=null){
            return mglist.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        chatitems chatitem=(chatitems)mglist.get(position);
        if (chatitem.conversation.startsWith(vsender+"v")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else  {
            // If some other user sent the message

            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        public TextView textv;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            textv=(TextView)itemView.findViewById(R.id.textmessage);
        }
        void bind(chatitems chatitemz) {
         textv.setText(chatitemz.message);
        }
    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        public TextView textv;

        SentMessageHolder(View itemView) {
            super(itemView);
            textv=(TextView)itemView.findViewById(R.id.textmessage);
        }
        void bind(chatitems chatitemx) {
            textv.setText(chatitemx.message);
        }
    }
}
