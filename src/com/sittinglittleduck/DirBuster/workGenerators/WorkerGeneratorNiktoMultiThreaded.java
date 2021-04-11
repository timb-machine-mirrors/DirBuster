/*
 * WorkerGenerator.java
 *
 * Copyright 2008 James Fisher
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA
 */
package com.sittinglittleduck.DirBuster.workGenerators;

import com.sittinglittleduck.DirBuster.BaseCase;
import com.sittinglittleduck.DirBuster.Config;
import com.sittinglittleduck.DirBuster.GenBaseCase;
import com.sittinglittleduck.DirBuster.Manager;
import com.sittinglittleduck.DirBuster.WorkUnit;
import com.sittinglittleduck.DirBuster.utils.NiktoVar;
import com.sittinglittleduck.DirBuster.utils.NiktoVarHolder;
import com.sittinglittleduck.DirBuster.utils.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;

/**
 * Produces the work to be done, when we are reading from a nikto database
 *
 *
 */
public class WorkerGeneratorNiktoMultiThreaded extends MultiThreadedGenerator
{

    public static final int doDIR = 0;
    public static final int doFile = 1;
    private Manager manager;
    private BlockingQueue<WorkUnit> workQueue;
    private String inputFile;
    private String firstPart;
    private boolean stopMe = false;
    private boolean isWorking = true;
    private boolean first = false;
    HttpClient httpclient;
    private int counter = 1;

    /*
     * flag to tell the pause this thread
     */
    private boolean pleasewait = false;

    /*
     * location of the nikto database file
     */
    private String niktoDatabaseFile = "";

    /*
     * location of the nikto database vars file
     */
    private String niktoDatabaseVarsFile = "";

    /*
     * stores all the nikto vars
     */
    private NiktoVarHolder varsHolder = new NiktoVarHolder();

    /**
     * Creates a new instance of WorkerGenerator
     * @param manager Manager object
     */
    public WorkerGeneratorNiktoMultiThreaded(String firstpart)
    {
        manager = Manager.getInstance();
        workQueue = manager.workQueue;
        //this.firstPart = firstpart;
        niktoDatabaseFile = manager.getNiktoFileLocation() + File.separatorChar + "db_tests";
        niktoDatabaseVarsFile = manager.getNiktoFileLocation() + File.separatorChar + "db_variables";

        httpclient = manager.getHttpclient();

        firstPart = manager.getFirstPartOfURL();

        readNiktoVars();

    }

