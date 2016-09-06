<?php
     error_reporting(E_ALL ^ E_DEPRECATED);
     header('Content-Type: text/html; charset=utf-8');
    // connect to mysql database
    $con = mysql_connect('localhost','root','');
    if (!$con){
        die('Could not connect: ' . mysql_error());
    }
    mysql_select_db("myspots_test", $con); // name of your database

     mysql_query("SET NAMES utf8", $con);
    
    // check if "image" abd "SpotID" is set 
    if(isset($_POST["id"])) {

        $sid = $_POST["id"];
        $uList =  urldecode($_POST['list']);
        $query = mysql_query("SELECT `list` FROM `myspots`  WHERE `id` = '$sid' ")
            or die('Could not save Image Name: ' . mysql_error());

        $row = mysql_fetch_row($query);
        $former = $row[0];
        
       

        mysql_query("UPDATE `myspots` SET `list` = '$uList,$former'  WHERE `id` = '$sid' ")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$sid' , '$uList';
        echo '<br> not set';
    }
    // close the connection
    mysql_close($con);
?>