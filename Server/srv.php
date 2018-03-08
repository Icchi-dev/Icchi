<?php

require "user.php";
require "item.php";
require "post.php";
require "parameter.php";

$command = $_POST["command"];

if(strcmp($command, "register") == 0) {
	register();
} else if(strcmp($command, "login") == 0) {
	login();
} else if(strcmp($command, "updateAccount") == 0) {
	updateAccount();
} else if(strcmp($command, "getUser") == 0) {
	getUser();
} else if (strcmp($command, "getItem") == 0) {
	getItem();
} else if(strcmp($command, "getPost") == 0) {
	getPost();
} else if(strcmp($command, "createItem") == 0) {
	createItem();
} else if (strcmp($command, "getMatchParameter") == 0) {
	getMatchParameter();
} else {
	login();
  echo("unknown");
}

function register() {

	$email = $_POST["email"];
	$password = $_POST["password"];
	$name = $_POST["name"];
	$image = $_POST["image"];
	$fbLink = $_POST["fbLink"];

	$userId = createUserId();

	User::register($userId, $email, $password, $name, $image, $fbLink);
	echo(json_encode(Array("result" => "0",
													"userId" => $userId)));
}

function login() {

	$email = $_POST["email"];
	$password = $_POST["password"];

	$userId = User::login($email, $password);
	if (is_null($userId)) {
		echo(json_encode(Array("result" => "0",
														"userId" => $userId)));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function updateAccount() {

	$userId = $_POST["userId"];
	$name = $_POST["name"];
	$age = $_POST["age"];
	$gender = $_POST["gender"];
	$likes = $_POST["likes"];
	$hates = $_POST["hates"];
	$image = $_POST["image"];
	$fblink = $_POST["fbLink"];

	User::update($userId, $name, $age, $gender, $likes, $hates, $image, $fbLink);
	echo(json_encode(Array("result" => "0")));
}

function getUser() {

	$users = User::readAll();
	$userList = Array();
	foreach ($users as $userData) {
		$userList[] = Array("userId" => $userData->id,
												"name" => $userData->name,
												"age" => $userData->age,
												"gender" => $userData->gender,
												"likes" => $userData->likes,
												"hates" => $userData->hates,
												"image" => $userData->image,
												"fbLink" => $userData->fbLink);
	}
	$ret = Array("result" => "0",
							 "users" => $userList);
	echo(json_encode($ret));
}

function getItem() {

	$data = [];
	$items = Item::readAll();
	foreach($items as $item) {
		$data[] = Array(
			"itemId" => $item->id,
			"name" => $item->name,
			"kana" => $item->kana
		);
	}
	echo(json_encode(Array("result" => "0",
												"items" => $data)));
}

function getPost() {

	$posts = Post::readAll();
	$postList = Array();
	foreach ($posts as $postData) {
		$postList[] = Array("title" => $postData->title,
												"source" => $postData->source,
												"relates" => $postData->relates,
												"sumbnail" => $postData->sumbnail,
												"link" => $postData->link);
	}
	$ret = Array("result" => "0",
							 "posts" => $postList);
	echo(json_encode($ret));
}

function createItem() {

	$itemName = $_POST["itemName"];
	$itemId = Item::append($itemName);
	echo(json_encode(Array("result" => "0", "itemId" => $itemId)));
}

function getMatchParameter() {

	$parameter = Parameter::read();
	if (!is_null($parameter)) {
		$ret = Array("result" => "0",
								 "pointPerItem" => $parameter->pointPerItem,
								 "pointPerMinorItem" => $parameter->pointPerMinorItem,
								 "minorThreshold" => $parameter->minorThreshold);
		echo(json_encode($ret));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function createUserId() {

	date_default_timezone_set('Asia/Tokyo');

	static $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLKMNOPQRSTUVWXYZ0123456789";
	$str = "";
  	for ($i = 0; $i < 8; $i++) {
    	$str .= $chars[mt_rand(0, strlen($chars) - 1)];
  	}
	return date("YmdHis") . "_" . $str;
}

function decodeBase64($str) {
	$val = str_replace(array("_", "-", " "), array("+", "/", "+"), $str);
	return base64_decode($val);
}

function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}


?>
