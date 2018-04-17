<?php

require "Account.php";
require "post.php";
require 'session.php';

$command = $_POST["command"];

Session::start();

if(strcmp($command, "webLogin") == 0) {
    webLogin();
} else if(strcmp($command, "getPost") == 0) {
	getPost();
} else if (strcmp($command, "setPost") == 0) {
  setPost();
} else if (strcmp($command, "createPost") == 0) {
  createPost();
} else if(strcmp($command, "postSortOrderUp") == 0) {
    postSortOrderUp();
} else if (strcmp($command, "postSortOrderDown") == 0) {
    postSortOrderDown();
} else if (strcmp($command, "postDelete") == 0) {
    postDelete();
} else if (strcmp($command, "getPostById") == 0) {
    getPostById();
}


else {
  echo("unknown");
}

function setLogin($accountId) {

    $loginKey = "loginKey";
    session::set($loginKey, $accountId);
}
function isLogin() {

    $loginKey = "loginKey";
    $accountId = session::get($loginKey, "");
    if ($accountId === "") {
        return false;
    }
    return true;
}

function webLogin() {

    $id = $_POST["name"];
    $password = $_POST["password"];

    $accountId = Account::login($id, $password);
    if (!is_null($accountId)) {
        setLogin($accountId);
        echo(json_encode(Array("result" => "0")));
    } else {
        setLogin("");
        echo(json_encode(Array("result" => "1")));
    }
}

function getPost() {

    if (!isLogin()) {
        echo(json_encode(Array("result" => "1")));
        return;
    }

	$posts = Post::readAll();
	$postList = Array();
	foreach ($posts as $postData) {
	    $postList[] = Array("id" => $postData->id,
	                        "title" => $postData->title,
                            "source" => $postData->source,
                            "relates" => $postData->relates,
                            "sumbnail" => $postData->sumbnail,
                            "link" => $postData->link,
                            "sortOrder" => $postData->sortOrder);
	}
	$ret = Array("result" => "0",
	               "posts" => $postList);
	echo(json_encode($ret));
}

function setPost() {

  // TODO: セッション管理

  $id = $_POST["id"];
  $title = $_POST["title"];
  $source = $_POST["source"];
  $sumbnail = $_POST["sumbnail"];
  $link = $_POST["link"];

  if ($id == null || $id === "") {
    // TODO:
    echo("");
    return;
  }
  POST::edit($id, $title, $source, $sumbnail, $link);
  $ret = Array("result" => "0");
  echo(json_encode($ret));
}

function createPost() {

  $id = POST::nextPostId();
  $title = $_POST["title"];
  $source = $_POST["source"];
  $sumbnail = $_POST["sumbnail"];
  $link = $_POST["link"];

  POST::register($id, $title, $source, "", $sumbnail, $link, "0");
  $ret = Array("result" => "0");
  echo(json_encode($ret));
}

function postSortOrderUp() {

    if (!isLogin()) {
        echo(json_encode(Array("result" => "1")));
        return;
    }

    $id = $_POST["id"];

    if ($id == null || $id === "") {
      // TODO:
      echo("");
      return;
    }

    Post::setSortOrderUpById($id);

    $ret = Array("result" => "0");
    echo(json_encode($ret));
}

function postSortOrderDown() {

    if (!isLogin()) {
        echo(json_encode(Array("result" => "1")));
        return;
    }

    $id = $_POST["id"];

    if ($id == null || $id === "") {
        return;
    }

    Post::setSortOrderDownById($id);

    $ret = Array("result" => "0");
    echo(json_encode($ret));
}

function postDelete() {

    if (!isLogin()) {
        echo(json_encode(Array("result" => "1")));
        return;
    }

    $id = $_POST["id"];

    if ($id == null || $id === "") {
        return;
    }

    Post::delete($id);

    $ret = Array("result" => "0");
    echo(json_encode($ret));
}

function getPostById() {

  if (!isLogin()) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  $id = $_POST["id"];

  if ($id == null || $id === "") {
    echo("");   // TODO: どうしよう・・
    return;
  }

  $posts = Post::readAll();

  foreach($posts as $postData) {
    if (strcmp($postData->id, $id) == 0) {
      $retData = Array("id" => $postData->id,
                       "title" => $postData->title,
                       "source" => $postData->source,
                       "relates" => $postData->relates,
                       "sumbnail" => $postData->sumbnail,
                       "link" => $postData->link,
                       "sortOrder" => $postData->sortOrder);
      $ret = Array("result" => "0",
                   "post" => $retData);
      echo(json_encode($ret));
      return;
    }
  }

  echo("");   // TODO: どうしよう・・
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
