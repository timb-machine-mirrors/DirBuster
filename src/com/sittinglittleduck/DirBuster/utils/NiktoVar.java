/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sittinglittleduck.DirBuster.utils;

import java.util.Vector;

/**
 *
 * @author james
 */
public class NiktoVar
{
    private String name = "";
    private Vector<String> data = new Vector<String>(10,10);

    public NiktoVar(String name, Vector<String> data)
    {
        this.name = name;
        this.data = data;
    }

    public Vector<String> getData()
    {
        return data;
    }

    public String getName()
    {
        return name;
    }





}
