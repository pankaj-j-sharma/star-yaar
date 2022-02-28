<?php
	
		
	if(isset($_GET["p"]))
	{

		 if($_GET["p"]=="getDirContents")
		 {     
		        require_once("uploadtos3.php");
                        getDirContents('/home/ubuntu/php/upload/');
                }

		
		if($_GET["p"]=="uploadVideo")
		{
			uploadVideoEnhanced();
		}
		else
		if($_GET["p"]=="gifupload")
		{
			gifupload();
		}
		
		else
		if($_GET["p"]=="signup")
		{
			signup();
		}
		else
		if($_GET["p"]=="showAllVideos")
		{
			showAllVideos();
		}
		else
		if($_GET["p"]=="showMyAllVideos")
		{
			showMyAllVideos();
		}
		else
		if($_GET["p"]=="likeDislikeVideo")
		{
			likeDislikeVideo();
		}
		else
		if($_GET["p"]=="postComment")
		{
			postComment();
		}
		else
		if($_GET["p"]=="showVideoComments")
		{
			showVideoComments();
		}
		else
		if($_GET["p"]=="updateVideoView")
		{
			updateVideoView();
		}
		else
		if($_GET["p"]=="allSounds")
		{
			allSounds();
		}
		else
		if($_GET["p"]=="fav_sound")
		{
			fav_sound();
		}
		else
		if($_GET["p"]=="my_FavSound")
		{
			my_FavSound();
		}
		else
		if($_GET["p"]=="my_liked_video")
		{
			my_liked_video();
		}
		else
		if($_GET["p"]=="discover")
		{
			discover();
		}
		else
		if($_GET["p"]=="edit_profile")
		{
			edit_profile();
		}
		else
		if($_GET["p"]=="follow_users")
		{
			follow_users();
		}
		else
		if($_GET["p"]=="get_user_data")
		{
			get_user_data();
		}
		else
		if($_GET["p"]=="get_followers")
		{
			get_followers();
		}
		else
		if($_GET["p"]=="get_followings")
		{
			get_followings();
		}
		else
		if($_GET["p"]=="SearchByHashTag")
		{
			SearchByHashTag();
		}
        else
		if($_GET["p"]=="DeleteSound")
		{
			DeleteSound();
		}
		else
		if($_GET["p"]=="DeleteVideo")
		{
			DeleteVideo();
		}
    	else
    	if($_GET["p"]=="sendPushNotification")
		{
			sendPushNotification();
		}
		else
    	if($_GET["p"]=="uploadImage")
		{
			uploadImage();
		}
		else
    	if($_GET["p"]=="editSoundSection")
		{
			editSoundSection();
		}
    	
		
		
		
		
		//admin panel functions
		else
		if($_GET["p"]=="Admin_Login")
		{
			Admin_Login();
		}
		else
		if($_GET["p"]=="All_Users")
		{
			All_Users();
		}
		else
		if($_GET["p"]=="admin_all_sounds")
		{
			admin_all_sounds();
		}
		else
		if($_GET["p"]=="admin_uploadSound")
		{
			admin_uploadSound();
		}
		else
		if($_GET["p"]=="admin_getSoundSection")
		{
			admin_getSoundSection();
		}
		else
		if($_GET["p"]=="admin_show_allVideos")
		{
			admin_show_allVideos();
		}
		else
		if($_GET["p"]=="admin_addSection")
		{
			admin_addSection();
		}
		else
		if($_GET["p"]=="DeleteSoundSectino")
		{
			DeleteSoundSectino();
		}
		else
		if($_GET["p"]=="all_discovery_sections")
		{
			all_discovery_sections();
		}
		else
		if($_GET["p"]=="deleteSection")
		{
			deleteSection();
		}
		else
		if($_GET["p"]=="assignSection")
		{
			assignSection();
		}
		else
		if($_GET["p"]=="blockUser")
		{
			blockUser();
		}
		else
		if($_GET["p"]=="getSingleSectionDetails")
		{
			getSingleSectionDetails();
		}
		else
		if($_GET["p"]=="addVideointoDiscovry")
		{
			addVideointoDiscovry();
		}
		
		
	
	
	}
	else
	{
		echo"Not Found";

	}

	// read files in the directory reecursively
	function getDirContents($dir, &$results = array()) {

    		$files = scandir($dir);

    		foreach ($files as $key => $value) {
        		$path = realpath($dir . DIRECTORY_SEPARATOR . $value);
        		if (!is_dir($path)) {
				$results[] = $path;
				echo(str_replace("/home/ubuntu/php/upload/","",$path)."\n");
				$file_raw=fopen($path,'rb');
				uploadToStorage(str_replace("/home/ubuntu/php/upload/","",$path),$file_raw);
				//break;

        		} else if ($value != "." && $value != "..") {
            			getDirContents($path, $results);
				$results[] = $path;
				echo(str_replace("/home/ubuntu/php/upload/","",$path)."\n");
                                $file_raw=fopen($path,'rb');
                                uploadToStorage(str_replace("/home/ubuntu/php/upload/","",$path),$file_raw);
				//break;
       		 	}
   		 }
    		return $results;
	}



	function sendPushNotificationToMobileDevice($data)
	{
        require_once("config.php");
        $key=firebase_key;
      
        $curl = curl_init();

        curl_setopt_array($curl, array(
            CURLOPT_URL => "https://fcm.googleapis.com/fcm/send",
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "POST",
            CURLOPT_POSTFIELDS => $data,
            CURLOPT_HTTPHEADER => array(
                "authorization: key=".$key."",
                "cache-control: no-cache",
                "content-type: application/json",
                "postman-token: 85f96364-bf24-d01e-3805-bccf838ef837"
            ),
        ));

        $response = curl_exec($curl);
        $err = curl_error($curl);

        curl_close($curl);

        if ($err) 
        {
           // print_r($err);
        } 
        else 
        {
            //print_r($response);
        }

    }
	
	
    			
    			
	function signup()
	{
		require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['fb_id']) && isset($event_json['first_name']) && isset($event_json['last_name']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$first_name=htmlspecialchars(strip_tags($event_json['first_name'] , ENT_QUOTES));
			$last_name=htmlspecialchars(strip_tags($event_json['last_name'] , ENT_QUOTES));
			$gender=htmlspecialchars(strip_tags($event_json['gender'] , ENT_QUOTES));
			$profile_pic=htmlspecialchars_decode(stripslashes($event_json['profile_pic']));
			$version=htmlspecialchars_decode(stripslashes($event_json['version']));
			$device=htmlspecialchars_decode(stripslashes($event_json['device']));
			$signup_type=htmlspecialchars_decode(stripslashes($event_json['signup_type']));
		    $username=$first_name.rand();
		    
			$log_in="select * from users where fb_id='".$fb_id."' ";
			$log_in_rs=mysqli_query($conn,$log_in);
			
			if(mysqli_num_rows($log_in_rs))
			{   
			    $rd=mysqli_fetch_object($log_in_rs);  
			     
				if($rd->block=='0')
				{
					$array_out = array();
					 $array_out[] = 
						//array("code" => "200");
						array(
							"fb_id" => $rd->fb_id,
							"action" => "login",
							"profile_pic" => $rd->profile_pic,
							"first_name" => $rd->first_name,
							"last_name" => $rd->last_name,
							"username" => $rd->username,
							"bio" => $rd->bio,
							"gender" => $rd->gender
						);
					
					$output=array( "code" => "200", "msg" => $array_out);
					print_r(json_encode($output, true));
				}
				else
				{
					$array_out = "error in login";
					$output=array( "code" => "201", "msg" => $array_out);
					print_r(json_encode($output, true));
				}
				
			}	
			else
			{
			    
			    $qrry_1="insert into users(fb_id,username,first_name,last_name,profile_pic,version,device,signup_type,gender)values(";
    			$qrry_1.="'".$fb_id."',";
    			$qrry_1.="'".$username."',";
    			$qrry_1.="'".$first_name."',";
    			$qrry_1.="'".$last_name."',";
    			$qrry_1.="'".$profile_pic."',";
    			$qrry_1.="'".$version."',";
    			$qrry_1.="'".$device."',";
    			$qrry_1.="'".$signup_type."',";
    			$qrry_1.="'".$gender."'";
    			$qrry_1.=")";
    			if(mysqli_query($conn,$qrry_1))
    			{
				     $last_insert_fb_id = mysqli_insert_id($conn);
				     
				     $array_out = array();
    				 $array_out[] = 
    					//array("code" => "200");
    					array(
    						"fb_id" => $fb_id,
    						"username" => $username,
    						"action" => "signup",
    						"profile_pic" => $profile_pic,
    						"username" => $username,
    						"first_name" => $first_name,
    						"last_name" => $last_name,
    						"signup_type" => $signup_type,
    						"gender" => $gender
    					);
    				
    				$output=array( "code" => "200", "msg" => $array_out);
    				print_r(json_encode($output, true));
    			}
    			else
    			{
    			    //echo mysqli_error();
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"problem in signup");
            		
            		$output=array( "code" => "201", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
			}
			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function uploadImage()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		//0= owner  1= company 2= ind mechanic
		
		if(isset($event_json['fb_id']) && isset($event_json['image_link']) )
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$image_link=stripslashes(strip_tags($event_json['image_link']));
		
			
			$qrry_1="select * from users WHERE fb_id ='".$fb_id."' ";
			$log_in_rs=mysqli_query($conn,$qrry_1);
			
			if(mysqli_num_rows($log_in_rs))
			{
			    $rd=mysqli_fetch_object($log_in_rs);
			   	
			    $qrry_1="update users SET profile_pic ='".$image_link."' WHERE fb_id ='".$fb_id."' ";
    			if(mysqli_query($conn,$qrry_1))
    			{
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"success");
            		
            		$output=array( "code" => "200", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
    			else
    			{
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"problem in uploading");
            		
            		$output=array( "code" => "201", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
			}   
	    	
			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function gifupload()
	{
	            //giffimage
	            
				require_once("config.php");
        		$input = file_get_contents("php://input");
        	    $event_json = json_decode($input,true);
        	   
        	    //print_r($event_json);
        	    $gif1 = $event_json['giffimage']['file_data'];
        	    
        	    
        	    $event_json['giffimage'];
        	    //print_r($event_json['fb_id']);
        	    
        	    $gif = base64_decode($gif1);
			    
			    
			      $fileName="hamza".rand();
			 
			
		    	file_put_contents("upload/gif/".$fileName.".gif", $gif);
			    
			


	}
	
	function uploadVideo()
	{
		require_once("config.php"); 
		//require_once("uploadtos3.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['fb_id']) && isset($event_json['picbase64'])  && isset($event_json['videobase64']))
		{   
		    $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		    $description=htmlspecialchars(strip_tags($event_json['description'] , ENT_QUOTES));
		    $sound_id=htmlspecialchars(strip_tags($event_json['sound_id'] , ENT_QUOTES));
		    $thum = $event_json['picbase64']['file_data'];
		    $video = $event_json['videobase64']['file_data'];
		    $gif = $event_json['gifbase64']['file_data'];
            
            $fileName=rand()."_".rand();
			$video_url="upload/video/".$fileName.".mp4";
			$thum_url="upload/thum/".$fileName.".jpg";
			$gif_url="upload/gif/".$fileName.".gif";

			/*list($type, $data) = explode(',', $data);
			list(, $data)      = explode(',', $data);*/
			$thum = base64_decode($thum);
			//uploadToStorage($fileName,$thum);

			file_put_contents("upload/thum/".$fileName.".jpg", $thum);
			
			/*picture resize*/
				// File and new size
				$filename = 'upload/thum/'.$fileName.'.jpg';
				$newfilename='upload/thum/'.$fileName.'.jpg';
				$percent = 0.4;
				
				// Get new sizes
				list($width, $height) = getimagesize($filename);
				$newwidth = $width * $percent;
				$newheight = $height * $percent;
				// Load
				$thumb = imagecreatetruecolor($newwidth, $newheight);
				$source = imagecreatefromjpeg($filename);
				// Resize
				$res=imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);
				// Output
				imagejpeg($thumb,$newfilename);
			/*picture resize*/
			
			
			$video = base64_decode($video);
			
			file_put_contents("upload/video/".$fileName.".mp4", $video);
			
			/*video upload*/
				// File and new size
				$filename = 'upload/video/'.$fileName.'.mp4';
				$newfilename='upload/video/'.$fileName.'.mp4';
				$percent = 0.2;
				// Get new sizes
				list($width, $height) = getimagesize($filename);
				$newwidth = $width * $percent;
				$newheight = $height * $percent;
				// Load
				$thumb = imagecreatetruecolor($newwidth, $newheight);
				$source = imagecreatefromjpeg($filename);
				// Resize
				$res=imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);
				// Output
				imagejpeg($thumb,$newfilename);
				
			/*video upload*/
			
			
			$gif = base64_decode($gif);
			
			$filename = 'upload/gif/'.$fileName.'.mp4';
			
			file_put_contents("upload/gif/".$fileName.".gif", $gif);
			
		
			
			$qrry_1="insert into videos(description,video,sound_id,fb_id,gif,thum)values(";
			$qrry_1.="'".$description."',";
			$qrry_1.="'".$video_url."',";
			$qrry_1.="'".$sound_id."',";
			$qrry_1.="'".$fb_id."',";
			$qrry_1.="'".$gif_url."',";
			$qrry_1.="'".$thum_url."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			   $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "file uploaded"
    				);
    			
    			$output=array( "code" => "200", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
			else
			{
			    $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "error in uploading files"
    				);
    			
    			$output=array( "code" => "201", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
			
			
			
			
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	
	}
	
	function showAllVideos()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$token=$event_json['token'];
			
			@mysqli_query($conn,"update users set tokon='".$token."' where fb_id='".$fb_id."' ");
			
			
			if($event_json['video_id'])
			{
			    $query=mysqli_query($conn,"select * from videos where id='".$event_json['video_id']."' ");
			}
			else
			{
			    $query=mysqli_query($conn,"select * from videos order by rand() ");
			}
			
		        
    		$array_out = array();
    		while($row=mysqli_fetch_array($query))
    		{
    		    
    		    $query1=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
		        $rd=mysqli_fetch_object($query1);
		       
		        $query112=mysqli_query($conn,"select * from sound where id='".$row['sound_id']."' ");
		        $rd12=mysqli_fetch_object($query112);
		        
		        $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' ");
                $countLikes_count=mysqli_fetch_assoc($countLikes);
		        
		        $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row['id']."' ");
                $countcomment_count=mysqli_fetch_assoc($countcomment);
                
                
                $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' and fb_id='".$fb_id."' ");
                $liked_count=mysqli_fetch_assoc($liked);
		        
        	   	$array_out[] = 
        			array(
        			"id" => $row['id'],
        			"fb_id" => $row['fb_id'],
        			"user_info" =>array
            					(
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic
            					),
            		"count" =>array
            					(
            					    "like_count" => $countLikes_count['count'],
                        			"video_comment_count" => $countcomment_count['count']
            					),
            		"liked"=> $liked_count['count'],			
            	    "video" => $row['video'],
        			"thum" => $row['thum'],
        			"gif" => $row['gif'],
        			"description" => $row['description'],
        			"sound" =>array
            					(
            					    "id" => $rd12->id,
            					    "audio_path" => 
                            			array(
                                			"mp3" => $API_path."/upload/audio/".$rd12->id.".mp3",
                    			            "acc" => $API_path."/upload/audio/".$rd12->id.".aac"
                                		),
                        			"sound_name" => $rd12->sound_name,
                        			"description" => $rd12->description,
                        			"thum" => $rd12->thum,
                        			"section" => $rd12->section,
                        			"created" => $rd12->created,
            					),
        			"created" => $row['created']
        		);
    			
    		}
    		$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function SearchByHashTag()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$tag=htmlspecialchars(strip_tags($event_json['tag'] , ENT_QUOTES));
			$token=$event_json['token'];
			
			@mysqli_query($conn,"update users set tokon='".$token."' where fb_id='".$fb_id."' ");
			
			$query=mysqli_query($conn,"select * from videos where description like '%".$tag."%' order by rand() ");
		        
    		$array_out = array();
    		while($row=mysqli_fetch_array($query))
    		{
    		    
    		    $query1=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
		        $rd=mysqli_fetch_object($query1);
		       
		        $query112=mysqli_query($conn,"select * from sound where id='".$row['sound_id']."' ");
		        $rd12=mysqli_fetch_object($query112);
		        
		        $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' ");
                $countLikes_count=mysqli_fetch_assoc($countLikes);
		        
		        $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row['id']."' ");
                $countcomment_count=mysqli_fetch_assoc($countcomment);
                
                
                $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' and fb_id='".$fb_id."' ");
                $liked_count=mysqli_fetch_assoc($liked);
		        
        	   	$array_out[] = 
        			array(
        			"id" => $row['id'],
        			"fb_id" => $row['fb_id'],
        			"user_info" =>array
            					(
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic
            					),
            		"count" =>array
            					(
            					    "like_count" => $countLikes_count['count'],
                        			"video_comment_count" => $countcomment_count['count'],
                        			"view" => $row['view'],
            					),
            		"liked"=> $liked_count['count'],			
            	    "video" => $row['video'],
        			"thum" => $row['thum'],
        			"gif" => $row['gif'],
        			"description" => $row['description'],
        			"sound" =>array
            					(
            					    "id" => $rd12->id,
            					    "audio_path" => 
                            			array(
                                			"mp3" => $API_path."/upload/audio/".$rd12->id.".mp3",
                    			            "acc" => $API_path."/upload/audio/".$rd12->id.".aac"
                                		),
                        			"sound_name" => $rd12->sound_name,
                        			"description" => $rd12->description,
                        			"thum" => $rd12->thum,
                        			"section" => $rd12->section,
                        			"created" => $rd12->created,
            					),
        			"created" => $row['created']
        		);
    			
    		}
    		$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function showMyAllVideos()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']) && isset($event_json['my_fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$my_fb_id=htmlspecialchars(strip_tags($event_json['my_fb_id'] , ENT_QUOTES));
			
		    $query1=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
		    $rd=mysqli_fetch_object($query1);
		    if(mysqli_num_rows($query1))
		    {
		        
		        $query=mysqli_query($conn,"select * from videos where fb_id='".$fb_id."' order by id DESC");
		        
		        $array_out_video = array();
        		while($row=mysqli_fetch_array($query))
        		{
        		  
        		   $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' ");
                   $countLikes_count=mysqli_fetch_assoc($countLikes);
    		       
    		       $query112=mysqli_query($conn,"select * from sound where id='".$row['sound_id']."' ");
		           $rd12=mysqli_fetch_object($query112);
		        
    		       $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row['id']."' ");
                   $countcomment_count=mysqli_fetch_assoc($countcomment);
                   
                   $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' and fb_id='".$fb_id."' ");
                   $liked_count=mysqli_fetch_assoc($liked);
                
        		   $array_out_video[] = 
            			array(
            			"id" => $row['id'],
            			"video" => $row['video'],
            			"thum" => $row['thum'],
            			"gif" => $row['gif']."?time=".rand(),
            			"description" => $row['description'],
            			"liked" => $liked_count['count'],
            			"count" =>array
            					(
            					    "like_count" => $countLikes_count['count'],
                        			"video_comment_count" => $countcomment_count['count'],
                        			"view" => $row['view'],
            					),
    					"sound" =>array
            					(
            					    "id" => $rd12->id,
            					    "audio_path" => 
                            			array(
                                			"mp3" => $API_path."/upload/audio/".$rd12->id.".mp3",
                    			            "acc" => $API_path."/upload/audio/".$rd12->id.".aac"
                                		),
                        			"sound_name" => $rd12->sound_name,
                        			"description" => $rd12->description,
                        			"thum" => $rd12->thum,
                        			"section" => $rd12->section,
                        			"created" => $rd12->created,
            					),
            			"created" => $row['created']
            		);
        			
        		}
		        
		        
		        //count total heart
		            $query123=mysqli_query($conn,"select * from videos where fb_id='".$fb_id."' ");
		        
    		        $array_out_count_heart ="";
            		while($row123=mysqli_fetch_array($query123))
            		{
            		  	$array_out_count_heart .=$row123['id'].',';
            		}
            		
            		$array_out_count_heart=$array_out_count_heart.'0';
            		
            		$hear_count=mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id IN($array_out_count_heart) ");
		            $hear_count=mysqli_fetch_assoc($hear_count);
		            
		        //count total heart
		        
		        //count total_fans
		        
		            $total_fans=mysqli_query($conn,"SELECT count(*) as count from follow_users where followed_fb_id='".$fb_id."' ");
		            $total_fans=mysqli_fetch_assoc($total_fans);
		            
		        //count total_fans
		        
		        //count total_following
		        
		            $total_following=mysqli_query($conn,"SELECT count(*) as count from follow_users where fb_id='".$fb_id."' ");
		            $total_following=mysqli_fetch_assoc($total_following);
		            
		        //count total_following
		        
		        
		        $count_video_rows=count($array_out_video);
		        if($count_video_rows=="0")
		        {
		            $array_out_video=array(0);
		        }
		        
		        
                $query2=mysqli_query($conn,"SELECT count(*) as count from follow_users where fb_id='".$my_fb_id."' and followed_fb_id='".$fb_id."' ");
		        $follow_count=mysqli_fetch_assoc($query2);
		        
		       
                if($follow_count['count']=="0")
                {
                    $follow="0";
                    $follow_button_status="Follow";
                }
                else
                if($follow_count['count']!="0")
                {
                    $follow="1";
                    $follow_button_status="Unfollow";
                }
                
                
		        $array_out = array();
		        $array_out[] = 
        			array(
        			"fb_id" => $fb_id,
        			"user_info" =>array
            					(   
            					    "fb_id" => $rd->fb_id,
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic,
                        			"gender" => $rd->gender,
        	                		"created" => $rd->created,
            					),
        			"follow_Status" =>array
            					(
            					    "follow" => $follow,
                        			"follow_status_button" => $follow_button_status
            					),
        			"total_heart" => $hear_count['count'],
        			"total_fans" => $total_fans['count'],
        			"total_following" => $total_following['count'],
        			"user_videos" => $array_out_video
            	    
        		);
		        
		      
		    } 
		    
		    $output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function updateVideoView()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['id']))
		{
			$id=htmlspecialchars(strip_tags($event_json['id'] , ENT_QUOTES));
		       
		    mysqli_query($conn,"update videos SET view =view+1 WHERE id ='".$id."' ");
    		
    		$array_out[] = 
				array(
				"response" =>"success");
				
		    $output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			$array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	    
	}
	
	
	
	
	function likeDislikeVideo()
	{
	    require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['fb_id']) && isset($event_json['video_id']) )
		{   
		    $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		    $video_id=htmlspecialchars(strip_tags($event_json['video_id'] , ENT_QUOTES));
		    $action=htmlspecialchars(strip_tags($event_json['action'] , ENT_QUOTES));
		   
		    if($action=="0")
		    {
		        mysqli_query($conn,"Delete from video_like_dislike where video_id ='".$video_id."' ");
	    
        	    $array_out = array();
        					
        			 $array_out[] = 
        				array(
        				"response" =>"video unlike");
        	        
            	$output=array( "code" => "200", "msg" => $array_out);
        		print_r(json_encode($output, true));
		    }
		    else
		    {
		        $qrry_1="insert into video_like_dislike(video_id,fb_id,action)values(";
    			$qrry_1.="'".$video_id."',";
    			$qrry_1.="'".$fb_id."',";
    			$qrry_1.="'".$action."'";
    			$qrry_1.=")";
    			if(mysqli_query($conn,$qrry_1))
    			{
    			   $array_out = array();
        			$array_out[] = 
        				array(
        					"response" => "actions success"
        				);
        			
        			$output=array( "code" => "200", "msg" => $array_out);
        			print_r(json_encode($output, true)); 
        			
        			
        			//push notification
    			        
    			        //fetch user tokon 
    			        
        			        $query1=mysqli_query($conn,"select * from videos where id='".$video_id."' ");
    		                $rd=mysqli_fetch_object($query1);
    		                
    		                $query11=mysqli_query($conn,"select * from users where fb_id='".$rd->fb_id."' ");
    		                $rd1=mysqli_fetch_object($query11);
		                
		                //fetch user tokon 
		                
		                //fetch name of a person who is liking other person video
    		                $query111=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
    		                $rd11=mysqli_fetch_object($query111);
    		                
    		            //fetch name of a person who is liking other person video
		                
        			    $title=$rd11->first_name." Liked Your Video";
        			    $message="You have received 1 more like on your video";
        			    
        			    $notification['to'] = $rd1->tokon;
                        $notification['notification']['title'] = $title;
                        $notification['notification']['body'] = $message;
                        // $notification['notification']['text'] = $sender_details['User']['username'].' has sent you a friend request';
                        $notification['notification']['badge'] = "1";
                        $notification['notification']['sound'] = "default";
                        $notification['notification']['icon'] = "";
                         $notification['notification']['image'] = "";
                        $notification['notification']['type'] = "";
                        $notification['notification']['data'] = "";
        
                        sendPushNotificationToMobileDevice(json_encode($notification));
                        
	                //push notification
    			}
    			else
    			{
    			    $array_out = array();
        			$array_out[] = 
        				array(
        					"response" => "error in uploading files"
        				);
        			
        			$output=array( "code" => "201", "msg" => $array_out);
        			print_r(json_encode($output, true)); 
    			}
    			
    			
    			
    			
		    }
		   	
			
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
    function postComment()
    {
        
        require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		
		if(isset($event_json['fb_id']) && isset($event_json['video_id']) )
		{   
		    $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		    $video_id=htmlspecialchars(strip_tags($event_json['video_id'] , ENT_QUOTES));
		    $comment=htmlspecialchars(strip_tags($event_json['comment'] , ENT_QUOTES));
		    
		    $query1=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
		    $rd=mysqli_fetch_object($query1);
		        
    	    $qrry_1="insert into video_comment(video_id,fb_id,comments)values(";
			$qrry_1.="'".$video_id."',";
			$qrry_1.="'".$fb_id."',";
			$qrry_1.="'".$comment."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			   $array_out = array();
    			$array_out[] = 
    				array(
    					"fb_id" => $fb_id,
    					"video_id" => $video_id,
    					"comments" => $comment,
    					"user_info" =>array
            					(
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic
            					),
    				);
    			
    			$output=array( "code" => "200", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
    			
    			
                //push notification
    			        
			        //fetch user tokon 
			        
    			        $query1=mysqli_query($conn,"select * from videos where id='".$video_id."' ");
		                $rd=mysqli_fetch_object($query1);
		                
		                $query11=mysqli_query($conn,"select * from users where fb_id='".$rd->fb_id."' ");
		                $rd1=mysqli_fetch_object($query11);
	                
	                //fetch user tokon 
	                
	                //fetch name of a person who is liking other person video
		                $query111=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
		                $rd11=mysqli_fetch_object($query111);
		                
		            //fetch name of a person who is liking other person video
	                
    			    $title=$rd11->first_name." Post comment on your video";
    			    $message=$comment;
    			    
    			    $notification['to'] = $rd1->tokon;
                    $notification['notification']['title'] = $title;
                    $notification['notification']['body'] = $message;
                    // $notification['notification']['text'] = $sender_details['User']['username'].' has sent you a friend request';
                    $notification['notification']['badge'] = "1";
                    $notification['notification']['sound'] = "default";
                    $notification['notification']['icon'] = "";
                     $notification['notification']['image'] = "";
                    $notification['notification']['type'] = "";
                    $notification['notification']['data'] = "";
    
                    sendPushNotificationToMobileDevice(json_encode($notification));
                    
                //push notification
			}
			else
			{
			    $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "error"
    				);
    			
    			$output=array( "code" => "201", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
			
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
		
    }
    
    function showVideoComments()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['video_id']))
		{
			$video_id=htmlspecialchars(strip_tags($event_json['video_id'] , ENT_QUOTES));
			
			$query=mysqli_query($conn,"select * from video_comment where video_id='".$video_id."' order by id DESC");
		        
    		$array_out = array();
    		while($row=mysqli_fetch_array($query))
    		{
    		  
    		   $query1=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
		       $rd=mysqli_fetch_object($query1);
		        
    		   $array_out[] = 
        			array(
        			"video_id" => $row['video_id'],
        			"fb_id" => $row['fb_id'],
        			"user_info" =>array
            					(
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic
            					),
            	    
        			"comments" => $row['comments'],
        			"created" => $row['created']
        		);
    			
    		}
    		$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function allSounds()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		$query=mysqli_query($conn,"select * from sound_section ");
	        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		  
		   //echo $row['section'];
		   //echo "select * from sound where section ='".$row['section']."' ";
		   
		   $query1=mysqli_query($conn,"select * from sound where section ='".$row['id']."' ");
		   $array_out1 = array();
		   while($row1=mysqli_fetch_array($query1))
		   {
		        $array_out1[] = 
        			array(
            			"id" => $row1['id'],
            			
            			"audio_path" => 
                    			array(
                        			"mp3" => $API_path."/upload/audio/".$row1['id'].".mp3",
            			            "acc" => $API_path."/upload/audio/".$row1['id'].".aac"
                        		),
            			"sound_name" => $row1['sound_name'],
            			"description" => $row1['description'],
            			"section" => $row1['section'],
            			"thum" => $row1['thum'],
            			"created" => $row1['created']
            		);
		    }
		    
			if(count($array_out1)!="0")
			{		
				$array_out2[] = 
					array(
					"section_name" => $row['section_name'],
					"sections_sounds" => $array_out1
					
				);
			}
		    
		   
			
		}
		$output=array( "code" => "200", "msg" => $array_out2);
		print_r(json_encode($output, true));
		
	}
	
	
	function fav_sound()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['fb_id']) && isset($event_json['sound_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$sound_id=htmlspecialchars(strip_tags($event_json['sound_id'] , ENT_QUOTES));
			
			$qrry_1="insert into fav_sound(fb_id,sound_id)values(";
			$qrry_1.="'".$fb_id."',";
			$qrry_1.="'".$sound_id."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			 
			
				 $array_out = array();
				 $array_out[] = 
					//array("code" => "200");
					array(
        			"response" =>"successful");
				
				$output=array( "code" => "200", "msg" => $array_out);
				print_r(json_encode($output, true));
			}
			else
			{
			    //echo mysqli_error();
			    $array_out = array();
					
        		 $array_out[] = 
        			array(
        			"response" =>"problem");
        		
        		$output=array( "code" => "201", "msg" => $array_out);
        		print_r(json_encode($output, true));
			}
			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function my_FavSound()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	    
	    if(isset($event_json['fb_id']))
		{
		    
		   $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		    
    	   $query1=mysqli_query($conn,"select * from fav_sound where fb_id ='".$fb_id."' ");
		   $array_out1 = array();
		   while($row1=mysqli_fetch_array($query1))
		   {
		        
		        $qrry="select * from sound WHERE id ='".$row1['sound_id']."' ";
    			$log_in_rs=mysqli_query($conn,$qrry);
    			
    		    $rd=mysqli_fetch_object($log_in_rs);
    			    
    			$array_out1[] = 
        			array(
            			"id" => $row1['id'],
            			
            			"audio_path" => 
                    			array(
                        			"mp3" => $API_path."/upload/audio/".$row1['sound_id'].".mp3",
            			            "acc" => $API_path."/upload/audio/".$row1['sound_id'].".aac"
                        		),
            			"sound_name" => $rd->sound_name,
            			"description" => $rd->description,
            			"section" => $rd->section,
            			"thum" => $rd->thum,
            			"created" => $rd->created,
            		);
		    }
		    
		    $output=array( "code" => "200", "msg" => $array_out1);
			print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
		
	}
	
	
	
	function my_liked_video()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			
		    $query1=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
		    $rd=mysqli_fetch_object($query1);
		    if(mysqli_num_rows($query1))
		    {
		        
		        $query=mysqli_query($conn,"select * from video_like_dislike where fb_id='".$fb_id."' order by id DESC");
		        
		        $array_out_video = array();
        		while($row=mysqli_fetch_array($query))
        		{
        		   
        		   $query11=mysqli_query($conn,"select * from videos where id='".$row['video_id']."' ");
		           $rdd=mysqli_fetch_object($query11);
		            
		           $query112=mysqli_query($conn,"select * from sound where id='".$rdd->sound_id."' ");
		           $rd12=mysqli_fetch_object($query112);
		        
        		   $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['video_id']."' ");
                   $countLikes_count=mysqli_fetch_assoc($countLikes);
    		        
    		       $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row['video_id']."' ");
                   $countcomment_count=mysqli_fetch_assoc($countcomment);
                   
                   $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['video_id']."' and fb_id='".$fb_id."' ");
                   $liked_count=mysqli_fetch_assoc($liked);
                   
                   $query111=mysqli_query($conn,"select * from users where fb_id='".$rdd->fb_id."' ");
		           $rd11=mysqli_fetch_object($query111);
                
        		   $array_out_video[] = 
            			array(
            			"id" => $rdd->id,
            			"video" => @$rdd->video,
            			"thum" => @$rdd->thum,
            			"gif" => @$rdd->gif,
            			"description" => $rdd->description,
            			"liked" => $liked_count['count'],
            			"user_info" =>array
        					(
        					    "first_name" => $rd11->first_name,
        					    "username" => $rd11->username,
                    			"last_name" => $rd11->last_name,
                    			"profile_pic" => $rd11->profile_pic
        					),
            			"count" =>array
            					(
            					    "like_count" => $countLikes_count['count'],
                        			"video_comment_count" => $countcomment_count['count'],
                        			"view" => $rdd->view,
            					),
    					"sound" =>array
            					(
            					    "id" => $rd12->id,
            					    "audio_path" => 
                            			array(
                                			"mp3" => $API_path."/upload/audio/".@$rd12->id.".mp3",
                    			            "acc" => $API_path."/upload/audio/".@$rd12->id.".aac"
                                		),
                        			"sound_name" => @$rd12->sound_name,
                        			"description" => @$rd12->description,
                        			"thum" => @$rd12->thum,
                        			"section" => @$rd12->section,
                        			"created" => @$rd12->created,
            					),
            			"created" => $row['created']
            		);
        			
        		}
		        
		        $count_video_rows=count($array_out_video);
		        if($count_video_rows=="0")
		        {
		            $array_out_video=array(0);
		        }
		        
		        
		        
		        
                
                
		        $array_out = array();
		        $array_out[] = 
        			array(
        			"fb_id" => $fb_id,
        			"user_info" =>array
            					(
            					    "first_name" => $rd->first_name,
                        			"last_name" => $rd->last_name,
                        			"profile_pic" => $rd->profile_pic,
                        			"gender" => $rd->gender,
        	                		"created" => $rd->created,
            					),
        			
        			"total_heart" => "100",
        			"total_fans" => "88",
        			"total_following" => "55",
        			"user_videos" => $array_out_video
            	    
        		);
		        
		      
		    } 
		    
		    $output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function get_followers()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			
		    $query=mysqli_query($conn,"select * from follow_users where followed_fb_id='".$fb_id."' order by id DESC");
		        
	        $array_out = array();
    		while($row=mysqli_fetch_array($query))
    		{
    		   
    		   $query11=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
		       $rd1=mysqli_fetch_object($query11);
		       
		       
                $query2=mysqli_query($conn,"SELECT count(*) as count from follow_users where followed_fb_id='".$row['fb_id']."' and fb_id='".$fb_id."'  ");
                $follow_count=mysqli_fetch_assoc($query2);
                
                if($follow_count['count']=="0")
                {
                	$follow="0";
                	$follow_button_status="Follow";
                }
                else
                if($follow_count['count']!="0")
                {
                	$follow="1";
                	$follow_button_status="Unfollow";
                }
                

    		   $array_out[] = 
        			array(
        			    "fb_id" => $rd1->fb_id,
					    "username" => $rd1->username,
					    "first_name" => $rd1->first_name,
					    "last_name" => $rd1->last_name,
					    "gender" => $rd1->gender,
					    "bio" => $rd1->bio,
					    "profile_pic" => $rd1->profile_pic,
					    "created" => $rd1->created,
					    "follow_Status" =>array
                		(
                			"follow" => $follow,
                			"follow_status_button" => $follow_button_status
                		)
        		);
    			
    		}
	        
	        
		    $output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function get_followings()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		if(isset($event_json['fb_id']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			
		    $query=mysqli_query($conn,"select * from follow_users where fb_id='".$fb_id."' order by id DESC");
		        
	        $array_out = array();
    		while($row=mysqli_fetch_array($query))
    		{
    		   
    		   $query11=mysqli_query($conn,"select * from users where fb_id='".$row['followed_fb_id']."' ");
		       $rd1=mysqli_fetch_object($query11);
		       
		       $query1=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
		       $rd=mysqli_fetch_object($query1);
		        
		        
                $query2=mysqli_query($conn,"SELECT count(*) as count from follow_users where fb_id='".$fb_id."' and followed_fb_id='".$row['followed_fb_id']."' ");
                $follow_count=mysqli_fetch_assoc($query2);
                
                if($follow_count['count']=="0")
                {
                	$follow="0";
                	$follow_button_status="Follow";
                }
                else
                if($follow_count['count']!="0")
                {
                	$follow="1";
                	$follow_button_status="Unfollow";
                }
                

    		   $array_out[] = 
        			array(
        			    "fb_id" => $rd1->fb_id,
					    "username" => $rd1->username,
					    "first_name" => $rd1->first_name,
					    "last_name" => $rd1->last_name,
					    "gender" => $rd1->gender,
					    "bio" => $rd1->bio,
					    "profile_pic" => $rd1->profile_pic,
					    "created" => $rd1->created,
					    "follow_Status" =>array
                		(
                			"follow" => $follow,
                			"follow_status_button" => $follow_button_status
                		)
        		);
    			
    		}
	        
	        
		    $output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
    		
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	
	function discover()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
	
		$query=mysqli_query($conn,"select DISTINCT section from videos where section!='0' ");
	        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		  
		   //echo $row['section'];
		   //echo "select * from sound where section ='".$row['section']."' ";
		   
		   $query1=mysqli_query($conn,"select * from videos where section ='".$row['section']."' ");
		   $array_out1 = array();
		   while($row1=mysqli_fetch_array($query1))
		   {    
		        $query11=mysqli_query($conn,"select * from users where fb_id='".$row1['fb_id']."' ");
		        $rd1=mysqli_fetch_object($query11);
		        
		        $query112=mysqli_query($conn,"select * from sound where id='".$row1['sound_id']."' ");
		        $rd12=mysqli_fetch_object($query112);
		        
		        $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row1['id']."' ");
                $countLikes_count=mysqli_fetch_assoc($countLikes);
		        
		        $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row1['id']."' ");
                $countcomment_count=mysqli_fetch_assoc($countcomment);
               
		        $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row1['id']."' and fb_id='".$fb_id."' ");
                $liked_count=mysqli_fetch_assoc($liked);
                   
		        $array_out1[] = 
        			array(
            			"id" => $row1['id'],
            			"video" => $row1['video'],
            			"thum" => $row1['thum'],
            			"gif" => $row1['gif'],
            			"description" => $row1['description'],
            			"liked" => $liked_count['count'],
            			"count" =>array
            					(
            					    "like_count" => $countLikes_count['count'],
                        			"video_comment_count" => $countcomment_count['count'],
                        			"view" => $row1['view'],
            					),
            			"user_info" =>array
            					(
            					    "fb_id" => $rd1->fb_id,
            					    "first_name" => $rd1->first_name,
                        			"last_name" => $rd1->last_name,
                        			"profile_pic" => $rd1->profile_pic,
                        			"gender" => $rd1->gender,
        	                		"created" => $rd1->created,
            					),
    					"sound" =>array
            					(
            					    "id" => $rd12->id,
            					    "audio_path" => 
                            			array(
                                			"mp3" => $API_path."/upload/audio/".$rd12->id.".mp3",
                    			            "acc" => $API_path."/upload/audio/".$rd12->id.".aac"
                                		),
                        			"sound_name" => $rd12->sound_name,
                        			"description" => $rd12->description,
                        			"thum" => $rd12->thum,
                        			"section" => $rd12->section,
                        			"created" => $rd12->created,
            					),
            			"created" => $row1['created']
            		);
		    }
		    
		    $array_out2[] = 
    			array(
    			"section_name" => $row['section'],
    			"sections_videos" => $array_out1
    			
    		);
		   
			
		}
		$output=array( "code" => "200", "msg" => $array_out2);
		print_r(json_encode($output, true));
		
	}
	
	function all_discovery_sections()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		$query=mysqli_query($conn,"select DISTINCT section from videos where section!='0' ");
	        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		  
		   //echo $row['section'];
		   //echo "select * from sound where section ='".$row['section']."' ";
    		  $array_out1[] = 
        			array(
        			"section_name" => $row['section']
        	  );
		}
		$output=array( "code" => "200", "msg" => $array_out1);
		print_r(json_encode($output, true));
	}
	
	
	function deleteSection()
	{   
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		$name=$event_json['name'];
		
		@mysqli_query($conn,"update videos set section='0' where section='".$name."' ");
		
		$output=array( "code" => "200", "msg" => "deleted");
		print_r(json_encode($output, true));
		
	    
	}
	
	function assignSection()
	{   
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		$name=$event_json['name'];
		$id=$event_json['id'];
		
		@mysqli_query($conn,"update videos set section='".$name."' where id='".$id."' ");
		
		$output=array( "code" => "200", "msg" => "updated");
		print_r(json_encode($output, true));
		
	    
	}
	
	function edit_profile()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		//0= owner  1= company 2= ind mechanic
		
		if(isset($event_json['fb_id']) && isset($event_json['first_name']) && isset($event_json['last_name']) && isset($event_json['gender']) && isset($event_json['bio']) )
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$first_name=htmlspecialchars(strip_tags($event_json['first_name'] , ENT_QUOTES));
			$last_name=htmlspecialchars(strip_tags($event_json['last_name'] , ENT_QUOTES));
			$gender=htmlspecialchars(strip_tags($event_json['gender'] , ENT_QUOTES));
		    $bio=htmlspecialchars(strip_tags($event_json['bio'] , ENT_QUOTES));
			$username=htmlspecialchars(strip_tags($event_json['username'] , ENT_QUOTES));
			
			$qrry_1="update users SET first_name ='".$first_name."' , last_name ='".$last_name."', gender ='".$gender."' , bio ='".$bio."'  WHERE fb_id ='".$fb_id."' ";
			if(mysqli_query($conn,$qrry_1))
			{
			    $array_out = array();
				 
				$qrry_1="select * from users WHERE fb_id ='".$fb_id."' ";
    			$log_in_rs=mysqli_query($conn,$qrry_1);
    			
    			if(mysqli_num_rows($log_in_rs))
    			{   
    			    
                    
    			    $rd=mysqli_fetch_object($log_in_rs);
    			    
    			       $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"first_name" => $rd->first_name,
            			"username" => $rd-username,
            			"last_name" => $rd->last_name,
            			"gender" => $rd->gender,
            			"bio" => bio
            			);
            		
            		$output=array( "code" => "200", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
			
        		
			}
			else
			{
			    $array_out = array();
					
        		 $array_out[] = 
        			array(
        			"response" =>"problem in updating");
        		
        		$output=array( "code" => "201", "msg" => $array_out);
        		print_r(json_encode($output, true));
			}
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	    
	}
	
	
	function follow_users()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		//0= owner  1= company 2= ind mechanic
		
		
		if(isset($event_json['fb_id']) && isset($event_json['followed_fb_id']) && isset($event_json['status']))
		{
			$fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
			$followed_fb_id=htmlspecialchars(strip_tags($event_json['followed_fb_id'] , ENT_QUOTES));
			$status=htmlspecialchars(strip_tags($event_json['status'] , ENT_QUOTES));
			
			if($status=="0")
			{
			    mysqli_query($conn,"Delete from follow_users where fb_id ='".$fb_id."' and followed_fb_id='".$followed_fb_id."'  ");
			    
			    $array_out = array();
    				 $array_out[] = 
    					//array("code" => "200");
    					array(
            			"response" =>"unfollow");
    				
    				$output=array( "code" => "200", "msg" => $array_out);
    				print_r(json_encode($output, true));
			}
			else
			{
			    $qrry_1="insert into follow_users(fb_id,followed_fb_id)values(";
    			$qrry_1.="'".$fb_id."',";
    			$qrry_1.="'".$followed_fb_id."'";
    			$qrry_1.=")";
    			if(mysqli_query($conn,$qrry_1))
    			{
    			 
    			
    				 $array_out = array();
    				 $array_out[] = 
    					//array("code" => "200");
    					array(
            			"response" =>"follow successful");
    				
    				$output=array( "code" => "200", "msg" => $array_out);
    				print_r(json_encode($output, true));
    			}
    			else
    			{
    			    //echo mysqli_error();
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"problem in signup");
            		
            		$output=array( "code" => "201", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
			}
			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	
	
	//admin panel services
	
	function Admin_Login()
	{   
	   	require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		//0= owner  1= company 2= ind mechanic
		
		if(isset($event_json['email']) && isset($event_json['password']) )
		{
			$email=htmlspecialchars(strip_tags($event_json['email'] , ENT_QUOTES));
			$password=strip_tags($event_json['password']);
		
			
			$log_in="select * from admin where email='".$email."' and pass='".md5($password)."' ";
			echo($password."=".md5($password));
			$log_in_rs=mysqli_query($conn,$log_in);
			
			if(mysqli_num_rows($log_in_rs))
			{
				$array_out = array();
				 $array_out[] = 
					//array("code" => "200");
					array(
						"response" => "login success"
					);
				
				$output=array( "code" => "200", "msg" => $array_out);
				print_r(json_encode($output, true));
			}	
			else
			{
			    
    			$array_out = array();
    					
        		 $array_out[] = 
        			array(
        			"response" =>"Error in login");
        		
        		$output=array( "code" => "201", "msg" => $array_out);
        		print_r(json_encode($output, true));
			}
			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	
	function All_Users()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		
		$query=mysqli_query($conn,"select * from users order by id DESC");
		        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		     
		   	 $array_out[] = 
				array(
					"fb_id" => $row['fb_id'],
					"username" => $row['username'],
					"first_name" => $row['first_name'],
					"last_name" => $row['last_name'],
					"gender" => $row['gender'],
					"profile_pic" => $row['profile_pic'],
					"block" => $row['block'],
					"version" => $row['version'],
					"device" => $row['device'],
					"signup_type" => $row['signup_type'],
					"created" => $row['created']
					
				);
			
		}
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
		
	    
	}
	
	
	
	function admin_all_sounds()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		
		$query=mysqli_query($conn,"select * from sound order by id DESC");
		        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		     
		   	 $array_out[] = 
				array(
					"id" => $row['id'],
					"sound_name" => $row['sound_name'],
					"description" => $row['description'],
					"thum" => $row['thum'],
					"section" => $row['section'],
					"created" => $row['created']
					
				);
			
		}
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
		
	    
	}
	
	function admin_uploadSound()
	{
	    require_once("config.php");
	    require_once("uploadtos3.php");

	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		//0= owner  1= company 2= ind mechanic
		
		if(isset($event_json['fileUrl']))
		{
			$fileUrl=$event_json['fileUrl'];
			$sound_name=htmlspecialchars(strip_tags($event_json['sound_name'] , ENT_QUOTES));
			$description=htmlspecialchars(strip_tags($event_json['description'] , ENT_QUOTES));
			$section_id=htmlspecialchars(strip_tags($event_json['section_id'] , ENT_QUOTES));
			
			$qrry_1="insert into sound(sound_name,description,section)values(";
			$qrry_1.="'".$sound_name."',";
			$qrry_1.="'".$description."',";
			$qrry_1.="'".$section_id."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			     $insert_id=mysqli_insert_id($conn);
			     @copy($fileUrl,'upload/audio/'.$insert_id.'.aac');

			     $saveurl='upload/audio/'.$insert_id.'.aac';
			     uploadToStorage($insert_id.".aac", fopen($saveurl,"rb"),"audio/");

				 $array_out = array();
				 $array_out[] = 
					//array("code" => "200");
					array(
        			"response" =>"successful");
				
				$output=array( "code" => "200", "msg" => $array_out);
				print_r(json_encode($output, true));
			}
			else
			{
			    //echo mysqli_error();
			    $array_out = array();
					
        		 $array_out[] = 
        			array(
        			"response" =>"problem in signup");
        		
        		$output=array( "code" => "201", "msg" => $array_out);
        		print_r(json_encode($output, true));
			}
			
			
			
			

			
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	
	function admin_getSoundSection()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		
		$query=mysqli_query($conn,"select * from sound_section");
		        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		     
		   	 $array_out[] = 
				array(
					"id" => $row['id'],
					"section_name" => $row['section_name'],
					"created" => $row['created']
				);
			
		}
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
	}
	
	
	function admin_show_allVideos()
	{
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	
		$query=mysqli_query($conn,"select * from videos order by id DESC");
	        
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		    
		    $query1=mysqli_query($conn,"select * from users where fb_id='".$row['fb_id']."' ");
	        $rd=mysqli_fetch_object($query1);
	       
	        $query112=mysqli_query($conn,"select * from sound where id='".$row['sound_id']."' ");
	        $rd12=mysqli_fetch_object($query112);
	        
	        $countLikes = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' ");
            $countLikes_count=mysqli_fetch_assoc($countLikes);
	        
	        $countcomment = mysqli_query($conn,"SELECT count(*) as count from video_comment where video_id='".$row['id']."' ");
            $countcomment_count=mysqli_fetch_assoc($countcomment);
            
            
            $liked = mysqli_query($conn,"SELECT count(*) as count from video_like_dislike where video_id='".$row['id']."' and fb_id='".$row['fb_id']."' ");
            $liked_count=mysqli_fetch_assoc($liked);
	        
    	   	$array_out[] = 
    			array(
    			"id" => $row['id'],
    			"fb_id" => $row['fb_id'],
    			"user_info" =>array
        					(
        					    "first_name" => $rd->first_name,
        					    "username" => $rd->username,
                    			"last_name" => $rd->last_name,
                    			"profile_pic" => $rd->profile_pic
        					),
        		"count" =>array
        					(
        					    "like_count" => $countLikes_count['count'],
                    			"video_comment_count" => $countcomment_count['count']
        					),
        		"liked"=> $liked_count['count'],			
        	    "video" => $row['video'],
    			"thum" => $row['thum'],
    			"gif" => $row['gif'],
    			"section" => $row['section'],
    			"sound" =>array
        					(
        					    "id" => $rd12->id,
        					    "audio_path" => 
                        			array(
                            			"mp3" => $API_path."/upload/audio/".$rd12->id.".mp3",
                			            "acc" => $API_path."/upload/audio/".$rd12->id.".aac"
                            		),
                    			"sound_name" => $rd12->sound_name,
                    			"description" => $rd12->description,
                    			"thum" => $rd12->thum,
                    			"section" => $rd12->section,
                    			"created" => $rd12->created,
        					),
    			"created" => $row['created']
    		);
			
		}
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
	}
	
	
	function get_user_data()
	{
	   
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
	    
	    $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		$query=mysqli_query($conn,"select * from users where fb_id='".$fb_id."' ");
	   
		$array_out = array();
		while($row=mysqli_fetch_array($query))
		{
		    
		    $array_out[] = 
    			array(
    			"fb_id" => $row['fb_id'],
    			"username" => $row['username'],
    			"first_name"=> $row['first_name'],			
        	    "last_name" => $row['last_name'],
    			"gender" => $row['gender'],
    			"bio" => $row['bio'],
    			"profile_pic" => $row['profile_pic'],
    			"created" => $row['created']
    		);
			
		}
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
	}
	
	function changePassword()
	{
	    
	    require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
	    
	    //print_r($event_json);
		
		if(isset($event_json['new_password']) && isset($event_json['old_password']))
		{
			$old_password=strip_tags($event_json['old_password']);
			$new_password=strip_tags($event_json['new_password']);
		    
		    die();
		    
		    $qrry_1="select * from admin where pass ='".md5($old_password)."' ";
			$log_in_rs=mysqli_query($conn,$qrry_1);
			$rd=mysqli_fetch_object($log_in_rs);
			
			if($rd->id!="")
			{
			    $qrry_1="update admin SET pass ='".md5($new_password)."' where id='".$rd->id."'  ";
    			if(mysqli_query($conn,$qrry_1))
    			{
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"success");
            		
            		$output=array( "code" => "200", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
    			else
    			{
    			    $array_out = array();
    					
            		 $array_out[] = 
            			array(
            			"response" =>"problem in updating");
            		
            		$output=array( "code" => "201", "msg" => $array_out);
            		print_r(json_encode($output, true));
    			}
			}
			else
			{
			    $array_out = array();
					
        		 $array_out[] = 
        			array(
        			"response" =>"problem in updating");
        		
        		$output=array( "code" => "201", "msg" => $array_out);
        		print_r(json_encode($output, true));
			}
			
			
		}
		else
		{
			$array_out = array();
					
			 $array_out[] = 
				array(
				"response" =>"Json Parem are missing");
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	    
	}
	
	
	function DeleteSound()
	{
	    require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['id']))
		{   
		    $id=htmlspecialchars(strip_tags($event_json['id'] , ENT_QUOTES));
		   
		    mysqli_query($conn,"Delete from sound where id ='".$id."' ");
		    mysqli_query($conn,"Delete from fav_sound where sound_id ='".$id."' ");
	    	mysqli_query($conn,"update videos set sound_id='0' where sound_id ='".$id."' ");
		
    	    $array_out = array();
    					
    			 $array_out[] = 
    				array(
    				"response" =>"video unlike");
    	        
        	$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
		
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function DeleteVideo()
	{
	    require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['id']))
		{   
		    $id=htmlspecialchars(strip_tags($event_json['id'] , ENT_QUOTES));
		   	
			$qrry_1="select * from videos where id ='".$id."' ";
			$log_in_rs=mysqli_query($conn,$qrry_1);
			$rd=mysqli_fetch_object($log_in_rs);
			
			$videoPath=$rd->video;
			$thumPath=$rd->thum;
			$gifPath=$rd->gif;
			
			@unlink($videoPath);
			@unlink($thumPath);
			@unlink($gifPath);
			
			
		    mysqli_query($conn,"Delete from videos where id ='".$id."' ");
			mysqli_query($conn,"Delete from video_like_dislike where video_id ='".$id."' ");
			mysqli_query($conn,"Delete from video_comment where video_id ='".$id."' ");
		   
		   	
			
    	    $array_out = array();
    					
    			 $array_out[] = 
    				array(
    				"response" =>"video unlike");
    	        
        	$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
		
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		} 
	}
	
	function admin_addSection()
	{
	    require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json['name']);
		$name=$event_json['name'];
		if(isset($name))
		{   
		    $qrry_1="insert into sound_section(section_name)values(";
			$qrry_1.="'".$name."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			   $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "success"
    				);
    			
    			$output=array( "code" => "200", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
    			
    		
			}
			else
			{
			    
			    $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "error"
    				);
    			
    			$output=array( "code" => "201", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
			
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	}
	
	function DeleteSoundSectino()
	{
	    require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['id']))
		{   
		    $id=htmlspecialchars(strip_tags($event_json['id'] , ENT_QUOTES));
		   
		    mysqli_query($conn,"Delete from sound_section where id ='".$id."' ");
			mysqli_query($conn,"update sound set section='' ");
		   
    	    $array_out = array();
    					
    			 $array_out[] = 
    				array(
    				"response" =>"video unlike");
    	        
        	$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
		
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		} 
	}
	
	function blockUser()
	{
		require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['user_id']))
		{   
		    $user_id=htmlspecialchars(strip_tags($event_json['user_id'] , ENT_QUOTES));
			$block=htmlspecialchars(strip_tags($event_json['block'] , ENT_QUOTES));
		   
		    mysqli_query($conn,"update users set block='".$block."' where fb_id ='".$user_id."' ");
		   
    	    $array_out = array();
    					
    			 $array_out[] = 
    				array(
    				"response" =>"user blocked");
    	        
        	$output=array( "code" => "200", "msg" => $array_out);
    		print_r(json_encode($output, true));
		
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		} 
	}
	
	
	function getSingleSectionDetails()
	{
		require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
	    
	    //print_r($event_json);
		$id=strip_tags($event_json['id']);
		 
		$qrry_1="select * from sound_section where id ='".$id."' ";
		$log_in_rs=mysqli_query($conn,$qrry_1);
		$rd=mysqli_fetch_object($log_in_rs);
		
		$array_out[] = 
    			array(
    			"id" => $rd->id,
    			"section_name" => $rd->section_name,
    			"created"=> $rd->created
    		);
			
	
		$output=array( "code" => "200", "msg" => $array_out);
		print_r(json_encode($output, true));
	
	
	}
	
	function editSoundSection()
	{
		require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
	    
	    //print_r($event_json);
		$id=strip_tags($event_json['id']);
		$section_name=htmlspecialchars(strip_tags($event_json['section_name'] , ENT_QUOTES));
		 
		$qrry_1="update sound_section set section_name='".$section_name."'  where id ='".$id."' ";
		$log_in_rs=mysqli_query($conn,$qrry_1);
		$rd=mysqli_fetch_object($log_in_rs);
		
		
	
		$output=array( "code" => "200", "msg" => "updated");
		print_r(json_encode($output, true));
	
	
	}
	
	function addVideointoDiscovry()
	{
		require_once("config.php");
	    $input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
	    
	    //print_r($event_json);
		$id=strip_tags($event_json['id']);
		$section_name=htmlspecialchars(strip_tags($event_json['section_name'] , ENT_QUOTES));
		 
		$qrry_1="update videos set section='".$section_name."'  where id ='".$id."' ";
		$log_in_rs=mysqli_query($conn,$qrry_1);
		$rd=mysqli_fetch_object($log_in_rs);
		
		
	
		$output=array( "code" => "200", "msg" => "updated");
		print_r(json_encode($output, true));
	}
	
	
	function sendPushNotification()
	{
		require_once("config.php");
		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		
		$log_in="select * from users where fb_id='".$event_json['receiverid']."' ";
		$log_in_rs=mysqli_query($conn,$log_in);
		$rd=mysqli_fetch_object($log_in_rs);  
		
		$tokon=$rd->tokon;
		$title=htmlspecialchars(strip_tags($event_json['title'] , ENT_QUOTES));
		$message=htmlspecialchars(strip_tags($event_json['message'] , ENT_QUOTES));
		$icon=htmlspecialchars(strip_tags($event_json['icon'] , ENT_QUOTES));
		$senderid=htmlspecialchars(strip_tags($event_json['senderid'] , ENT_QUOTES));
		$receiverid=htmlspecialchars(strip_tags($event_json['receiverid'] , ENT_QUOTES));
		$action_type=htmlspecialchars(strip_tags($event_json['action_type'] , ENT_QUOTES));
		
		
		
		$notification['to'] = $tokon;
		$notification['notification']['title'] = $title;
		$notification['notification']['body'] = $message;
		// $notification['notification']['text'] = $sender_details['User']['username'].' has sent you a friend request';
		$notification['notification']['badge'] = "1";
		$notification['notification']['sound'] = "default";
		$notification['notification']['icon'] = "";
		 $notification['notification']['image'] = "";
		$notification['notification']['type'] = "";
		
		$notification['data']['title'] = $title;
		$notification['data']['body'] = $message;
		$notification['data']['icon'] = $icon;
		$notification['data']['senderid'] = $senderid;
		$notification['data']['receiverid'] = $receiverid;
		$notification['data']['action_type'] = $action_type;	
		
		sendPushNotificationToMobileDevice(json_encode($notification));  
	}


	function uploadVideoEnhanced()
	{
	    require_once("config.php");
		require_once("uploadtos3.php");

		$input = @file_get_contents("php://input");
	    $event_json = json_decode($input,true);
		//print_r($event_json);
		
		if(isset($event_json['fb_id']) && isset($event_json['picbase64'])  && isset($event_json['videobase64']))
		{   
		    $fb_id=htmlspecialchars(strip_tags($event_json['fb_id'] , ENT_QUOTES));
		    $description=htmlspecialchars(strip_tags($event_json['description'] , ENT_QUOTES));
		    $sound_id=htmlspecialchars(strip_tags($event_json['sound_id'] , ENT_QUOTES));
		    $thum = $event_json['picbase64']['file_data'];
		    $video = $event_json['videobase64']['file_data'];
		    $gif = $event_json['gifbase64']['file_data'];
            
            $fileName=rand()."_".rand();
			$video_url="upload/video/".$fileName.".mp4";
			$thum_url="upload/thum/".$fileName.".jpg";
			$gif_url="upload/gif/".$fileName.".gif";
			
			/*list($type, $data) = explode(',', $data);
			list(, $data)      = explode(',', $data);*/
			$thum = base64_decode($thum);			
			file_put_contents("upload/thum/".$fileName.".jpg", $thum);
			uploadToStorage($fileName.".jpg",$thum,"thum/");
			
			$video = base64_decode($video);		
			file_put_contents("upload/video/".$fileName.".mp4", $video);			
			uploadToStorage($fileName.".mp4",$video,"video/");
			
			$gif = base64_decode($gif);
			file_put_contents("upload/gif/".$fileName.".gif", $gif);
			uploadToStorage($fileName.".gif",$gif,"gif/");
			
			$qrry_1="insert into videos(description,video,sound_id,fb_id,gif,thum)values(";
			$qrry_1.="'".$description."',";
			$qrry_1.="'".$video_url."',";
			$qrry_1.="'".$sound_id."',";
			$qrry_1.="'".$fb_id."',";
			$qrry_1.="'".$gif_url."',";
			$qrry_1.="'".$thum_url."'";
			$qrry_1.=")";
			if(mysqli_query($conn,$qrry_1))
			{
			   $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "file uploaded"
    				);
    			
    			$output=array( "code" => "200", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
			else
			{
			    $array_out = array();
    			$array_out[] = 
    				array(
    					"response" => "error in uploading files"
    				);
    			
    			$output=array( "code" => "201", "msg" => $array_out);
    			print_r(json_encode($output, true)); 
			}
						
		}
		else
		{
			$array_out = array();
			$array_out[] = 
				array(
					"response" => "json parem missing"
				);
			
			$output=array( "code" => "201", "msg" => $array_out);
			print_r(json_encode($output, true));
		}
	
	}	


?>

