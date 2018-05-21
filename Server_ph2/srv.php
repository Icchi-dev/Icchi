<?php

require "account.php";
require "item.php";

$command = $_POST["command"];

if(strcmp($command, "login") == 0) {
  login();
} else if(strcmp($command, "getItem") == 0) {
  getItem();
} else if(strcmp($command, "deleteItem") == 0) {
  deleteItem();
}
else {
  echo("unknown");
}

function login() {

  $name = $_POST["name"];
  $password = $_POST["password"];

  if (Account::login($name, $password)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getItem() {

  $itemList = Item::readAll();
}

function deleteItem() {

}







function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}


?>
