<?php

class PostData {
  public $id;
	public $title;
	public $source;
	public $relates;
	public $sumbnail;
	public $link;
	public $sortOrder;
  public $forAll;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 8) {
      $postData = new PostData();
		  $postData->id = $datas[0];
			$postData->title = $datas[1];
			$postData->source = $datas[2];
			$postData->relates = $datas[3];
			$postData->sumbnail = $datas[4];
			$postData->link = $datas[5];
			$postData->sortOrder = $datas[6];
      $postData->forAll = $datas[7];
			return $postData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->title;
    $str .= ",";
    $str .= $this->source;
    $str .= ",";
    $str .= $this->relates;
    $str .= ",";
    $str .= $this->sumbnail;
    $str .= ",";
    $str .= $this->link;
    $str .= ",";
    $str .= $this->sortOrder;
    $str .= ",";
    $str .= $this->forAll;
    $str .= "\n";
    return $str;
  }
}

class Post {
	const FILE_NAME = "../data/post.txt";

	static function readAll() {

		if (file_exists(Post::FILE_NAME)) {
			$fileData = file_get_contents(Post::FILE_NAME);
			if ($fileData !== false) {
				$postList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$postData = PostData::initFromFileString($lines[$i]);
					if (!is_null($postData)) {
						$postList[] = $postData;
					}
				}
				usort($postList , "Post::cmp");
				return $postList;
			}
		}
		return [];
	}

	static function cmp($a, $b) {

    $aval = intval($a->sortOrder);
    $bval = intval($b->sortOrder);

    if ($aval < $bval) {
      return -1;
    } else if ($aval > $bval) {
      return 1;
    } else {
      return 0;
    }
	}

	static function writeAll($postList) {

	    $str = "";
	    foreach($postList as $postData) {
        $str .= $postData->toFileString();
	    }
	    file_put_contents(Post::FILE_NAME, $str);
	}

	static function register($postData) {
    file_put_contents(Post::FILE_NAME, $postData->toFileString(), FILE_APPEND);
	}

  static function edit($id, $title, $source, $sumbnail, $link, $forAll) {

    $postList = Post::readAll();
    foreach ($postList as &$post) {
      if (strcmp($post->id, $id) == 0) {
        $postData = new PostData();
        $postData->id = $id;
        $postData->title = $title;
        $postData->source = $source;
        $postData->sumbnail = $sumbnail;
        $postData->link = $link;
        $postData->forAll = $forAll;
        $postData->sortOrder = $post->sortOrder;
        $post = $postData;
      }
    }
    Post::writeAll($postList);
  }

	static function setSortOrderUpById($id) {

    $orgSortOrder = 1;
	  $upperSortOrder = 1;
    $upperId = 1;
    $postList = Post::readAll();
    foreach ($postList as $post) {
      if (strcmp($post->id, $id) == 0) {
        $orgSortOrder = $post->sortOrder;
	      break;
	    }
	    $upperId = $post->id;
      $upperSortOrder = $post->sortOrder;
	   }
     if ($orgSortOrder != $upperSortOrder) {
       Post::updateSortOrder($id, $upperSortOrder);
       Post::updateSortOrder($upperId, $orgSortOrder);
	   }
	}

	static function setSortOrderDownById($id) {

    $orgSortOrder = 1;
    $postList = Post::readAll();
    $reversed = array_reverse($postList);
    $end = $reversed[0];
    $downId = $end->id;
    $downerSortOrder = $end->sortOrder;

    foreach ($reversed as &$post) {
      if (strcmp($post->id, $id) == 0) {
        $orgSortOrder = $post->sortOrder;
        break;
      }
      $downId = $post->id;
      $downerSortOrder = $post->sortOrder;
    }
    if ($orgSortOrder != $downerSortOrder) {
      Post::updateSortOrder($id, $downerSortOrder);
      Post::updateSortOrder($downId, $orgSortOrder);
    }
	}

	static function updateSortOrder($id,$sortOrder) {

    $postList = Post::readAll();
    foreach ($postList as &$post) {
      $postData = new PostData();
      $postData->id = $post->id;
	    $postData->title = $post->title;
	    $postData->source = $post->source;
      $postData->relates = $post->relates;
	    $postData->sumbnail = $post->sumbnail;
	    $postData->link = $post->link;
	    $postData->sortOrder = $post->sortOrder;
	    if (strcmp($post->id, $id) == 0) {
        $postData->sortOrder = $sortOrder;
	    }
	    $post = $postData;
	  }
	  Post::writeAll($postList);
	}

	static function delete($id) {

    $postList = Post::readAll();
    $newPostList = [];
    foreach ($postList as $postData) {
      if (strcmp($postData->id, $id) != 0) {
        $newPostList[] = $postData;
	    }
	    Post::writeAll($newPostList);
    }
	}

  static function nextPostId() {

    $maxId = 0;

    $postList = Post::readAll();
    foreach ($postList as $post) {
      $intId = intval($post->id);
      if ($intId > $maxId) {
        $maxId = $intId;
      }
    }
    return strval($maxId + 1);
  }

  static function nextSortOrder() {

    $maxOrder = 0;

    $postList = Post::readAll();
    foreach ($postList as $post) {
      $intOrder = intval($post->sortOrder);
      if ($intOrder > $maxOrder) {
        $maxOrder = $intOrder;
      }
    }
    return strval($maxOrder + 1);
  }
}


?>
