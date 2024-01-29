<?php
  include_once('includes/config.php');
$username = $_POST["username"];
$sname = $_POST["secondname"];
$email = $_POST["email"];
$password = $_POST["psw"];
$mobile = $_POST["mobile"];
$gender = $_POST["gender"];
$License= $_POST["dLicense"];
//$username = "Abdo"; $email = "abdelhamid@yahoo.com"; $password = "123456"; $mobile = "01222225522"; $gender = "Male";/
$isValidEMail = filter_var($email , FILTER_VALIDATE_EMAIL);
if($connect){
if(strlen($password ) > 40 || strlen($password ) < 6){
echo "Password length must be more than 6 and less than 40";
}
else if($isValidEMail === false){
echo "This Email is not valid";
}
else{
$sqlCheckUname = "SELECT * FROM driver WHERE user_name LIKE '$username'";
$u_name_query = mysqli_query($connect, $sqlCheckUname);
$sqlCheckEmail = "SELECT * FROM driver WHERE email LIKE '$email'";
$email_query = mysqli_query($connect, $sqlCheckEmail);
//if(mysqli_num_rows($u_name_query) > 0){
//
//}else 
if(mysqli_num_rows($email_query) > 0){
  echo "User already exists";
}
  else{
    $sql_register = "INSERT INTO driver (`user_name`, `secName`, `password`, `dLicense`, `gender`, `email`, `mobile`) VALUES ('$username','$sname','$password','$License','$gender','$email','$mobile')";
if(mysqli_query($connect,$sql_register)){
echo "You are registered successfully";
}else{
echo "Failed to register you account";
}}


}
}
else{
echo "Connection Error";
}
?>