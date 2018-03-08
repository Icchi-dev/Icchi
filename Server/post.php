<?php

class PostData {
	public $title;
	public $source;
	public $relates;
	public $sumbnail;
	public $link;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 5) {
			$postData = new PostData();
			$postData->title = $datas[0];
			$postData->source = $datas[1];
			$postData->relates = $datas[2];
			$postData->sumbnail = $datas[3];
			$postData->link = $datas[4];
			return $postData;
		}
		return null;
	}
}

class Post {
	const FILE_NAME = "data/post.txt";

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
