<?php

$con = mysqli_connect('localhost', 'root', '','skaters');

// get the post records
$name = $_POST['name'];
$user = $_POST['user'];
$email = $_POST['email'];
$password = $_POST['password'];
$status = $_POST['status'];


$sqlCheckEmail = "SELECT * FROM tbl_customercare WHERE email LIKE '$email'";
$email_query = mysqli_query($con, $sqlCheckEmail);
 if(mysqli_num_rows($email_query) > 0){
  echo "User  already used type another one";
 }
  else{
     $sql = "INSERT INTO tbl_customercare (`user`, `name`, `email`, `password`, `status`)
  VALUES ('$user','$name','$email','$password','$status')";


$rs = mysqli_query($con, $sql);

if($rs)
{
    echo " Records Inserted";
}
  }

?>