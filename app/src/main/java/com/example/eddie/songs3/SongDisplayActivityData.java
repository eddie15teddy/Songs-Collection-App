package com.example.eddie.songs3;

//this class is used to send all required data to the SongDisplayActivity


import java.io.Serializable;

public class SongDisplayActivityData implements Serializable
 {

    private int songNumber;
    private String firstLine;

    public SongDisplayActivityData( int songNumber, String firstLine)
    {
        this.songNumber = songNumber;
        this.firstLine = firstLine;
    }

    public int getSongNumber()
    {
        return songNumber;
    }

    public String getFirstLine()
    {
        return firstLine;
    }
}
