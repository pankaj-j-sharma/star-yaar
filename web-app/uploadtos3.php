<?php

function uploadToStorage($filename,$rawdata,$sub=""){
	//echo "here....";
	// Include the SDK using the composer autoloader
	require 'vendor/autoload.php';

	$AWS_ACCESS_KEY_ID = "AKIA6E2NPJ5RKB4RIG42";
	$AWS_SECRET_KEY = "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";
	$AWS_REGION = "ap-south-1";
	$AWS_BUCKET_NAME = "myottappbucket";
	$AWS_S3_ENDPOINT = "http://myottappbucket.s3-website.ap-south-1.amazonaws.com";

	$s3 = new Aws\S3\S3Client([
			'region'  => $AWS_REGION,
			'version' => 'latest',
			'credentials' => [
				'key'    => $AWS_ACCESS_KEY_ID,
				'secret' => $AWS_SECRET_KEY,
			]
	]);

	// Send a PutObject request and get the result object.
	$key = 'StarYaar/upload/'.$sub;
	//$filename='20.mp4';

	$result = $s3->putObject([
			'Bucket' => $AWS_BUCKET_NAME,
			'Key'    => $key.$filename,
			'Body'   => $rawdata,
			//'ACL' => 'public-read',
			'ContentType' => 'video/mp4'
			 //'SourceFile' => '/home/ubuntu/php/upload/video/'.$filename
]);
}

// Print the body of the result by indexing into the result object.
//var_dump($result);

?>
