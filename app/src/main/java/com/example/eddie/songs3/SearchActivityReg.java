package com.example.eddie.songs3;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.navigation.NavigationView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchActivityReg extends BaseActivity {

    NavigationView navView;
    boolean startedBySplashAct = false;
    RecyclerView recyclerView;
    ArrayList<RegularItem> regularItemList;
    ArrayList<WholeItem> wholeItemList;
    public static Context context;

    //this holds the numbers of songs being shown, in the same order
    //this is used to see what number to show, when the use presses on it
    ArrayList<Integer> songNumList;

    //this is used to sort the search results alphabetically
    ArrayList<String> songLineList;
    ArrayList<String> songFirstLineList;

    EditText txtIn;
    Switch swtSearchMode;

    AdapterRegularItem arrayAdapterReg;
    AdapterWholeItem arrayAdapterWhole;

    final String TXT_EMPTY = "empty";
    final String TXT_NUMBERS = "number";
    final String TXT_STRING = "string";

    int lastNumberClickedPosition = 0;
    int lastNumberClickedNumber = 0;

    boolean regularSearchMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //see if this activity was started by splash activity
        //if activity was started by splash than interStr will be splash
        Intent intent = getIntent();
        String intentStr = intent.getStringExtra("parent_activity");
        if (intentStr != null && intentStr.equals("splash"))
            startedBySplashAct = true;

        //declare all veriables and widgets
        context = getContext();

        navView = findViewById(com.example.eddie.songs3.R.id.nav_view);
        txtIn = findViewById(com.example.eddie.songs3.R.id.txtInReg);
        swtSearchMode = findViewById(com.example.eddie.songs3.R.id.swtSearchType);

        regularItemList = new ArrayList<>();
        wholeItemList = new ArrayList<>();

        arrayAdapterReg = new AdapterRegularItem(com.example.eddie.songs3.R.layout.regular_item_layout, regularItemList, SearchActivityReg.this);
        arrayAdapterWhole = new AdapterWholeItem(com.example.eddie.songs3.R.layout.whole_item_layout, wholeItemList, SearchActivityReg.this);

        songNumList = new ArrayList<>();
        songLineList = new ArrayList<>();
        songFirstLineList = new ArrayList<>();

        //set the corresponding item checked
        navView.getMenu().getItem(0).setChecked(true);

        //set needed properties for recyclerView
        recyclerView = findViewById(com.example.eddie.songs3.R.id.RecyclerViewReg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(arrayAdapterReg);

        //on item touch
        recyclerView.addOnItemTouchListener(new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position) {

                lastNumberClickedPosition = position;

                String numberString = songNumList.get(position).toString();
                int number = Integer.parseInt(numberString);
                String line = songFirstLineList.get(position);

                lastNumberClickedNumber = number;

                SongDisplayActivityData data = new SongDisplayActivityData(number, line);
                Intent intent = new Intent(SearchActivityReg.this, SongDisplayActivity.class);
                intent.putExtra("data" , data);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        SearchActivityReg.this,
                        new Pair<View, String>(view.findViewById(com.example.eddie.songs3.R.id.regularSearchItemLayout), AppTools.TRANSITION_SLIDE));

                //startActivity(intent, options.toBundle());
                startActivity(intent);          //start without special transition


            }
        }));

        //show all songs
        try {
            showAllSongs();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        swtSearchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regularSearchMode = !swtSearchMode.isChecked();

                //change the color of the text depending on if the switch is on
                if(swtSearchMode.isChecked())
                    swtSearchMode.setTextColor(getResources().getColor(com.example.eddie.songs3.R.color.colorAccent));
                else
                    swtSearchMode.setTextColor(getResources().getColor(com.example.eddie.songs3.R.color.textColorMain));


                //so that when the user switches the search mode, it will search again, and show the new results
                String textViewText = txtIn.getText().toString();
                txtIn.setText(textViewText);

                //set the cursor to the end of the TextView
                txtIn.setSelection(txtIn.getText().length());

            }
        });

        /////////////////////////////////////////////
        ////////SEARCH ALGORITHMS START HERE/////////
        /////////////////////////////////////////////

        txtIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try {
                    //every time the text changes this will run, and search the songs
                    String text = txtIn.getText().toString();
                    String txtType = getTextType(text);

                    if (txtType.equals(TXT_EMPTY))
                        showAllSongs();
                    else
                    {
                        if(regularSearchMode)
                            regularSearch(txtType, text);
                        else
                            wholeSearch(text);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void wholeSearch(String text) throws IOException
    {
        wholeItemList.clear();
        songNumList.clear();
        songFirstLineList.clear();
        songLineList.clear();

        for(int i = 1; i <= AppTools.SONG_AMOUNT; i++)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(i + ".txt")));
            String currentLine;
            boolean found = false;

            reader.readLine();


            while((currentLine = reader.readLine()) != null && !found)
            {
                if (!AppTools.isChordLine(currentLine)) {   //only search if it's not a chord line

                    currentLine = AppTools.removeSymbols(currentLine);                //remove the /b and other symbols

                    if (currentLine.toLowerCase().contains(text.toLowerCase())) {
                        found = true;

                        String firstLine = getFirstLine(i);
                        firstLine = AppTools.removeSymbols(firstLine);


                        //split the line

                        //phrase is what the user has type into the search bar
                        String beforePhrase;
                        String afterPhrase;
                        String Phrase;

                        String[] phrases = currentLine.split("(?i)" + AppTools.removeSymbols(text));

                        beforePhrase = phrases[0];

                        if (phrases.length == 1)
                            afterPhrase = "";
                        else
                            afterPhrase = phrases[1];

                        Phrase = text.toLowerCase();

                        if (phrases.length > 2)              //if this is true, than the line was split multiple times, because text was in current line more than once.
                        //in this case, it will take the current line, and replace what was already made with nothing, and than add that to the end of the phrase
                        {
                            String alreadyMade = beforePhrase + Phrase + afterPhrase;
                            String restOfLine = currentLine.toLowerCase().replace(alreadyMade, "");

                            afterPhrase = afterPhrase + restOfLine;
                        }


                        //add all the items to the lists
                        WholeItem item = new WholeItem(i + "", firstLine, Phrase, beforePhrase, afterPhrase);

                        wholeItemList.add(item);
                        songNumList.add(i);
                        songFirstLineList.add(firstLine);
                        songLineList.add(currentLine);
                    }

                }
            }
            reader.close();
        }

        //set the adapters
        arrayAdapterWhole.notifyDataSetChanged();
        recyclerView.setAdapter(arrayAdapterWhole);
    }


    private void regularSearch(String txtType, String userSearch) throws IOException
    {
        //first step is to clear the old search results
        regularItemList.clear();
        songNumList.clear();
        songFirstLineList.clear();

        if(txtType.equals(TXT_NUMBERS))             //number search
        {
            //go through all the songs, and see which song numbers start with what the user has put in
            for(int i = 1; i <= AppTools.SONG_AMOUNT; i ++)
            {
                String currentNum = Integer.toString(i);

                if(currentNum.startsWith(userSearch))
                {
                    String firstLine = getFirstLine(i);

                    regularItemList.add(new RegularItem(currentNum, firstLine, isFavourite(i)));
                    songNumList.add(i);
                    songFirstLineList.add(firstLine);
                }

            }
        }
        else if(txtType.equals(TXT_STRING))         //string search
        {

            //go through all the songs, and see if the first line starts with what the user has types in
            for(int i = 1; i <= AppTools.SONG_AMOUNT; i++)
            {

                String firstLine = getFirstLine(i);

                //if the first line starts with what the user has put in, this songs has to be added to the lists to be displayed
                if(firstLine.toLowerCase().startsWith(userSearch.toLowerCase()))                    //case of letters doesnt matter
                {
                    regularItemList.add(new RegularItem(i + "", firstLine, isFavourite(i)));
                    songNumList.add(i);
                    songFirstLineList.add(firstLine);
                }
            }

        }

            arrayAdapterReg.notifyDataSetChanged();
            recyclerView.setAdapter(arrayAdapterReg);

    }


    private void showAllSongs() throws IOException
    {
        //clear all the old search results
        regularItemList.clear();
        songNumList.clear();
        songFirstLineList.clear();

        for(int i = 1; i <= AppTools.SONG_AMOUNT; i ++)
        {
            String number;
            String firstLine;

            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(i+ ".txt")));

            number = reader.readLine();
            firstLine = getFirstLine(i);

            reader.close();

            songFirstLineList.add(firstLine);

            regularItemList.add(new RegularItem(number, firstLine, isFavourite(i)));
            songNumList.add(i);

        }
            arrayAdapterReg.notifyDataSetChanged();
            recyclerView.setAdapter(arrayAdapterReg);

    }

    private String getFirstLine(int num) throws IOException
    {
        String firstLine;

        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(num + ".txt")));
        reader.readLine();  //skip song number line
        firstLine = reader.readLine();

        if(AppTools.isChordLine(firstLine))         //if the first line is a chord line, read the next line
            firstLine = reader.readLine();


        reader.close();

        return firstLine;
    }

    private String getTextType(String text)
    {
        if(text.length() == 0)                           //the user has not put in any text into the search bar
            return TXT_EMPTY;
        else
        {
            if(text.matches("[0-9]+"))              //there are only numbers in the search bar
                return TXT_NUMBERS;
            else                                          //there is a string, maybe with a number
                return TXT_STRING;
        }

    }

    private boolean isFavourite(int num)
    {
        SQLiteDatabase dtbase = this.openOrCreateDatabase(AppTools.FAV_SONG_DB_NAME, this.MODE_PRIVATE, null);
        dtbase.execSQL("CREATE TABLE IF NOT EXISTS regFavTbl (songNum INT(3), firstLine VARCHAR)");
        Cursor c = dtbase.rawQuery("SELECT * FROM regFavTbl", null);
        int songNumberIndex = c.getColumnIndex("songNum");
        int count = c.getCount();

        c.moveToFirst();
        for(int i = 0; i < count; i++)
        {
            int currentNum = c.getInt(songNumberIndex);
            if(currentNum == num)
            {
                c.close();
                return true;

            }
            c.moveToNext();
        }
        return false;
    }



    //required methods for NavigationDrawer
    @Override
    protected int getContentLayoutResId() {
        return com.example.eddie.songs3.R.layout.activity_search_reg;
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();


        //set the right Search Mode
        regularSearchMode = !swtSearchMode.isChecked();

       // txtIn.setText(txtIn.getText());                 //this triggers the searchAlgorithm

        //set the cursor to the end of the TextView
       // txtIn.setSelection(txtIn.getText().length());

        //check if the song was added or removed from favourites list
        if(lastNumberClickedPosition != 0)              //if value is 0 no song was clicked yet
        {


          RegularItem newItem = new RegularItem(regularItemList.get(lastNumberClickedPosition).number, regularItemList.get(lastNumberClickedPosition).firstLine, isFavourite(lastNumberClickedNumber));

          regularItemList.set(lastNumberClickedPosition, newItem );
          arrayAdapterReg.notifyItemChanged(lastNumberClickedPosition);
        }
    }


    @Override
    public void onBackPressed() {

        //erase the text if there is any
        if(txtIn.getText().length() != 0)
            txtIn.setText("");
        else
        {
            //if the app is started by splash, than exit, otherwise do regular onBackPressed
            if (!startedBySplashAct)
                super.onBackPressed();
            else
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}