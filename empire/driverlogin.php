<?php
 
  if($_SERVER['REQUEST_METHOD']=='POST'){

	include_once('includes/config.php');
 

 
 $email = $_POST['email'];
 $password = $_POST['psw'];




 $Sql_Query = "select * from driver where email = '$email' and password = '$password' and status='1'";
 
 $check = mysqli_fetch_array(mysqli_query($connect,$Sql_Query));

 
  if(isset($check)){

 
 echo "Driver Login";
 }
 else{
 echo "Not approved yet!";
 }
 
 }else{
 echo "Check Again";
 }


?>


