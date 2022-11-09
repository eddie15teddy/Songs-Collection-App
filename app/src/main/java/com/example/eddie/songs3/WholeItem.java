package com.example.eddie.songs3;

public class WholeItem
{
    String num, firstLine, matchingPhrase, before, after;

    public WholeItem(String num, String firstLine, String matchingPhrase, String before, String after)
    {
        this.num = num;
        this.firstLine = firstLine;
        this.matchingPhrase = matchingPhrase;
        this.before = before;
        this.after = after;
    }

    public String getNum()
    {
        return num;
    }

    public String getFirstLine()
    {
        return  firstLine;
    }

    public String getMatchingPhrase()
    {
        return matchingPhrase;
    }

    public String getBefore()
    {
        return before;
    }

    public String getAfter()
    {
        return after;
    }

}
