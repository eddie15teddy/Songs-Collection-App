package com.example.eddie.songs3;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterWholeItem extends RecyclerView.Adapter<AdapterWholeItem.ViewHolder>
{
    int listItemLayout;

    ArrayList<WholeItem> itemsList;
    static Context c;

    public AdapterWholeItem(int layoutID, ArrayList<WholeItem> itemList,
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
                        com.example.eddie.songs3.R.layout.whole_item_layout,
                        parent,
                        false);

        ViewHolder myViewHolder = new ViewHolder(v);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView number = holder.number;
        TextView firstLine = holder.firstLine;
        TextView txtMatching = holder.txtMatching;
        TextView txtBefore = holder.txtBefore;
        TextView txtAfter = holder.txtAfter;

        number.setText(itemsList.get(position).getNum());
        firstLine.setText(itemsList.get(position).getFirstLine());
        txtMatching.setText(itemsList.get(position).getMatchingPhrase());
        txtAfter.setText(itemsList.get(position).getAfter());
        txtBefore.setText(itemsList.get(position).getBefore());
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
        public TextView txtMatching;
        public TextView txtBefore;
        public TextView txtAfter;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            number = itemView.findViewById(com.example.eddie.songs3.R.id.txtNumber);
            firstLine = itemView.findViewById(com.example.eddie.songs3.R.id.txtFirstLine);
            txtMatching = itemView.findViewById(com.example.eddie.songs3.R.id.txtMatchingPhrase);
            txtAfter = itemView.findViewById(com.example.eddie.songs3.R.id.txtAfterMatchingPhrase);
            txtBefore = itemView.findViewById(com.example.eddie.songs3.R.id.txtBeforeMatchingPhrase);
        }

        @Override
        public void onClick(View view)
        {

        }
    }
}
