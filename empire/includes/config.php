<?php

    //database configuration
    $host       = "localhost";
    $user       = "root";
    $pass       = "";
    $database   = "skaters";
    $connect = new mysqli($host, $user, $pass, $database);

   
  /*   if (!$connect) {
        die ("connection failed: " . mysqli_connect_error());
    } else {
        $connect->set_charset('utf8');
        //echo "connected";
    }
 */
?>