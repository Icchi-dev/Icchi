<?php

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
} else {
  echo("unknown");
}

function register() {

	$email = $_POST["email"];
	$password = $_POST["password"];
	$userId = createUserId();

	$userData = $userId . "," . $email . "," . $password . ",,,,,\n";
	$fileName = "data/user.txt";
	if (file_put_contents($fileName, $userData, FILE_APPEND) !== false) {
		echo(json_encode(Array("result" => "0",
													"userId" => $userId)));
		return;
	}
	echo(json_encode(Array("result" => "1")));
}

function login() {

	$email = $_POST["email"];
	$password = $_POST["password"];

	$fileName = "data/user.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$users = explode("\n", $fileData);
			for ($i = 0; $i < count($users); $i++) {
				$userDatas = explode(",", $users[$i]);
				if (count($userDatas) >= 3) {
					if ((strcmp($userDatas[1], $email) == 0)
					&& (strcmp($userDatas[2], $password) == 0)) {
						echo(json_encode(Array("result" => "0",
																		"userId" => $userDatas[0])));
						return;
					}
				}
			}
		}
	}
	echo(json_encode(Array("result" => "1")));
}

function updateAccount() {

	$userId = $_POST["userId"];
	$name = decodeBase64($_POST["name"]);
	$age = $_POST["age"];
	$gender = $_POST["gender"];
	$likes = $_POST["likes"];
	$hates = $_POST["hates"];
	$image = $_POST["image"];
	$fblink = $_POST["fbLink"];

	$fileName = "data/user.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$users = explode("\n", $fileData);

			$newUsers = Array();
			for ($i = 0; $i < count($users); $i++) {
				$userDatas = explode(",", $users[$i]);
				if (count($userDatas) >= 1) {
					if (strcmp($userDatas[0], $userId) == 0) {
						$newUsers[] = $userId . "," . $name . "," . $age . "," . $gender . "," . $likes . "," . $hates . "," . $image . "," . $fblink;
						continue;
					}
				}
				$newUsers[] = $users[$i];
			}
			if ($newIndex >= 0) {
				file_put_contents($fileName, implode("\n", $newUsers));
				echo(json_encode(Array("result" => "0")));
				return;
			}
		}
	}
	echo(json_encode(Array("result" => "1")));
}

function getUser() {

	$fileName = "data/user.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$userList = Array();
			$users = explode("\n", $fileData);
			for ($i = 0; $i < count($users); $i++) {
				$userDatas = explode(",", $users[$i]);
				if (count($userDatas) >= 8) {
					$userList[] = Array("userId" => $userDatas[0],
														"name" => $userDatas[1],
														"age" => $userDatas[2],
														"gender" => $userDatas[3],
														"likes" => $userDatas[4],
														"hates" => $userDatas[5],
														"image" => $userDatas[6],
														"fbLink" => $userDatas[7]);
				}
			}
			$ret = Array("result" => "0",
									 "users" => $userList);
			echo(json_encode($ret));
			return;
		}
	}
	echo(json_encode(Array("result" => "1")));
}

function getItem() {

	$fileName = "data/item.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$itemList = Array();
			$items = explode("\n", $fileData);
			for ($i = 0; $i < count($items); $i++) {
				$itemDatas = explode(",", $items[$i]);
				if (count($itemDatas) >= 2) {
					$itemList[] = Array("itemId" => $itemDatas[0],
														"name" => $itemDatas[1]);
				}
			}
			$ret = Array("result" => "0",
									 "items" => $itemList);
			echo(json_encode($ret));
			return;
		}
	}
	echo(json_encode(Array("result" => "1")));
}

function getPost() {

	$fileName = "data/post.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$postList = Array();
			$posts = explode("\n", $fileData);
			for ($i = 0; $i < count($posts); $i++) {
				$postDatas = explode(",", $posts[$i]);
				if (count($postDatas) >= 2) {
					$postList[] = Array("title" => $postDatas[0],
															"source" => $postDatas[1],
															"relates" => $postDatas[2],
															"sumbnail" => $postDatas[3],
															"link" => $postDatas[4]);
				}
			}
			$ret = Array("result" => "0",
									 "posts" => $postList);
			echo(json_encode($ret));
			return;
		}
	}
	echo(json_encode(Array("result" => "1")));
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
