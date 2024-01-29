<?php

$con = mysqli_connect('localhost', 'root', '','skaters');

// get the post records
$name = $_POST['name'];
$user = $_POST['user'];
$email = $_POST['email'];
$password = $_POST['password'];
$status = $_POST['status'];


 $sql = "INSERT INTO tbl_inventory (`user`, `name`, `email`, `password`, `status`)
  VALUES ('$user','$name','$email','$password','$status')";


$rs = mysqli_query($con, $sql);

if($rs)
{
	echo "Contact Records Inserted";
}

?>