<?php

class UserData {
	public $id;
	public $email;
	public $password;
	public $name;
	public $age;
	public $gender;
	public $likes;
	public $hates;
	public $fbId;
	public $image;
	public $fbLink;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 11) {
			$userData = new UserData();
			$userData->id = $datas[0];
			$userData->email = $datas[1];
			$userData->password = $datas[2];
			$userData->name = $datas[3];
			$userData->age = $datas[4];
			$userData->gender = $datas[5];
			$userData->likes = $datas[6];
			$userData->hates = $datas[7];
			$userData->fbId = $datas[8];
			$userData->image = $datas[9];
			$userData->fbLink = $datas[10];
			return $userData;
		}
		return null;
	}
}

class User {
	const FILE_NAME = "../data/user.txt";

	static function readAll() {

		if (file_exists(User::FILE_NAME)) {
			$fileData = file_get_contents(User::FILE_NAME);
			if ($fileData !== false) {
				$userList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$userData = UserData::initFromFileString($lines[$i]);
					if (!is_null($userData)) {
						$userList[] = $userData;
					}
				}
				return $userList;
			}
		}
		return [];
	}
}


?>
