<?php
    //phpinfo();

	// Turn off error reporting
    //error_reporting(1);
    
    
	//web API path
	//eg http://domain.com/API/

	//$API_path="http://incampus.co.in/api/";
	//$API_path = "https://myottappbucket.s3.ap-south-1.amazonaws.com/StarYaar/";
	$API_path = "https://d1h3jj97p32kma.cloudfront.net/StarYaar";
	//this is firebase server key to send push notications
	
	// check how you can get firebase server key  https://i.gyazo.com/7c3f23a30c14d3008533605a9821f944.png
	define("firebase_key","AAAAMoFhBz0:APA91bEhhjYwOiC7boUHDKOW4jC6bIgoGuCPP73YJdUdWw3iqdOHZ3B_OSOZiNsYWPk859-NY1qZzEHh2j8H_P8VbmwHayd61R5q_NSdXvFHiu95uJrBDiwMULMCk72McW9Hp5CH83MI");
    
    
	
	//database configration
	$servername = "localhost";
	$database = "mystaryaardb";//"bring_tictic";
	$username = "pankaj";
	$password = "qbolbk";
    
	// Create connection

	$conn = mysqli_connect($servername, $username, $password, $database);
	mysqli_query($conn,"SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION'");


	// run the sample query 
	//$query=mysqli_query($conn,"select * from users ");
	//$rd=mysqli_fetch_object($query);
	//print_r('data '.$rd->first_name);

	// Check connection

	if (!$conn) {

	    die("Connection failed: " . mysqli_connect_error());

	}
    
	
?>
