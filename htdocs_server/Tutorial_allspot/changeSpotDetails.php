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
        $uTitle = urldecode($_POST["title"]);
        $uAddr = urldecode($_POST["addr"]);
        $uArea = urldecode($_POST["area"]);
        $uPhone = urldecode($_POST["phone"]);
        $uContents = urldecode($_POST["contents"]);
        $uType = urldecode($_POST["type"]);

        mysql_query("UPDATE `allspots` SET `title` = '$uTitle', `addr` = '$uAddr', `area` = '$uArea' ,`phone` = '$uPhone',`type` = '$uType',`contents` = '$uContents'
                     WHERE `id` = '$sid' ")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$sid' , '$uTitle' , '$uAddr' , '$uArea', '$uPhone';
        echo '<br> not set';
    }
    // close the connection
    mysql_close($con);
?>