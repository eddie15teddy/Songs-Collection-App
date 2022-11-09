package com.example.eddie.songs3;

public class LineItemDisplay
{
    String textLine;
    boolean isBold;
    boolean isChordLine;

    public LineItemDisplay(String textLine, boolean isBold, boolean isChordLine)
    {
        this.textLine = textLine;
        this.isBold = isBold;
        this.isChordLine = isChordLine;
    }

    public String getTextLine()
    {
        return textLine;
    }


    public boolean isBold() {
        return isBold;
    }

    public boolean isChordLine()
    {
        return isChordLine;
    }

}
