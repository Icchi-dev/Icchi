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
	public $image;
	public $fbLink;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 10) {
			$userData = new UserData();
			$userData->id = $datas[0];
			$userData->email = $datas[1];
			$userData->password = $datas[2];
			$userData->name = $datas[3];
			$userData->age = $datas[4];
			$userData->gender = $datas[5];
			$userData->likes = $datas[6];
			$userData->hates = $datas[7];
			$userData->image = $datas[8];
			$userData->fbLink = $datas[9];
			return $userData;
		}
		return null;
	}
}

class User {
	const FILE_NAME = "data/user.txt";

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

	static function writeAll($userList) {

		$str = "";
		foreach($userList as $user) {
//			$str .= $user->toFileString();
			$str .= $user->id;
			$str .= ",";
			$str .= $user->email;
			$str .= ",";
			$str .= $user->password;
			$str .= ",";
			$str .= $user->name;
			$str .= ",";
			$str .= $user->age;
			$str .= ",";
			$str .= $user->gender;
			$str .= ",";
			$str .= $user->likes;
			$str .= ",";
			$str .= $user->hates;
			$str .= ",";
			$str .= $user->image;
			$str .= ",";
			$str .= $user->fbLink;
			$str .= "\n";
		}
		file_put_contents(User::FILE_NAME, $str);
	}

	static function update($userId, $name, $age, $gender, $likes, $hates, $image, $fbLink) {

		$users = User::readAll();
		foreach ($users as &$user) {
			if (strcmp($user->id, $userId) == 0) {
				$newUserData = new UserData();
				$newUserData = $userId;
				$newUserData->email = $user->email;
				$newUserData->password = $user->password;
				$newUserData->name = $user->name;
				$newUserData->age = $user->age;
				$newUserData->gender = $user->gender;
				$newUserData->likes = $user->likes;
				$newUserData->hates = $user->hates;
				$newUserData->image = $user->image;
				$newUserData->fbLink = $user->fbLink;
				$user = $newUserData;
			}
		}
		User::writeAll($users);
	}
}


?>
