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
    if(isset($_POST["id"]) && isset($_POST["pic"]) && isset($_POST["title"]) && isset($_POST["coords"]) && 
        isset($_POST["addr"]) && isset($_POST["area"]) && isset($_POST["type"])) {

        
        $SpotID = $_POST["id"];
        $ImageName = $_POST["pic"];    
        $uTitle = urldecode($_POST['title']);
        $uCoords = $_POST["coords"];
        $uAddr = urldecode($_POST['addr']);
        $uArea = urldecode($_POST['area']);
        $uType = urldecode($_POST["type"]);
        
        mysql_query("INSERT INTO `allspots`(`id`, `title`, `coords`, `pic`, `type`, `addr`, `area`) 
            VALUES ('$SpotID' , '$uTitle' , '$uCoords' , '$ImageName' , '$uType', '$uAddr' , '$uArea');")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo '$SpotID' , '$uTitle' , '$uCoords' , '$ImageName' , '$uType', '$uAddr' , '$uArea';
        echo 'not set';
    }
    // close the connection
    mysql_close($con);
?>