package com.example.eddie.songs3;

public class RegularItem
{    String number;
    String firstLine;
    boolean isFavourite;
    public RegularItem(String number, String firstLine, boolean isFavourite)
    {
        this.firstLine = firstLine;
        this.number =  number;
        this.isFavourite = isFavourite;
    }

    public String getNumber()
    {
        return number;
    }

    public String getFirstLine()
    {
        return firstLine;
    }

    public boolean isFavourite()
    {
        return  isFavourite;
    }


}
