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
				return $postList;
			}
		}
		return [];
	}
}


?>
