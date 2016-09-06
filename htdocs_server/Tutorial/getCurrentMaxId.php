<?php
error_reporting(E_ALL ^ E_DEPRECATED);

$con = $con = mysql_connect('localhost','root','');

if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
mysql_select_db("myspots_test", $con);
mysql_query("SET NAMES 'utf8'");

$result = mysql_query("SELECT MAX(id) FROM `myspots`");

/*while($row = mysql_fetch_assoc($result))
  {
    $output[]=$row;
  }*/
  $row = mysql_fetch_assoc($result);
  //echo json_encode($output);
  echo json_encode($row);

mysql_close($con);


?>