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
    if(isset($_POST["id"])&& isset($_POST["name"]) && isset($_POST["area"])) {

        
        $ScheduleID = $_POST["id"];  
        $uName = urldecode($_POST['name']);
        $uArea = urldecode($_POST['area']);
        
        mysql_query("INSERT INTO `myschedule_name`(`id`, `name`, `area`) 
            VALUES ('$ScheduleID' , '$uName' , '$uArea');")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$ScheduleID' , '$uName' , '$uArea';
        echo 'not set';
    }
    // close the connection
    mysql_close($con);
?>