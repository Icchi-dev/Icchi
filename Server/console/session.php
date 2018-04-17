<?php

// セッション管理クラス
class Session {

    // セッション開始
    static public function start(){
        session_start();
    }

    // セッションIDを生成しなおす
    static public function regenerate(){
        session_regenerate_id(true);
    }

    // セッション終了処理
    static public function destory(){
        Session::clear();
        session_destroy();
    }

    // ログイン設定
    static public function setLogin($loginId) {
        $loginKey = "loginKey";
        session::set($loginKey, $loginId);
    }

    // ログイン済み確認
    static public function isLogin() {
        $loginKey = "loginKey";
        $loginId = session::get($loginKey, "");
        if (strlen($loginId) <= 0) {
            return false;
        }
        return true;
    }

    //　session変数設定
    // @param string $key キー
    // @param mixed $value 設定する値
    static public function set($key, $value){
        $_SESSION[$key] = $value;
    }

    // セッション変数取得
    // @param string $key キー
    // @param mixed $default 存在しない場合のデフォルト値
    // @return string セッション変数値
    static public function get($key, $default = null){
        if(isset($_SESSION[$key])){
            return $_SESSION[$key];
        }
        return $default;
    }

    // セッション変数削除
    // @param string $key キー
    static public function remove($key){
        if(isset($_SESSION[$key])){
            unset($_SESSION[$key]);
        }
    }

    // セッション変数クリア
    static public function clear(){
        $_SESSION = array();
    }
}