    /*
     * this reads and stores all the nikto vars
     */
    private void readNiktoVars()
    {
        BufferedReader d = null;
        try
        {
            /*
             * read in the vars file
             */


            d = new BufferedReader(new InputStreamReader(new FileInputStream(niktoDatabaseVarsFile)));

            String line = "";
            while((line = d.readLine()) != null)
            {
                /*
                 * if the line is not blank for starts with a comment
                 */
                if(!line.startsWith("#") && !line.equalsIgnoreCase(""))
                {
                    //System.out.println("line: " + line);
                    StringTokenizer st1 = new StringTokenizer(line, "=", false);
                    String name = st1.nextToken();
                    String data = st1.nextToken();
                    StringTokenizer st2 = new StringTokenizer(data, " ", false);

                    Vector<String> dataVector = new Vector<String>(10, 10);
                    while(st2.hasMoreTokens())
                    {
                        dataVector.addElement(st2.nextToken());
                    }

                    varsHolder.addVar(new NiktoVar(name, dataVector));
                }
            }


        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if(d != null)
            {
                try
                {
                    d.close();
                }
                catch(IOException ex)
                {
                    Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Thread run method
     */
    @Override
    public void run()
    {
        isWorking = true;

        Vector<String> names = varsHolder.getAllNames();



        BufferedReader d = null;
        String line = "";
        try
        {


            /*
             * read in the nikto database file
             */

            d = new BufferedReader(new InputStreamReader(new FileInputStream(niktoDatabaseFile)));

            while((line = d.readLine()) != null)
            {
                try
                {
                    /*
                     * if the line is not a comment or blank
                     */
                    if(!line.startsWith("#") && !line.equalsIgnoreCase(""))
                    {
                        //System.out.println("db line: " + line);
                        StringTokenizer st1 = new StringTokenizer(line, "\",\"", false);
                        /*
                         * skip the tokens we dont need
                         */

                        /* id */
                        st1.nextToken();

                        /* unknown */
                        st1.nextToken();

                        /* unknown */
                        st1.nextToken();

                        /* request */
                        String request = st1.nextToken();

                        /* method */
                        st1.nextToken();

                        /* trigger - can be either a response code or a string */
                        String expectedResponse = st1.nextToken();

                        int requiredCode = -1;

                        try
                        {
                            requiredCode = Integer.parseInt(expectedResponse);
                        }
                        catch(NumberFormatException e)
                        {
                            //do nothing as we dont care if it's not
                        }

                        

                        /*
                         * find and replace all the place holders in the item
                         * currently there is a max of one place holder per entry, if nikto change this code will not work
                         */

                        String foundPlaceHolder = "";
                        for(int a = 0; a < names.size(); a++)
                        {
                            if(request.contains(names.elementAt(a)))
                            {
                                foundPlaceHolder = names.elementAt(a);
                                break;
                            }
                        }


                        /*
                         * generate all the different requests
                         */
                        Vector<String> requests = new Vector<String>(10, 10);

                        if(foundPlaceHolder.equalsIgnoreCase(""))
                        {
                            /*
                             * stops duplicates
                             */
                            if(!requests.contains(request))
                            {
                                String temp = request;
                                if(!temp.startsWith("/"))
                                {
                                    temp = "/" + temp;

                                    //System.out.println("Added / to " + temp);
                                }

                                temp.replaceAll("\\\"", "\"");
                                temp.replaceAll("\\@", "@");
                                requests.addElement(temp);
                            }
                        }
                        else
                        {
                            for(int a = 0; a < varsHolder.getVarsByName(foundPlaceHolder).size(); a++)
                            {
                                String temp = request.replace(foundPlaceHolder, varsHolder.getVarsByName(foundPlaceHolder).elementAt(a));
                                if(!temp.startsWith("/"))
                                {
                                    temp = "/" + temp;

                                    //System.out.println("Added / to " + temp);
                                }

                                temp.replaceAll("\\\"", "\"");
                                temp.replaceAll("\\@", "@");
                                requests.addElement(temp);
                            }
                        }

                        /*
                         * for each request gen a base case and add to work queue.
                         */
                        for(int a = 0; a < requests.size(); a++)
                        {
                            if(doesStringOnlyContain(requests.elementAt(a), '/') && requests.elementAt(a).length() > 0)
                            {
                                /*
                                 * if the URL is just ////// etc then skip, as it will have the same result as / in 99.9999999999% of cases
                                 * if we do not do this it will casue infernate loops
                                 */

                                continue;
                            }
                            else if(requests.elementAt(a).equalsIgnoreCase("/%2e/") || requests.elementAt(a).equalsIgnoreCase("/./"))
                            {
                                /*
                                 * again these case infernate loops on most web servers
                                 */
                                continue;
                            }

                            if(Config.debug)
                            {
                                System.out.println("[DEBUG] - Adding NIKTO Request to queue: " + requests.elementAt(a));
                            }

                            /*
                             * if stop is called
                             */
                            if(stopMe)
                            {
                                isWorking = false;
                                return;
                            }

                            /*
                             * if we need to pause the thread
                             */
                            synchronized(this)
                            {
                                while(pleasewait)
                                {
                                    try
                                    {
                                        wait();
                                    }
                                    catch(InterruptedException e)
                                    {
                                        return;
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            /*
                             * Remove the query string, needed to determine the request required for the base case
                             */
                            String temprequest = requests.elementAt(a).trim();

                            if(temprequest.indexOf("?") > -1)
                            {
                                temprequest = temprequest.substring(0, temprequest.indexOf("?"));
                            }

                            /*
                             * store of the basecase of object
                             */
                            BaseCase baseCaseObj = null;

                            /*
                             * determine if we are looking at a dir or file request
                             */
                            if(temprequest.endsWith("/"))
                            {

                                /*
                                 * if is a dir
                                 */
                                if(temprequest.length() > 2)
                                {

                                    //System.out.println("NIKTO: This should be a dir = " + temprequest);
                                    /*
                                     * remove the last char which will be / the sub string on the new last /
                                     */
                                    temprequest = temprequest.substring(0, temprequest.length() - 1);
                                    //System.out.println("LAST char gone: " + temprequest);
                                    temprequest = temprequest.substring(0, temprequest.lastIndexOf("/") + 1);
                                    //System.out.println("DIR for basecase: " + temprequest);
                                    temprequest = Utils.makeURLSafe(firstPart + temprequest);

                                    baseCaseObj = GenBaseCase.genBaseCase(temprequest, true, null);

                                    String method = "";
                                    if(manager.getAuto() && !baseCaseObj.useContentAnalysisMode() && !baseCaseObj.isUseRegexInstead())
                                    {
                                        method = "HEAD";
                                    }
                                    else
                                    {
                                        method = "GET";
                                    }

                                    URL currentURL = new URL(Utils.makeURLSafe(firstPart + requests.elementAt(a)));
                                    //System.out.println("URL string: " + urlString);

                                    workQueue.put(new WorkUnit(currentURL, true, method, baseCaseObj, requests.elementAt(a), requiredCode));
                                    counter++;

                                }
                                else
                                {
                                    /*
                                     * dont need to process / requests as these will be performed else where
                                     */
                                }

                            }
                            else
                            {
                                /*
                                 * if it's a file
                                 */
                                //System.out.println("this should be a file: " + temprequest);
                                String file = temprequest.substring(temprequest.lastIndexOf("/"));
                                String fileExt = "";

                                if(file.contains("."))
                                {
                                    fileExt = Utils.makeURLSafe(file.substring(file.lastIndexOf(".")));
                                }

                                /*
                                 * get the base case for the request
                                 */
                                String fileLoc = Utils.makeURLSafe(firstPart + temprequest.substring(0, temprequest.lastIndexOf("/") + 1));
                                baseCaseObj = GenBaseCase.genBaseCase(fileLoc, false, fileExt);


                                String method = "";
                                if(manager.getAuto() && !baseCaseObj.useContentAnalysisMode() && !baseCaseObj.isUseRegexInstead())
                                {
                                    method = "HEAD";
                                }
                                else
                                {
                                    method = "GET";
                                }

                                String urlString = Utils.makeURLSafe(firstPart + temprequest);

                                URL currentURL = new URL(urlString);
                                //System.out.println("Rcode: " + requiredCode + " - URL string: " + urlString);

                                workQueue.put(new WorkUnit(currentURL, false, method, baseCaseObj, requests.elementAt(a), requiredCode));
                                counter++;

                            }
                        }// end of loop to generated requests
                    }//end of dealing with a valid line
                }
                catch(Exception e)
                {
                    Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, e);
                }
            }//end of reading lines form the nikto database


        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        //catch(InterruptedException ex)
        //{
        //    Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        //}
        catch(IOException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                d.close();
            }
            catch(IOException ex)
            {
                Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        isWorking = false;
        counter = -1;
        return;
    }

    /**
     * Method to stop the manager while it is working
     */
    @Override
    public void stopMe()
    {
        stopMe = true;
        counter = -1;
    }

    @Override
    public boolean isWorking()
    {
        return isWorking;
    }

    @Override
    public int getCurrentPoint()
    {
        return counter;
    }

    @Override
    public void pause()
    {
        this.pleasewait = true;
    }

    @Override
    public void unPause()
    {
        this.pleasewait = false;
    }

    @Override
    public boolean isPaused()
    {
        return pleasewait;
    }

    @Override
    public int getType()
    {
        return 2;
    }

    @Override
    public String getStartpoint()
    {
        return firstPart;
    }

    @Override
    public String getFileExt()
    {
        return null;
    }

    /*
     * used to return the number of items tht will be sent
     */
    public synchronized int numberOfRequestsToSend()
    {
        Vector<String> names = varsHolder.getAllNames();

        int total = 0;
        try
        {
            BufferedReader d = null;
            String line = "";
            /*
             * read in the nikto database file
             */
            d = new BufferedReader(new InputStreamReader(new FileInputStream(niktoDatabaseFile)));

            while((line = d.readLine()) != null)
            {
                if(!line.startsWith("#") && !line.equalsIgnoreCase(""))
                {
                    //System.out.println("db line: " + line);
                    StringTokenizer st1 = new StringTokenizer(line, "\",\"", false);
                    /*
                     * skip the tokens we dont need
                     */
                    st1.nextToken();
                    st1.nextToken();
                    st1.nextToken();
                    String request = st1.nextToken();

                    /*
                     * find and replace all the place holders in the item
                     * currently there is a max of one place holder per entry, if nikto change this code will not work
                     */

                    String foundPlaceHolder = "";
                    for(int a = 0; a < names.size(); a++)
                    {
                        if(request.contains(names.elementAt(a)))
                        {
                            foundPlaceHolder = names.elementAt(a);
                            break;
                        }
                    }


                    /*
                     * generate all the different requests
                     */
                    if(foundPlaceHolder.equalsIgnoreCase(""))
                    {
                        total++;
                    }
                    else
                    {
                        for(int a = 0; a < varsHolder.getVarsByName(foundPlaceHolder).size(); a++)
                        {
                            total++;
                        }
                    }
                }
            }

        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(WorkerGeneratorNiktoMultiThreaded.class.getName()).log(Level.SEVERE, null, ex);
        }


        System.out.println("Loaded nikto checks: " + total);
        return total;
    }


    private boolean doesStringOnlyContain(String string, char tofind)
    {
        char charArray[] = string.toCharArray();
        for(int a = 0; a < charArray.length; a++)
        {
            if(charArray[a] != tofind)
            {
                return false;
            }
        }
        return true;
    }
}
