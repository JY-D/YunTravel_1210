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
    if(isset($_POST["id"]) && isset($_POST["name"]) && isset($_POST["title"]) && isset($_POST["start"]) && isset($_POST["end"])) {

        
        $ScheduleID = $_POST["id"];
        $uName = urldecode($_POST["name"]);
        $uTitle = urldecode($_POST["title"]);
        $uStart = urldecode($_POST["start"]);
        $uEnd = urldecode($_POST["end"]);
        $uContents = urldecode($_POST["contents"]);
        $uPic = urldecode($_POST["pic"]);
        
        mysql_query("INSERT INTO `myschedule`(`id`, `name`, `title`, `start`, `end`, `contents`, `pic`) 
            VALUES ('ScheduleID','$uName' , '$uTitle' , '$uStart' , '$uEnd' , '$uContents','$uPic');")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$ScheduleID' , '$uName' , '$uTitle' , '$uStart' , '$uEnd';
        echo 'not set';
    }
    // close the connection
    mysql_close($con);
?>