package com.example.eddie.songs3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteActivity extends BaseActivity {

    RecyclerView recyclerView;
    ArrayList<RegularItem> itemList;
    AdapterRegularItem adapter;
    NavigationView navView;
    TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(com.example.eddie.songs3.R.id.recyclerFavReg);
        itemList = new ArrayList<>();
        adapter = new AdapterRegularItem(com.example.eddie.songs3.R.layout.regular_item_layout, itemList, this);
        navView= findViewById(com.example.eddie.songs3.R.id.nav_view);
        txtEmpty = findViewById(R.id.txtEmpty);

        getSupportActionBar().setTitle(R.string.nav_fav);

        //set number checked
        navView.getMenu().getItem(1).setChecked(true);

        //set recycler view properties
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                RegularItem item = itemList.get(position);
                int songNum = Integer.parseInt(item.getNumber());
                String firstLine = item.getFirstLine();

                Intent intent = new Intent(FavouriteActivity.this, SongDisplayActivity.class);
                SongDisplayActivityData data = new SongDisplayActivityData(songNum, firstLine);
                intent.putExtra("data", data);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        FavouriteActivity.this, new Pair<View, String>(view.findViewById(com.example.eddie.songs3.R.id.regularSearchItemLayout), AppTools.TRANSITION_SLIDE));

               // startActivity(intent, options.toBundle());
                startActivity(intent);          //start without fancy transitions

            }
        }));

        loadList();
        displayList();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
        displayList();
    }

    private void loadList()
    {
        itemList.clear();

        SQLiteDatabase database = this.openOrCreateDatabase(AppTools.FAV_SONG_DB_NAME,this.MODE_PRIVATE, null);

        database.execSQL("CREATE TABLE IF NOT EXISTS regFavTbl (songNum INT(3), firstLine VARCHAR)");

        Cursor c = database.rawQuery("SELECT * FROM regFavTbl", null);

        int songNumIndex = c.getColumnIndex("songNum");
        int firstLineIndex = c.getColumnIndex("firstLine");
        int cCount = c.getCount();

        c.moveToFirst();
        for(int i = 0; i < cCount; i++)
        {
            int num;
            String line;

            num = c.getInt(songNumIndex);
            line = c.getString(firstLineIndex);

            itemList.add(new RegularItem(Integer.toString(num), line, true));

            c.moveToNext();
        }
    }

    private void displayList()
    {
        if(itemList.size() > 0)
            txtEmpty.setVisibility(View.INVISIBLE);
        else
            txtEmpty.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();
    }



    @Override
    protected int getContentLayoutResId() {
        return com.example.eddie.songs3.R.layout.activity_favourite;
    }

    @Override
    protected Context getContext() {
        return this;
    }
}
