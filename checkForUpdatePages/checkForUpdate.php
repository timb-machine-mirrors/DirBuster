<?

    /*
     * A list of the all version
     */
    $versions=array("0.10", "0.11", "0.11.1", "0.12");

    /*
     * Change log for each version
     */

     $text010 = "";

     
     $text011 = <<<EOT
* Fixed a couple of points within the GUI, and spelling mistakes.
* Added more content to the help section.
* Improved the way in which dirbuster handles inconsistent fail codes
* Fixed a bug that caused deadlock due to all the parsing threads exiting
* Tweaked the content analysis code to reduce false positives, when dirbuster is using that mode 
* Added code to make sure it display correctly on Vista
* Fixed a bug that caused files found to not be shown in the report
* Slight tweat to worker to improve performace
* Added a windows installer
* Added webstart
EOT;

     $text0111 = <<<EOT
* Fixed a bug in check for update code
EOT;

    $text011a = "Improved how DirBuster handles 200 fail codes, New windows Installer & other small tweaks";

    $text0111a = "Fixed a bug in check for update code";

    $text012 = <<<EOT
* Command line interface added
* Fixed a bug that caused the "User Agent" to not get set when adding custom headers
* Updated all api's used
EOT;


    /*
     * An array of all the change logs
     */
     $changelogs = array($text010, $text011a, $text0111a, $text012);


     /*
      * functions
      */
    function printlatest()
    {

        echo <<<EOT
<version current="Running - latest"/>
<changelog>
	No updates found
</changelog>
EOT;
    }


 
    if($version = $_GET['version'])
    {
        $beta = false;

        /*
         * check format of version
         */

         
        /*
         * Are we looking at a beta version
         */
         if(strrpos($version, "-") > 0)
         {
            $beta = true;
            $version = substr($version, 0, strpos($version, "-"));  
         }

         $foundCurrent = false;
         $changelog = "";
         
         for ( $a = 0; $a < count($versions); $a++)
         {

                if($foundCurrent)
                {
                    if($changelog == "")
                    {
                        $changelog = $changelogs[$a];
                    }
                    else
                    {
                        $changelog = $changelog.", ".$changelogs[$a];
                    }
                    //$changelog = $changelog.$versions[$a]."\n\n".$changelogs[$a]."\n\n";
                    
                }

                /*
                 * find the current version
                 */
                 if($version == $versions[$a])
                 {
                   /*
                    * Is this the latest version?
                    */

                    if($a ==  (count($versions) - 1) && !$beta)
                    {
                        printlatest();
                        exit(0);
                    }
                    else
                    {
                        $foundCurrent = true;
                        
                        if($beta)
                        {
                            $changelog = $changelog.$versions[$a]."\n\n".$changelogs[$a]."\n\n";
                            
                        }
                    
                    }

                 }

         } //end of for

         /*
          * Print the very simple xml
          */

          if(!$foundCurrent && $beta)
          {
            printlatest();
            exit(0);
          }
          elseif(!$foundCurrent)
          {
            printlatest();
            exit(0);
          }
          else
          {
            echo "<version current=\"".$versions[count($versions) -1]."\"/>\n";
            echo "<changelog> $changelog </changelog>\n";
           }
          //echo $changelog;
          //echo "\n";

    }
    else
    {
        printlatest();
    }