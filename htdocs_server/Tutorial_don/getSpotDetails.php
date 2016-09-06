<?php
error_reporting(E_ALL ^ E_DEPRECATED);
header('Content-Type: application/json; charset=utf-8');
  if(isset($_REQUEST['SpotID']))
     {
     $con = mysql_connect('localhost','root','');
     if (!$con)
     {
     	die('Could not connect: ' . mysql_error());
     }

     
     mysql_select_db("myspots_test", $con);
     mysql_query("SET NAMES 'utf8'");
    
     $spotID = $_REQUEST['SpotID'];
     $result = mysql_query("SELECT * FROM `myspots_don` WHERE id = '$spotID' ") or die ('Errant query:');

     while($row = mysql_fetch_assoc($result))
     {
        foreach ($row as $key => $value) {
         $row[$key] = urlencode($value);
     }
	 $output[]=$row;
     }
    
      echo urldecode(json_encode($output));

      mysql_close($con);
     }
     else
     {
     	$output = "not found";
        print(json_encode($output));

     }
?>