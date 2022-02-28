<?php 
require_once("config.php"); 
if( isset($_SESSION['id']) ){ ?>

<h2 class="title">Change Password</h2>
<div class="form">
	<div class="left col50">
		
		<div class="form">
			<form action="dashboard.php?p=change_password&updatepass=ok" id="changepass" method="post">
				<p style="margin-bottom: 30px;"><input type="password" id="oldpas" name="oldpas" required>
					<label alt="Old Password" placeholder="Old Password">
				</p>
				<p style="margin-bottom: 30px;"><input type="password" id="newpas" name="newpas" required> 
					<label alt="New Password" placeholder="New Password">
				</p>
				<p style="margin-bottom: 30px;"><input type="password"  id="renewpas" name="renewpas" required>
					<label alt="Re-type New Password" placeholder="Re-type New Password">
				</p>
				<p><input type="submit" value="Update Password" class="buttonColor"></p>
			
			</form>
		</div>
	</div>
	<div class="right col40">
		
	</div>
	<div class="clear"></div>
</div>

<?php } else {
	
	@header("Location: index.php");
    echo "<script>window.location='index.php'</script>";
    die;
    
} ?>