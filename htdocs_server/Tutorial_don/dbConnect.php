<?php
error_reporting(E_ALL ^ E_DEPRECATED);


function getConnection()
  {

   $con = mysql_connect("localhost","root","password");PASSWORD here
     return $con;
}


?>