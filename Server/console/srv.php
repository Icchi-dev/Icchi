<?php

$command = $_POST["command"];

if(strcmp($command, "login") == 0) {
	login();
} else {
  echo("unknown");
}

function login() {

	$name = $_POST["name"];
	$password = $_POST["password"];

	if ((strpos($name, ",") !== false) || (strpos($password, ",") !== false)) {
		echo("1");
		return;
	}

	$fileName = "data/account.txt";
	if (file_exists($fileName)) {
		$fileData = file_get_contents($fileName);
		if ($fileData !== false) {
			$lines = explode("\n", $fileData);
			for ($i = 0; $i < count($lines); $i++) {
				$datas = explode(",", $lines[$i]);
				if (count($datas) == 3) {
					if ((strcmp($datas[0], $name) == 0) && (strcmp($datas[1], $password) == 0)) {
						echo("0");
						return;
					}
				}
			}
		}
	}
	echo("1");
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
