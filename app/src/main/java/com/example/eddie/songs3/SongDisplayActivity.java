package com.example.eddie.songs3;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SongDisplayActivity extends AppCompatActivity{

    int songNumber;
    String firstLine;
    ArrayList<LineItemDisplay> itemList;
    AdapterDisplay adapter;
    boolean songIsFavourite = false;

    //for adding and removing songs from the favourites database
    SQLiteDatabase database;

    RecyclerView recyclerView;

    FloatingActionButton fabChords;
    FloatingActionButton fabFav;
    boolean chordAreOn;
    SharedPreferences sharedPreferences;

    CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.eddie.songs3.R.layout.activity_song_display);

        fabChords = findViewById(R.id.fabChord);
        fabFav = findViewById(R.id.fabFav);

        SongDisplayActivityData data = (SongDisplayActivityData)getIntent().getSerializableExtra("data");

        songNumber = data.getSongNumber();
        firstLine = data.getFirstLine();
        itemList = new ArrayList<>();


        recyclerView = findViewById(com.example.eddie.songs3.R.id.recViewDispActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //see if chords are on
        sharedPreferences = getSharedPreferences(AppTools.SHARED_PREF, Context.MODE_PRIVATE);
        chordAreOn = sharedPreferences.getBoolean(AppTools.SHARED_PREF_CHORDS, false);

        //create or open database for storing favourite songs
        database = openOrCreateDatabase(AppTools.FAV_SONG_DB_NAME, 0, null);
        //create table if does not exist

            database.execSQL("CREATE TABLE IF NOT EXISTS regFavTbl (songNum INT(3), firstLine VARCHAR)");



            //toolbar
         collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(songNumber + " - " + firstLine);                          //title

        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favFabPressed();
            }
        });

        fabChords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chordFabPressed();
            }
        });


        checkFavourite();

        displaySong();

    }


    private void displaySong()
    {
        try
        {
            itemList.clear();
            itemList.add(new LineItemDisplay("\n",false,false));

            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(songNumber + ".txt")));
            String line = "";

            reader.readLine();                          //skip the number line
            while((line = reader.readLine()) != null)
            {
                if(chordAreOn) {
                    LineItemDisplay item = new LineItemDisplay(line, AppTools.isBold(line), AppTools.isChordLine(line));
                    itemList.add(item);
                }
                else
                {
                    if(!AppTools.isChordLine(line))
                    {
                        LineItemDisplay item = new LineItemDisplay(line, AppTools.isBold(line), false);
                        itemList.add(item);
                    }
                }
            }

            //add 2 empty line
            itemList.add(new LineItemDisplay( "\n", false, false));
            itemList.add(new LineItemDisplay( "\n", false, false));

            //set the adapter
            adapter = new AdapterDisplay(com.example.eddie.songs3.R.layout.line_item_display, itemList);
            recyclerView.setAdapter(adapter);


        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void checkFavourite()
    {
        Cursor c;

        c = database.rawQuery("SELECT * FROM regFavTbl", null);


        int numberIndex =c.getColumnIndex("songNum");

        c.moveToFirst();
        int cCount = c.getCount();

        for(int i = 0; i < cCount; i++)
        {

            if((c.getInt(numberIndex)) == songNumber)
            {
                //found the song in the database, set img to on
                fabFav.setImageResource(R.drawable.ic_favorite_black);
                songIsFavourite = true;
                c.close();

                //check if the setting is on. If it is, change title to accent.
                if(AppTools.HIGHLIGHT_FAVOURITE_SONG_IN_VIEW)
                    setTitleColour(ContextCompat.getColor(this, R.color.colorAccent));

                break;          //don't need to continue the loop if song is found in database
            }
            c.moveToNext();
        }
    }

    private void favFabPressed()
    {
        //when button is clicked, reverse songIsFavourite boolean
        songIsFavourite = !songIsFavourite;

        //add or remove song to database
        if(songIsFavourite)
        {
            //add song to database
            database.execSQL("INSERT INTO regFavTbl (songNum, firstLine) VALUES (" + songNumber + " , '" + firstLine + "')");

            if(AppTools.HIGHLIGHT_FAVOURITE_SONG_IN_VIEW)
                setTitleColour(ContextCompat.getColor(this, R.color.colorAccent));


        }
        else {
            //remove song from database
            database.execSQL("DELETE FROM regFavTbl WHERE songNum = " + songNumber);

            if(AppTools.HIGHLIGHT_FAVOURITE_SONG_IN_VIEW)
                setTitleColour(ContextCompat.getColor(this, R.color.textColorMain));



        }

        //now switch the imgFavBtn image
        if(songIsFavourite)
            fabFav.setImageResource(R.drawable.ic_favorite_black);
        else
            fabFav.setImageResource(R.drawable.ic_favorite_border_black);

        fabFav.hide();              //there is a bug that makes the image disappear after trying to change it
        fabFav.show();              //this solves it

        //vibrate phone
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(5);
    }

    private void setTitleColour(int col)
    {//Sets the colour of the title of the passed colour
        collapsingToolbar.setCollapsedTitleTextColor(col);
        collapsingToolbar.setExpandedTitleColor(col);
    }//setTitleColour


    private void chordFabPressed()
    {
        chordAreOn =! chordAreOn;       //flip boolean
        sharedPreferences.edit().putBoolean(AppTools.SHARED_PREF_CHORDS, chordAreOn).apply();       //save change
        displaySong();  //display new
    }

}
