<?php

require('fpdf183/fpdf.php');
require('includes/config.php');
class PDF extends FPDF
{
// Page header
function Header()
{
    // Logo
    $this->Image('assets/images/quadskate.jpg',10,10,25);

         $this->SetDrawColor(0,80,180);
    $this->SetFillColor(230,230,0);
    $this->SetTextColor(220,50,50);
    $this->SetFont('Arial','I',13);
    // Move to the right
    $this->Cell(80);
    // Title
    $this->Cell(80,10,'Approved Customers',1,0,'C');
    // Line break
    $this->Ln(20);

}
 
// Page footer
function Footer()
{
    // Position at 1.5 cm from bottom
    $this->SetY(-15);
    // Arial italic 8
    $this->SetFont('Arial','I',8);
    // Page number
    $this->Cell(0,10,'Page '.$this->PageNo().'/{nb}',0,0,'C');
}

 
// $db = new dbObj();
// $connString =  $db->getConnstring();
}
$display_heading = array('id'=>'ID', 'email'=> 'Email',  'fname'=> 'First name', 'sname'=> 'Second name','gender'=> 'Gender','phone'=> 'Phone','status'=> 'Status');
 
$result = mysqli_query($connect, "SELECT `id`, `email`, `fname`, `sname`, `gender`, `phone`, `status` FROM `tbl_customer` WHERE status='1'") or die("Location: index.php");
// SHOW COLUMNS FROM student_info LIKE 's%'; 
;
$header = mysqli_query($connect, "SHOW COLUMNS FROM tbl_customer
WHERE field = 'id' or field = 'email 'or field = 'fname 'or field = 'sname 'or field = 'gender 'or field = 'phone 'or field = 'status'");
 
$pdf = new PDF();
//header
$pdf->AddPage();
//foter page
$pdf->AliasNbPages();
$pdf->SetFont('Arial','B',6);
foreach($header as $heading) {
$pdf->Cell(22,8,$display_heading[$heading['Field']],1);
}
foreach($result as $row) {
$pdf->Ln();
foreach($row as $column)
$pdf->Cell(22,8,$column,1);
}
$pdf->Output();



?>