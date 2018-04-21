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
			$str .= $user->fbId;
			$str .= ",";
			$str .= $user->image;
			$str .= ",";
			$str .= $user->fbLink;
			$str .= "\n";
		}
		file_put_contents(User::FILE_NAME, $str);
	}

	// FacebookID -> UserId変換
	static function fbIdToUserId($fbId) {

		if ((is_null($fbId)) || (strlen($fbId) == 0)) {
			return null;
		}
		$users = User::readAll();
		foreach ($users as $user) {
			if (strcmp($user->fbId, $fbId) == 0) {
				return $user->id;
			}
		}
		return null;
	}

	static function register($userId, $email, $password, $name, $fbId, $image, $fbLink) {

		$userData = $userId . "," . $email . "," . $password . "," . $name . ",,,,," . $fbId . "," . $image . "," . $fbLink . "\n";
		file_put_contents(User::FILE_NAME, $userData, FILE_APPEND);
	}

	static function login($email, $password) {

		$users = User::readAll();
		foreach ($users as $user) {
			if ((strcmp($user->email, $email) == 0) && (strcmp($user->password, $password) == 0)) {
				return $user->id;
			}
		}
		return null;
	}

	static function update($userId, $name, $age, $gender, $likes, $hates, $fbId, $image, $fbLink) {

		$users = User::readAll();
		foreach ($users as &$user) {
			if (strcmp($user->id, $userId) == 0) {
				$newUserData = new UserData();
				$newUserData->id = $userId;
				$newUserData->email = $user->email;
				$newUserData->password = $user->password;
				$newUserData->name = $name;
				$newUserData->age = $age;
				$newUserData->gender = $gender;
				$newUserData->likes = $likes;
				$newUserData->hates = $hates;
				$newUserData->fbId = $fbId;
				$newUserData->image = $image;
				$newUserData->fbLink = $fbLink;
				$user = $newUserData;
			}
		}
		User::writeAll($users);
	}
}


?>
