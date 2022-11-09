package com.example.eddie.songs3;

import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDisplay extends RecyclerView.Adapter<AdapterDisplay.ViewHolder> {

    private int listItemLayout;
    private ArrayList<LineItemDisplay> itemList;

    //constructor
    public AdapterDisplay(int layoutID, ArrayList<LineItemDisplay> itemList)
    {
        listItemLayout = layoutID;
        this.itemList = itemList;
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.example.eddie.songs3.R.layout.line_item_display, parent, false);
        ViewHolder myViewHolder = new ViewHolder(v);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int i) {

        TextView textLineView = holder.lyrics;
         textLineView.setTypeface(null, Typeface.NORMAL);

        String textLine = itemList.get(i).getTextLine();

        if(itemList.get(i).isBold())
        {
            textLineView.setTypeface(null, Typeface.BOLD);
            textLine = AppTools.removeSymbols(textLine);
        }
        if(itemList.get(i).isChordLine())
        {
            textLineView.setTextColor(ContextCompat.getColor(SearchActivityReg.context, R.color.textColorChords));
            textLine = AppTools.removeSymbols(textLine);
        }
        else                    //when scrolling some lines would become bold too. That's because the way recycler view works. This was if line is not bold, it's set to be white
        {
            textLineView.setTextColor(ContextCompat.getColor(SearchActivityReg.context, R.color.textColorMain));
        }
        textLineView.setText(textLine);

    }

    static  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView lyrics;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            lyrics = (TextView) itemView.findViewById(com.example.eddie.songs3.R.id.txtLyrics);

        }
        @Override
        public void onClick(View view)
        {

        }

    }



}
