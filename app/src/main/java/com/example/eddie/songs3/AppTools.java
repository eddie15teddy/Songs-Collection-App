package com.example.eddie.songs3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class AppTools
{

    public static final String TRANSITION_SLIDE = "transition_1";

    public static final int SONG_AMOUNT = 180;

    public static final String SITE_LINK = "https://www.slavic-baptist.com/";
    public static final String YOUTUBE_LINK = "https://www.youtube.com/channel/UCXWmnxJm6kC9Lbr08xcWAfA?view_as=subscriber";
    public static final String BUG_LINK = "https://www.slavic-baptist.com/report-app-bug";

    //this is the favourite songs database name
    public static final String FAV_SONG_DB_NAME = "FavSongList";

    public static final String SHARED_PREF = "appSharedPref";
    public static final String SHARED_PREF_CHORDS = "areChordOn";

    public static final String ACCENT_COLOR = "#00FFFF";
    public static final String HINT_COLOR = "#e5e4e4";

    //This is to easily change the setting without removing or adding the code.
    //User has no access to this setting.
    //If this is true, song title will be highlighted if its favourite.
    public static final boolean HIGHLIGHT_FAVOURITE_SONG_IN_VIEW = true;

    public static boolean isBold(String text)
    {
        if(text.length() >= 2)
        return  text.charAt(0) == '/' && text.charAt(1) == 'b';

        //if statement false, return false
        return false;
    }

    //check if line is a for chords
    public static boolean isChordLine(String text)
    {
        if(text.length() >= 2)
            return text.charAt(0) == '/' && text.charAt(1) == 'c';

        //if the above is not true, return false
        return false;
    }

    public static String removeSymbols(String text)
    {
        if(isBold(text))
        {
            StringBuilder newText = new StringBuilder();
            Pattern p = Pattern.compile("/b(.*)");
            Matcher m = p.matcher(text);

            while(m.find())
            {
                newText.append(m.group(1));
            }
            return newText.toString();
        }
        else if(isChordLine(text))
        {
            StringBuilder newText = new StringBuilder();
            Pattern p = Pattern.compile("/c(.*)");
            Matcher m = p.matcher(text);

            while(m.find())
            {
                newText.append(m.group(1));
            }
            return newText.toString();
        }
        else
            return text;
    }


}
