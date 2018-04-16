<?php

class PostData {
    public $id;
	public $title;
	public $source;
	public $relates;
	public $sumbnail;
	public $link;
	public $sortOrder;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 7) {
		    $postData = new PostData();
		    $postData->id = $datas[0];
			$postData->title = $datas[1];
			$postData->source = $datas[2];
			$postData->relates = $datas[3];
			$postData->sumbnail = $datas[4];
			$postData->link = $datas[5];
			$postData->sortOrder = $datas[6];
			return $postData;
		}
		return null;
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

				// 配列をソート
				usort($postList , "Post::cmp");

				return $postList;
			}
		}
		return [];
	}

	// ソートオーダーを昇順ソート
	static function cmp($a, $b)
	{
	    $cmp = strcmp($a->sortOrder, $b->sortOrder);
	    return $cmp;
	}


	static function writeAll($postList) {

	    $str = "";
	    foreach($postList as $postData) {

	        $str .= $postData->id;
	        $str .= ",";
	        $str .= $postData->title;
	        $str .= ",";
	        $str .= $postData->source;
	        $str .= ",";
	        $str .= $postData->relates;
	        $str .= ",";
	        $str .= $postData->sumbnail;
	        $str .= ",";
	        $str .= $postData->link;
	        $str .= ",";
	        $str .= $postData->sortOrder;
	        $str .= "\n";
	    }
	    file_put_contents(Post::FILE_NAME, $str);
	}

	static function register($id, $title, $source, $relates, $sumbnail, $link, $sortOrder) {

	    $postData = $id . "," . $title . "," . $source . "," . $relates . ","
	        . $sumbnail . "," . $link . "," . $sortOrder . "\n";
	        file_put_contents(Post::FILE_NAME, $postData, FILE_APPEND);
	}

	static function update($id, $title, $source, $relates, $sumbnail, $link, $sortOrder) {

	    $postList = Post::readAll();
	    foreach ($postList as &$post) {
	        if (strcmp($post->id, $id) == 0) {
	            $postData = new PostData();
	            $postData->id = $id;
	            $postData->title = $title;
	            $postData->source = $source;
	            $postData->relates = $relates;
	            $postData->sumbnail = $sumbnail;
	            $postData->link = $link;
	            $postData->sortOrder = $sortOrder;
	            $post = $postData;
	        }
	    }
	    Post::writeAll($postList);
	}

  static function edit($id, $title, $source, $sumbnail, $link) {

      $postList = Post::readAll();
      foreach ($postList as &$post) {
          if (strcmp($post->id, $id) == 0) {
              $postData = new PostData();
              $postData->id = $id;
              $postData->title = $title;
              $postData->source = $source;
              $postData->sumbnail = $sumbnail;
              $postData->link = $link;
              $post = $postData;
          }
      }
      Post::writeAll($postList);
  }

	// 指定するIDを一つ上のソート番号にする
	static function setSortOrderUpById($id) {

	    $orgSortOrder = 1;
	    $upperSortOrder = 1;
	    $upperId = 1;
	    $postList = Post::readAll();
	    foreach ($postList as &$post) {

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

	// 指定するIDを一つ下のソート番号にする
	static function setSortOrderDownById($id) {

	    $orgSortOrder = 1;
	    $postList = Post::readAll();
	    $reversed = array_reverse($postList);
	    $end = $reversed[0];
	    $downId = $end->id;
	    $downrSortOrder = $end->sortOrder;

	    foreach ($reversed as &$post) {

	        if (strcmp($post->id, $id) == 0) {

	            $orgSortOrder = $post->sortOrder;
	            break;
	        }
	        $downId = $post->id;
	        $downrSortOrder = $post->sortOrder;
	    }

	    if ($orgSortOrder != $downrSortOrder) {
	        Post::updateSortOrder($id, $downrSortOrder);
	        Post::updateSortOrder($downId, $orgSortOrder);
	    }
	}

	// ソート番号だけ更新する
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


	// 指定するIDを削除
	static function delete($id) {

	    $postList = Post::readAll();

	    $newPostList = [];

	    foreach ($postList as &$post) {
	        if (strcmp($post->id, $id) == 0) {
	            continue;
	        }
            $postData = new PostData();
            $postData->id = $post->id;
            $postData->title = $post->title;
            $postData->source = $post->source;
            $postData->relates = $post->relates;
            $postData->sumbnail = $post->sumbnail;
            $postData->link = $post->link;
            $postData->sortOrder = $post->sortOrder;
            $newPostList[] = $postData;

	    }
	    Post::writeAll($newPostList);
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
}


?>
