<?php 
require_once("config.php"); 
if( isset($_SESSION['id']))
{ 
	
	if( isset($_GET['action']) ) { //log

    	if( $_GET['action'] == "submitSound" ) { //login user
    
    		$alltitles=@$_POST['title'];
            $alltags=@$_POST['tagss'];
            $allurl=@$_POST['url'];
            $allSection=@$_POST['section_name'];
            $id=@$_POST['id'];
            if($alltitles!="" && $alltags!="" && $allurl!="")
            {
                $headers = array(
        			"Accept: application/json",
        			"Content-Type: application/json"
        		);
        
        		$data = array(
        			"fileUrl" => $allurl,
        			"sound_name" => $alltitles,
        			"description" => $alltags,
        			"section_id" => $allSection
        		);
        
        		$ch = curl_init( $baseurl.'admin_uploadSound' );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        		
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                $path="uploadSound/upload/".$id.".aac";
                @unlink($path);
                echo "<script>window.location='dashboard.php?p=sounds&page=soundGallary&action=success'</script>";
            }
            else 
    		{
    			echo "<script>window.location='dashboard.php?p=sounds&page=soundGallary&action=error'</script>";
    		} 
    
    	} //login user = end
        else
        if( $_GET['action'] == "deleteSound" ) { //login user
    
    		$sound_id=@$_GET['id'];
            
            if($sound_id!="")
            {
                $headers = array(
        			"Accept: application/json",
        			"Content-Type: application/json"
        		);
        
        		$data = array(
        			"id" => $sound_id
        		);
        
        		$ch = curl_init( $baseurl.'DeleteSound' );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        		
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                
                echo "<script>window.location='dashboard.php?p=sounds&page=sound&action=success'</script>";
            }
            else 
    		{
    			echo "<script>window.location='dashboard.php?p=sounds&page=sound&action=error'</script>";
    		} 
    
    	}
    	else
    	if( $_GET['action'] == "deleteSection" ) { //login user
    
    		$section_id=@$_GET['id'];
            
            if($section_id!="")
            {
                $headers = array(
        			"Accept: application/json",
        			"Content-Type: application/json"
        		);
        
        		$data = array(
        			"id" => $section_id
        		);
        
        		$ch = curl_init( $baseurl.'DeleteSoundSectino' );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        		
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                
                echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=success'</script>";
            }
            else 
    		{
    			echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=error'</script>";
    		} 
    
    	}
        else
        if( $_GET['action'] == "editSection" ) { //login user
    
    		$section_id=@$_POST['id'];
    		$section_name=htmlspecialchars(strip_tags($_POST['section_name'] , ENT_QUOTES));
            
            if($section_id!="")
            {
                $headers = array(
        			"Accept: application/json",
        			"Content-Type: application/json"
        		);
        
        		$data = array(
        			"id" => $section_id,
        			"section_name"=>$section_name
        		);
        
        		$ch = curl_init( $baseurl.'editSoundSection' );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        		
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
               
                echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=success'</script>";
            }
            else 
    		{
    			echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=error'</script>";
    		} 
    
    	}
    	else
        if( $_GET['action'] == "addSoundSection" ) { //login user
    
    		$section_name=htmlspecialchars(strip_tags($_POST['section_name'] , ENT_QUOTES));
            
            if($section_name!="")
            {
                $headers = array(
        			"Accept: application/json",
        			"Content-Type: application/json"
        		);
        
        		$data = array(
        			"name"=>$section_name
        		);
        
        		$ch = curl_init( $baseurl.'admin_addSection' );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        		
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                
                echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=success'</script>";
            }
            else 
    		{
    			echo "<script>window.location='dashboard.php?p=sounds&page=sections&action=error'</script>";
    		} 
    
    	}
    
    } //log = end
	?>

	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css">
  	<!--<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.js"></script>-->
  	<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	<script>
		$(document).ready(function() {
		    $('#data1').DataTable();
		} );
	</script>

	
	<h2 class="title">All Sounds</h2>
	
	<br>
	<div class="left">
        <a href="?p=sounds&page=sound" class="links_sublinks  <?php if(@$_GET['page']=="sound"){ echo "links_sublinks_active";} ?> ">
    		<span>All Sound</span>
    	</a>
        
        <a href="?p=sounds&page=sections" class="links_sublinks  <?php if(@$_GET['page']=="sections"){ echo "links_sublinks_active";} ?>  " style="margin-left: 22px;">
            <span>All Sections</span>
            
        </a>
        
        <a href="?p=sounds&page=soundGallary" class="links_sublinks  <?php if(@$_GET['page']=="soundGallary"){ echo "links_sublinks_active";} ?>  " style="margin-left: 22px;">
            <span>Sound Gallary</span>
            
        </a>
    
    </div>
    
    <?php
        if(@$_GET['page']=="sound")
        {
            ?>
                <div class="right" style="padding: 10px 0;">
                    <a href="uploadSound/" target="_blank">
            			<button style="background:  #C82D32; color:  white; padding:  8px 8px; border:  0px; border-radius:  3px;">Add Sound File</button>
                    </a>
                </div>
            <?php
        }
        
        if(@$_GET['page']=="sections")
        {
            ?>
                <div class="right" style="padding: 10px 0;">
                    <span onclick=addSoundSection();>
            			<button style="background:  #C82D32; color:  white; padding:  8px 8px; border:  0px; border-radius:  3px;">Add Section</button>
                    </span>
                </div>
            <?php
        }
    ?>
    
    <div class="clear"></div>
    <br>
    <br>
    <br>
    
	
	
	<?php 
		
		
		if(@$_GET['page']=="sound")
    	{
    	 
    	        $headers = array(
    			"Accept: application/json",
    			"Content-Type: application/json"
        		);
        
        		$data = array(
        			//"user_id" => $user_id
        		);
        
        		$ch = curl_init( $baseurl.'admin_all_sounds' );
               
        		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        	   // var_dump($return);
        
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                //echo count($json_data['msg']);
                //print_r($json_data);
        		//echo $json_data['code'];
        		//die;
        
        		if($json_data['code'] != "200"){
        			//echo "<div class='alert alert-danger'>Error in fetching order history, try again later..</div>";
        			?>
        			<div class="textcenter nothingelse">
        				<img src="img/noorder.png" alt="" />
        				<h3>No Record Found2</h3>
        			</div>
        			<?php
        
        		} else {
        			?>
        			
        			<?php 
        			
        			$rows = count($json_data['msg']);
        			if( $rows == 0 ) {
        				?>
        				<div class="textcenter nothingelse">
        					<img src="img/noorder.png" alt="" />
        					<h3>No Record Found1</h3>
        				</div>
        				<?php
        			}
        			echo "<table id='data1' class='display' style='width:100%''>
        			<thead>
        	            <tr>
        	                <th>ID</th>
        	                <th>Sound Preview</th>
        	                <th>Sound Name</th>
        	                <th>Description</th>
        	                <td>Section</th>
        	                <th>Created</th>
        	                <th>Action</th>
        	            </tr>
        	        </thead>
        			<tbody id='myTable_row'>";
        			
        			foreach( $json_data['msg'] as $str => $val ) {
        				//var_dump($val);
        				?>
        				<tr style=" text-align: center;">
        					<td>
        						<?php 
        							echo $val['id']; 
        						?>
        					</td>
        					<td id='preview_play_<?php echo $val['id']; ?>' >
        					    <span onclick="playsound('<?php echo $val['id']; ?>')"><img src="img/play.png" style="width: 30px;"></span>
        					</td>
        					<td style="line-height: 20px;">
        						<?php 
        							echo $val['sound_name'];
        						?>		
        					</td>
        					<td>
        						<?php echo $val['description'];  ?>
        					</td>
        					
        					<td>
        						<?php echo $val['section'];  ?>
        					</td>
        					
        				    <td>
        						<?php 
        							echo $val['created']; 
        						?>
        					</td>
        					<td>
        					    <div class="more">
                                    <button id="more-btn" class="more-btn">
                                        <span class="more-dot"></span>
                                        <span class="more-dot"></span>
                                        <span class="more-dot"></span>
                                    </button>
                                    <div class="more-menu">
                                        <div class="more-menu-caret">
                                            <div class="more-menu-caret-outer"></div>
                                            <div class="more-menu-caret-inner"></div>
                                        </div>
                                        <ul class="more-menu-items" tabindex="-1" role="menu" aria-labelledby="more-btn" aria-hidden="true">
                                            
                                            <a href="?p=sounds&page=sound&action=deleteSound&id=<?php echo $val['id']; ?>" style="color: red;text-decoration: none;">
                                                <li class="more-menu-item" role="presentation">
                                                    <button type="button" class="more-menu-btn" role="menuitem">Delete</button>
                                                </li>
                                            </a>
                                            
                                        </ul>
                                    </div>
                                </div>
        					</td>
        					
        					
        					
        				</tr>
        				<?php
        			}
        			echo "</tbody>
        			<tfoot>
        	            <tr>
        	                <th>ID</th>
        	                <th>thum</th>
        	                <th>Sound Name</th>
        	                <th>Description</th>
        	                <td>Section</th>
        	                <th>Created</th>
        	                <th>Action</th>
        	            </tr>
        	        </tfoot>
        	        </table> <nav><ul class='pagination pagination-sm' id='myPager'></ul></nav>";
        			///
        		}
        
        		curl_close($ch);
    	    
    	}
    	else
    	if(@$_GET['page']=="sections")
    	{
    	         $headers = array(
    			"Accept: application/json",
    			"Content-Type: application/json"
        		);
        
        		$data = array(
        			//"user_id" => $user_id
        		);
        
        		$ch = curl_init( $baseurl.'admin_getSoundSection' );
               
        		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
        		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        
        		$return = curl_exec($ch);
        
        		$json_data = json_decode($return, true);
        	   // var_dump($return);
        
        		$curl_error = curl_error($ch);
        		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                //echo count($json_data['msg']);
                //print_r($json_data);
        		//echo $json_data['code'];
        		//die;
        
        		if($json_data['code'] != "200"){
        			//echo "<div class='alert alert-danger'>Error in fetching order history, try again later..</div>";
        			?>
            			<div class="textcenter nothingelse">
            				<img src="img/noorder.png" alt="" />
            				<h3>No Record Found2</h3>
            			</div>
        			<?php
        
        		} else {
        			?>
        			
        			<?php 
        			
        			$rows = count($json_data['msg']);
        			if( $rows == 0 ) {
        				?>
        				<div class="textcenter nothingelse">
        					<img src="img/noorder.png" alt="" />
        					<h3>No Record Found1</h3>
        				</div>
        				<?php
        			}
        			echo "<table id='data1' class='display' style='width:100%''>
        			<thead>
        	            <tr>
        	                <th>ID</th>
        	                <th>Section Name</th>
        	                <th>Created</th>
        	                <th>Action</th>
        	            </tr>
        	        </thead>
        			<tbody id='myTable_row'>";
        			
        			foreach( $json_data['msg'] as $str => $val ) {
        				//var_dump($val);
        				?>
        				<tr style=" text-align: center;">
        					<td>
        						<?php 
        							echo $val['id']; 
        						?>
        					</td>
        					
        					<td>
        						<?php echo $val['section_name'];  ?>
        					</td>
        					
        				    <td>
        						<?php 
        							echo $val['created']; 
        						?>
        					</td>
        					<td>
        					    <div class="more">
                                    <button id="more-btn" class="more-btn">
                                        <span class="more-dot"></span>
                                        <span class="more-dot"></span>
                                        <span class="more-dot"></span>
                                    </button>
                                    <div class="more-menu">
                                        <div class="more-menu-caret">
                                            <div class="more-menu-caret-outer"></div>
                                            <div class="more-menu-caret-inner"></div>
                                        </div>
                                        <ul class="more-menu-items" tabindex="-1" role="menu" aria-labelledby="more-btn" aria-hidden="true">
                                            
                                            <li class="more-menu-item" role="presentation" onclick=editSection('<?php echo $val['id']; ?>');>
                                                <button type="button" class="more-menu-btn" role="menuitem">Edit</button>
                                            </li>
                                            
                                            <a href="?p=sounds&page=sections&action=deleteSection&id=<?php echo $val['id']; ?>" style="color: red;text-decoration: none;">
                                                <li class="more-menu-item" role="presentation">
                                                    <button type="button" class="more-menu-btn" role="menuitem">Delete</button>
                                                </li>
                                            </a>
                                            
                                        </ul>
                                    </div>
                                </div>
        					</td>
        					
        				</tr>
        				<?php
        			}
        			echo "</tbody>
        			<tfoot>
        	            <tr>
        	                <th>ID</th>
        	                <th>Section Name</th>
        	                <th>Created</th>
        	                <th>Action</th>
        	            </tr>
        	        </tfoot>
        	        </table> <nav><ul class='pagination pagination-sm' id='myPager'></ul></nav>";
        			///
        		}
        
        		curl_close($ch);
    	}
    	else
    	if(@$_GET['page']=="soundGallary")
    	{   
    	    
    	    $dir = "uploadSound/upload/";
		    $files = array();
    		$dir = opendir($dir); // open the cwd..also do an err check.
    		while(false != ($file = readdir($dir))) 
    		{
    			if(($file != ".") and ($file != "..") and ($file != "index.php")) {
    					$files[] = $file; // put in array.
    			}   
    		}
    		
    		natsort($files); // sort.
    	    
    	    ?>
    	        <div class="col100 contentside">
    	               
    	               <?php
    	                    foreach($files as $file) 
                			{
                				$post_name=explode(".",$file);
                				
                				?>
                				    <div class="col25 left">
                	                    <div id='<?php echo $post_name[0];?>' style="text-align: center;margin-bottom: 8px; border: solid 1px lightgray;padding: 8px 5px;margin-right: 10px;border-radius: 3px;">
                    					    <div style="font-size: 11px;margin-bottom:8px;height: 14px; overflow: hidden;"><?php echo $file; ?></div>
                    					    <div id="preview_play_gallary_<?php echo $post_name[0];?>" style="margin-bottom: 8px;"><img src="img/play.png" onclick=playsoundGallary('<?php echo $post_name[0];?>') style="width: 30px;"></div>
                    					    <div onclick=submitSound('<?php echo $post_name[0];?>') style="background: #D4401D;color: white;padding: 5px 0;border-radius: 3px;font-size: 11px;margin-top: 5px; width: 55px; float:left; ">
                    					        Publish
                    					    </div>
                    					    <div onclick=deleteImge('<?php echo $post_name[0];?>') style="background:gray;color: white;padding: 5px 0;border-radius: 3px;font-size: 11px;margin-top: 5px;width: 55px; float:right;">
                    					        Delete
                    					    </div>
                    					    <div class="clear"></div>
                    					</div>
                    					<?php //echo "<img src=\"uploadSound/upload/$file\" width=\"80px\">"; ?>
                	               </div>
                				<?php
                			}
    	               ?>
    	               
    	               <div class="clear"></div>
    	        </div>
    	    <?php
    	    
    	}
    	
    	
		
	?>
    
    
	<script>
	    
	    function addSoundSection()
	    {
	        
	        //alert(data1);
			document.getElementById("PopupParent").style.display="block";
		    document.getElementById("contentReceived").innerHTML="<div style='margin-top:150px;' align='center'><img src='img/loader.gif' width='150px'></div>";
		    var xmlhttp;
		    if(window.XMLHttpRequest)
		      {// code for IE7+, Firefox, Chrome, Opera, Safari
		        xmlhttp=new XMLHttpRequest();
		      }
		    else
		      {// code for IE6, IE5
		        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		      }
		      
		      xmlhttp.onreadystatechange=function()
		      {
		        if(xmlhttp.readyState==4 && xmlhttp.status==200)
		        {
		           // alert(xmlhttp.responseText);
		           document.getElementById('contentReceived').innerHTML=xmlhttp.responseText;
		        }
		      }
		    xmlhttp.open("GET","ajex-events.php?addSoundSection=ok&");
		    xmlhttp.send();
	        
	    }
	    function submitSound(data)
		{	
			//alert(data1);
			document.getElementById("PopupParent").style.display="block";
		    document.getElementById("contentReceived").innerHTML="<div style='margin-top:150px;' align='center'><img src='img/loader.gif' width='150px'></div>";
		    var xmlhttp;
		    if(window.XMLHttpRequest)
		      {// code for IE7+, Firefox, Chrome, Opera, Safari
		        xmlhttp=new XMLHttpRequest();
		      }
		    else
		      {// code for IE6, IE5
		        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		      }
		      
		      xmlhttp.onreadystatechange=function()
		      {
		        if(xmlhttp.readyState==4 && xmlhttp.status==200)
		        {
		           // alert(xmlhttp.responseText);
		           document.getElementById('contentReceived').innerHTML=xmlhttp.responseText;
		        }
		      }
		    xmlhttp.open("GET","ajex-events.php?submitSound=ok&sound_id="+data);
		    xmlhttp.send();
		}
	    function editSection(id)
	    {
	        //alert(data1);
			document.getElementById("PopupParent").style.display="block";
		    document.getElementById("contentReceived").innerHTML="<div style='margin-top:150px;' align='center'><img src='img/loader.gif' width='150px'></div>";
		    var xmlhttp;
		    if(window.XMLHttpRequest)
		      {// code for IE7+, Firefox, Chrome, Opera, Safari
		        xmlhttp=new XMLHttpRequest();
		      }
		    else
		      {// code for IE6, IE5
		        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		      }
		      
		      xmlhttp.onreadystatechange=function()
		      {
		        if(xmlhttp.readyState==4 && xmlhttp.status==200)
		        {
		           // alert(xmlhttp.responseText);
		           document.getElementById('contentReceived').innerHTML=xmlhttp.responseText;
		        }
		      }
		    xmlhttp.open("GET","ajex-events.php?editSection=ok&id="+id);
		    xmlhttp.send();
	    }
	    function deleteImge(idd)
		{   
		    var xmlhttp;
		    if(window.XMLHttpRequest)
		      {// code for IE7+, Firefox, Chrome, Opera, Safari
		        xmlhttp=new XMLHttpRequest();
		      }
		    else
		      {// code for IE6, IE5
		        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		      }
		      
		      xmlhttp.onreadystatechange=function()
		      {
		        if(xmlhttp.readyState==4 && xmlhttp.status==200)
		        {
		           //alert(xmlhttp.responseText);
		        }
		      }
		    xmlhttp.open("GET","uploadSound/delete.php?file_name="+idd);
		    xmlhttp.send();  
		    document.getElementById(idd).innerHTML = '';
		    document.getElementById(idd).style.display = "none";
		}
		
		
		function playsound(data)
		{	
			document.getElementById('preview_play_'+data).innerHTML='<audio controls="controls" style="border-radius: 20px;height: 30px;"><source src="<?php echo $storageUrl; ?>upload/audio/'+data+'.aac" type="audio/mp4" /></audio>';
		}
		function playsoundGallary(data)
		{	
			document.getElementById('preview_play_gallary_'+data).innerHTML='<audio controls="controls" style="border-radius: 20px;height: 30px; width:200px;"><source src="uploadSound/upload/'+data+'.aac" type="audio/mp4" /></audio>';
		}
	
		
	</script>



<?php } else {
	
	@header("Location: index.php");
    echo "<script>window.location='index.php'</script>";
    die;
    
} ?>
