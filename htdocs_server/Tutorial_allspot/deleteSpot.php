<?php
error_reporting(E_ALL ^ E_DEPRECATED);
    // connect to mysql database
    $con = mysql_connect('localhost','root','');
    if (!$con){
        die('Could not connect: ' . mysql_error());
    }
    mysql_select_db("myspots_test", $con); // name of your database
    
    // check if "image" abd "SpotID" is set 
    if(isset($_GET['id'])) {
        $spotID = $_GET['id'];  
    
        mysql_query("DELETE FROM `allspots` WHERE `id` = '$spotID' ")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$spotID' ;
        echo '<br> not set';
    }
    // close the connection
    mysql_close($con);
?>