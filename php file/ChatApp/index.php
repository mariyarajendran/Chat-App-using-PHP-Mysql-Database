<?php

//Turn off Error Reporting
error_reporting(0);

	if (isset($_POST['tag']) && $_POST['tag'] != '') 
	{
		// get tag
		$tag = $_POST['tag'];
	
		
	
		
		// Request type is check Login
		$nam = $_POST['name'];
		$phon= $_POST['phonenumber'];
		$user = $_POST['username'];
		$pass = $_POST['password'];
		$pho = $_POST['phone'];
		$userid=$_POST['userid'];
			$loginid=$_POST['id'];
		$masterslaveid=$_POST['masterslaveid'];
		$messagestore=$_POST['message'];
		$totaluniqueid=$_POST['total'];
		
		$loadmaster=$_POST['loadmaster'];
		$loadslave=$_POST['loadslave'];
		$loadmsgtotal=$_POST['loadmsgtotal'];

		// response Array
		$response = array("tag" => $tag);
		
		// Connecting to mysql database
        $con = mysql_connect("localhost", "root", "") or die(mysql_error()); //server,user,password
 
        // Selecing database
        $db = mysql_select_db("chatapp") or die(mysql_error()) or die(mysql_error());
		
		
		
		if ($tag == 'login') 
		{			
			$result = mysql_query("SELECT * FROM account WHERE username = '$user' AND password ='$pass' ") or die(mysql_error());
			
			// check for result 
			$no_of_rows = mysql_num_rows($result);
			
			 if ($no_of_rows > 0) 
			 {
			$ele = mysql_fetch_array($result);
			 $response["id"]=$ele['id'];
			 
			 
					$response['error'] = 0;
				$response["result"]="Login Successfully";
				 
				/* $conid = mysql_query("SELECT contactid FROM addcontact WHERE userid In (4,3)  ") or die(mysql_error());
				 $con=mysql_fetch_array($conid);
				 
				
				// $r = mysql_query("SELECT name FROM `account WHERE id ='$con'") or die(mysql_error());
				 // $con=mysql_fetch_array($r);
				 $response["result"]=$con['contactid'];*/
				 
				 
				 echo json_encode($response);
			 } 
			 else 
			 {
				// user not found
			//	$response['error'] = 1;
				$response["result"]="Username or Password Is Wrong!!!";
				echo json_encode($response);
			 }
				 
		}
		
		
		
		else if ($tag == 'insert') 
		{

		
				$result = mysql_query("INSERT INTO account(name,phoneno,username,password) VALUES('$nam','$phon','$user','$pass')");
			
			 if ($result) 
			 {
				 $response["result"]="Successfully Inserted.";
				 echo json_encode($response);
				 $response['error'] = 0;
			 } 
			 else 
			 {
				// unable to insert
				$response["result"]="Unable To Insert";
				echo json_encode($response);
				$response['error'] = 1;
			 }
			 
			
		}
		
		else if ($tag == 'contact') 
		{			
			$result = mysql_query("SELECT * FROM account WHERE   phoneno ='$pho' ") or die(mysql_error());
			
			// check for result 
			$no_of_rows = mysql_num_rows($result);
			
			 if ($no_of_rows > 0) 
			 {
			
				//	$response['error'] = 0;
				$ele = mysql_fetch_array($result);
				// $response["result"]=$ele['id'];
				 $response["result"]=$ele['phoneno'];
				 $phone=$ele['phoneno'];
				 $id=$ele['id'];
				 $name=$ele['name'];
				 $res = mysql_query("INSERT INTO addcontact(userid,phoneno,contactid,contactname) VALUES('$userid','$phone','$id','$name')");
				 
				 echo json_encode($response);
			 } 
			 else 
			 {
				// user not found
			//	$response['error'] = 1;
				$response["result"]="No Record Found";
				echo json_encode($response);
			 }
				 
		}
		
		
			else if ($tag == 'privatecontact') 
		{			
		require_once('dbconnection.php');
		
		$sql = "SELECT phoneno,contactid,contactname from addcontact WHERE userid='$loginid'";
		
		$result = mysqli_query($con,$sql);
			
 $res = array();
 
              while($row = mysqli_fetch_array($result)){
                 array_push($res,array(
				      
                      
					    'phoneno'=>$row['phoneno'],
						 'contactid'=>$row['contactid'],
					   'contactname'=>$row['contactname'],
					  
					   ));
					   
					   }
                 
 
 echo json_encode(array("res"=>$res));
 
mysqli_close($con);
		
				
		}
		
		
		/*else if ($tag == 'loadmessage') 
		{			
		require_once('dbconnection.php');
		
	$sql = "SELECT message_text from message WHERE message_fromto='$loadmaster'";
		
		
		
		$result = mysqli_query($con,$sql);
			
			
 $res = array();
 
 
 
              while($row = mysqli_fetch_array($result)){
                 array_push($res,array(
				      
					    'message_fromto'=>$row['message_text'],
										  
					   ));
					   
					   }
					   
					   $sqltwo = "SELECT message_text from message WHERE message_fromto='$loadslave'";
					   
					   $resulttwo = mysqli_query($con,$sqltwo);
					    $restwo = array();
						
					    while($rowtwo = mysqli_fetch_array($resulttwo)){
                 array_push($restwo,array(
				      
					    'message_fromto'=>$rowtwo['message_text'],
										  
					   ));
					   
					   }
					   
				$response["res"]=array($res);
				$response["restwo"]=array($restwo);
 echo json_encode($response);
 
mysqli_close($con);
		
				
		}*/
		
		else if ($tag == 'loadmessage') 
		{			
		require_once('dbconnection.php');
		
	$sql = "SELECT * from message WHERE totalidunique='$loadmsgtotal' and status='read' order by time asc";
		
		$result = mysqli_query($con,$sql);
			
 $res = array();
 
              while($row = mysqli_fetch_array($result)){
                 array_push($res,array(
				      
					    'message_text'=>$row['message_text'],
						'message_fromto'=>$row['message_fromto'],
										  
					   ));
					   
					   }
				   
			 echo json_encode(array("res"=>$res));
 
mysqli_close($con);
		
				
		}
		
		
		else if ($tag == 'loadmessagefrequently') 
		{			
		require_once('dbconnection.php');
		
	$sql = "SELECT * from message WHERE totalidunique='$loadmsgtotal' and status='unread' order by time asc";
		
		$result = mysqli_query($con,$sql);
			
 $res = array();
 
              while($row = mysqli_fetch_array($result)){
                 array_push($res,array(
				      
					    'message_text'=>$row['message_text'],
						'message_fromto'=>$row['message_fromto'],
						'status'=>$row['status']
										  
					   ));
					   
					   }
					    $up = "UPDATE  message SET status='read' WHERE totalidunique='$loadmsgtotal' and status='unread'";
					  $ree = mysqli_query($con,$up);

					  
				   
			 echo json_encode(array("res"=>$res));
 
mysqli_close($con);
		
				
		}
		
		
		
		else if ($tag == 'sendmessage') 
		{

		
				$result = mysql_query("INSERT INTO message(message_fromto,message_text,totalidunique,status) VALUES('$masterslaveid','$messagestore','$totaluniqueid','unread')");
			
			 if ($result) 
			 {
				 $response["result"]="Successfully Inserted.";
				 echo json_encode($response);
				 $response['error'] = 0;
			 } 
			 else 
			 {
				// unable to insert
				$response["result"]="Unable To Insert";
				echo json_encode($response);
				$response['error'] = 1;
			 }
			 
			
		}
		
		
		
		
		
		else 
		{
			echo "Invalid Request";
		}	
	} 
	else 
	{
    echo "Access Denied By Me";
	}

?>