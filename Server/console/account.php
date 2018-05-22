<?php

class AccountData {
  public $name;
  public $password;

  static function initFromFileString($line) {
    $datas = explode(",", $line);
    if (count($datas) == 2) {
        $accountData = new AccountData();
        $accountData->name = $datas[0];
        $accountData->password = $datas[1];
        return $accountData;
    }
    return null;
  }
}

class Account {

  const FILE_NAME = "data/account.txt";

  static function readAll() {

    if (file_exists(Account::FILE_NAME)) {
        $fileData = file_get_contents(Account::FILE_NAME);
        if ($fileData !== false) {
            $accountList = [];
            $lines = explode("\n", $fileData);
            for ($i = 0; $i < count($lines); $i++) {
                $accountData = AccountData::initFromFileString($lines[$i]);
                if (!is_null($accountData)) {
                    $accountList[] = $accountData;
                }
            }
            return $accountList;
        }
    }
    return [];
  }

  static function login($name, $password) {

    $accountList = Account::readAll();
    foreach ($accountList as $account) {
        if ((strcmp($account->name, $name) == 0) && (strcmp($account->password, $password) == 0)) {
            return true;
        }
    }
    return false;
  }
}

?>
