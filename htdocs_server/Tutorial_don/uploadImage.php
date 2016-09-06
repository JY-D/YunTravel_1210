<?php
error_reporting(E_ALL ^ E_DEPRECATED);
    // connect to mysql database
    $con = mysql_connect('localhost','root','');
    if (!$con){
        die('Could not connect: ' . mysql_error());
    }
    mysql_select_db("myspots_test", $con); // name of your database
    
    // check if "image" abd "SpotID" is set 
    if(isset($_POST["image"]) && isset($_POST["SpotID"])) {
        $data = $_POST["image"];
        $SpotID = $_POST["SpotID"];
        $ImageName = $SpotID.".jpg";
        $filePath = "C:/xampp/htdocs/Tutorial_don/img/".$ImageName;  // path of the file to store
        echo "file ".$filePath;
        // check if file exits
        if (file_exists($filePath)) {
            unlink($filePath); // delete the old file
        } 
        // create a new empty file
        $myfile = fopen($filePath, "w") or die("Unable to open file!");
        // add data to that file
        file_put_contents($filePath, base64_decode($data));
        // update the Customer table with new image name.
        mysql_query("UPDATE myspots_don SET pic='$ImageName' WHERE id='$SpotID'")
            or die('Could not save Image Name: ' . mysql_error());
        
    } else {
        echo 'not set';
    }
    // close the connection
    mysql_close($con);
?>