package com.example.eddie.songs3;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRegularItem extends RecyclerView.Adapter<AdapterRegularItem.ViewHolder>
{
    int listItemLayout;

    ArrayList<RegularItem> itemsList;
    static Context c;

    public AdapterRegularItem(int layoutID, ArrayList<RegularItem> itemList,
                              Context c)
    {
        this.listItemLayout = layoutID;
        this.itemsList = itemList;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())

                .inflate(
                        com.example.eddie.songs3.R.layout.regular_item_layout,
                        parent,
                        false);

        ViewHolder myViewHolder = new ViewHolder(v);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView number = holder.number;
        TextView firstLine = holder.firstLine;

        number.setText(itemsList.get(position).getNumber());
        firstLine.setText(itemsList.get(position).getFirstLine());

        if(itemsList.get(position).isFavourite)
        {
            number.setTextColor(Color.parseColor(AppTools.ACCENT_COLOR));
            firstLine.setTextColor(Color.parseColor(AppTools.ACCENT_COLOR));
        }
        else
        {
            number.setTextColor(Color.parseColor(AppTools.HINT_COLOR));
            firstLine.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        public TextView number;
        public TextView firstLine;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            number = itemView.findViewById(com.example.eddie.songs3.R.id.txtNumber);
            firstLine = itemView.findViewById(com.example.eddie.songs3.R.id.txtFirstLine);
        }

        @Override
        public void onClick(View view)
        {

        }
    }
}
