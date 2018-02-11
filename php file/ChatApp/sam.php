
		
<?php

 
require_once('dbconnection.php');
 
  $array=mysql_query("SELECT contactid FROM addcontact WHERE userid= '4' ") or die(mysql_error());
		$res = mysqli_query($con,$array);
		//$result = mysql_query("select name from account where id in (".implode(',',$array).") ") or die(mysql_error());
		
		echo $res;
 mysqli_close($con);
 ?>