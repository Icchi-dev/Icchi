<?php

require "account.php";
require "item.php";
require "user.php";
require "parameter.php";
require "post.php";

$command = $_POST["command"];

if (strcmp($command, "login") == 0) {
  login();
} else if (strcmp($command, "getItem") == 0) {
  getItem();
} else if (strcmp($command, "deleteItem") == 0) {
  deleteItem();
} else if (strcmp($command, "getParameter") == 0) {
  getParameter();
} else if (strcmp($command, "postParameter") == 0) {
  postParameter();
} else if (strcmp($command, "getPost") == 0) {
  getPost();
} else if (strcmp($command, "deletePost") == 0) {
  deletePost();
} else if(strcmp($command, "postSortOrderUp") == 0) {
    postSortOrderUp();
} else if (strcmp($command, "postSortOrderDown") == 0) {
    postSortOrderDown();
} else if (strcmp($command, "createPost") == 0) {
    createPost();
} else if (strcmp($command, "editPost") == 0) {
    editPost();
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

  $itemDatas = Item::readAll();
  $userDatas = User::readAll();
  $items = [];
  foreach($itemDatas as $itemData) {
    $itemId = $itemData->id;
    $likeCount = 0;
    $hateCOunt = 0;
    foreach($userDatas as $userData) {
      $likes = explode("-", $userData->likes);
      foreach($likes as $like) {
        if (strcmp($like, $itemId) == 0) {
          $likeCount++;
        }
      }
      $hates = explode("-", $userData->hates);
      foreach($hates as $hate) {
        if (strcmp($hate, $itemId) == 0) {
          $hateCOunt++;
        }
      }
    }
    $items[] = Array("id" => $itemData->id,
                     "name" => $itemData->name,
                     "likeCount" => $likeCount,
                     "hateCount" => $hateCOunt);
  }
  echo(json_encode(Array("result" => "0",
                         "data" => $items)));
}

function deleteItem() {

  $id = $_POST["id"];
  Item::delete($id);
  echo(json_encode(Array("result" => "0")));
}

function getParameter() {
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

function postParameter() {
  $pointPerItem = $_POST["pointPerItem"];
  $pointPerMinorItem = $_POST["pointPerMinorItem"];
  $minorThreshold = $_POST["minorThreshold"];
  Parameter::write($pointPerItem, $pointPerMinorItem, $minorThreshold);
  $ret = Array("result" => "0");
  echo(json_encode($ret));
}

function getPost() {

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
	             "data" => $postList);
	echo(json_encode($ret));
}

function deletePost() {

  $id = $_POST["id"];
  Post::delete($id);
  echo(json_encode(Array("result" => "0")));
}

function postSortOrderUp() {

    $id = $_POST["id"];
    Post::setSortOrderUpById($id);
    echo(json_encode(Array("result" => "0")));
}

function postSortOrderDown() {

    $id = $_POST["id"];
    Post::setSortOrderDownById($id);
    echo(json_encode(Array("result" => "0")));
}

function createPost() {

  $postData = new PostData();
  $postData->id = POST::nextPostId();
  $postData->title = $_POST["title"];
  $postData->source = $_POST["source"];
  $postData->sumbnail = $_POST["sumbnail"];
  $postData->link = $_POST["link"];
  $postData->sortOrder = POST::nextSortOrder();

  POST::register($postData);
  echo(json_encode(Array("result" => "0")));
}

function editPost() {

  $id = $_POST["id"];
  $title = $_POST["title"];
  $source = $_POST["source"];
  $sumbnail = $_POST["sumbnail"];
  $link = $_POST["link"];

  POST::edit($id, $title, $source, $sumbnail, $link);
  echo(json_encode(Array("result" => "0")));
}

function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}


?>
