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
public class NiktoVarHolder
{
    private Vector<NiktoVar> vars = new Vector<NiktoVar>(10,10);

    public NiktoVarHolder()
    {
    }

    public void addVar(NiktoVar var)
    {
        vars.addElement(var);
    }

    public Vector<String> getVarsByName(String name)
    {
        for(int a = 0; a < vars.size(); a++)
        {
            if(vars.elementAt(a).getName().equalsIgnoreCase(name))
            {
                return vars.elementAt(a).getData();
            }
        }
        return null;
    }

    /*
     * returns a list of all var names in the holder.
     */
    public Vector<String> getAllNames()
    {
        Vector<String> names = new Vector<String>(10,10);

        for(int a = 0; a < vars.size(); a++)
        {
            names.addElement(vars.elementAt(a).getName());
        }

        return names;
    }

}
