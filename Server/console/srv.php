<?php

require "Account.php";
require "post.php";
require 'session.php';

$command = $_POST["command"];

sessionStart();

if(strcmp($command, "webLogin") == 0) {
    webLogin();
} else if(strcmp($command, "getPost") == 0) {
	getPost();
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


function sessionStart() {

    $isLoging = Session::sessionExists();

    if (!$isLogin) {
        Session::start();
    }
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

function postSortOrderUp() {

    if (!isLogin()) {
        echo(json_encode(Array("result" => "1")));
        return;
    }

    $id = $_POST["id"];

    if ($id == null || $id === "") {
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
        return;
    }

    $posts = Post::readAll();
    $postData;
    foreach ($posts as $postData) {

        if (strcmp($post->id, $id) == 0) {
            $postData = Array("id" => $postData->id,
                "title" => $postData->title,
                "source" => $postData->source,
                "relates" => $postData->relates,
                "sumbnail" => $postData->sumbnail,
                "link" => $postData->link,
                "sortOrder" => $postData->sortOrder);
            break;
        }
    }

    $ret = Array("result" => "0",
        "post" => $postData);
    echo(json_encode($ret));
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
