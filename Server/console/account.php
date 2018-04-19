<?php

class AccountData {
    public $id;
    public $password;
    public $powerLevel;

    static function initFromFileString($line) {
        $datas = explode(",", $line);
        if (count($datas) == 3) {
            $accountData = new AccountData();
            $accountData->id = $datas[0];
            $accountData->password = $datas[1];
            $accountData->powerLevel = $datas[2];
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

    static function writeAll($accountList) {

        $str = "";
        foreach($accountList as $account) {
            $str .= $account->id;
            $str .= ",";
            $str .= $account->password;
            $str .= ",";
            $str .= $account->powerLevel;
            $str .= "\n";
        }
        file_put_contents(Account::FILE_NAME, $str);
    }

    static function register($id, $password, $powerLevel) {

        $accountData = $id . "," . $password . "," . $powerLevel . "\n";
        file_put_contents(Account::FILE_NAME, $accountData, FILE_APPEND);
    }

    static function login($id, $password) {

        $accountList = Account::readAll();
        foreach ($accountList as $account) {
            if ((strcmp($account->id, $id) == 0) && (strcmp($account->password, $password) == 0)) {
                return $account->id;
            }
        }
        return null;
    }

    static function update($id, $password, $powerLevel) {

        $accountList = $powerLevel::readAll();
        foreach ($accountList as &$account) {
            if (strcmp($account->id, $id) == 0) {
                $accountData = new AccountData();
                $accountData->id = $id;
                $accountData->password = $password;
                $accountData->powerLevel = $powerLevel;
                $account = $accountData;
            }
        }
        User::writeAll($accountList);
    }
}


?>
