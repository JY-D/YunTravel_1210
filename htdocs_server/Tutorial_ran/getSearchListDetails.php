<?php
error_reporting(E_ALL ^ E_DEPRECATED);
header('Content-Type: application/json; charset=utf-8');
  if(isset($_REQUEST['SpotList']))
     {
     $con = mysql_connect('localhost','root','');
     if (!$con)
     {
     	die('Could not connect: ' . mysql_error());
     }

     
     mysql_select_db("myspots_test", $con);
     mysql_query("SET NAMES 'utf8'");
    
     $SpotList = urldecode($_REQUEST['SpotList']);

     $result = mysql_query("SELECT * FROM `myspots_ran` WHERE list LIKE '%$SpotList%' ") or die ('Errant query:');

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