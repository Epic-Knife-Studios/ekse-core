package com.epicknife.server.util;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Pair<F, S>
{
    
    public F first;
    public S second;

    public Pair(F a, S b)
    {
        this.first = a;
        this.second = b;
    }

    public F getFirst()
    {
        return first;
    }

    public void setFirst(F first)
    {
        this.first = first;
    }

    public S getSecond()
    {
        return second;
    }

    public void setSecond(S second)
    {
        this.second = second;
    }

}