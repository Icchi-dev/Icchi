<?php

// セッション管理クラス
class Session {

    // セッション存在チェック
    static public function sessionExists(){
        if(isset($_COOKIE[session_name()])){
            return true;
        }
        return false;
    }

    // セッション開始
    static public function start(){
        session_start();
    }

    // セッションIDを生成しなおす
    static public function regenerate(){
        session_regenerate_id(true);
    }

    // クッキー削除要求処理
    static public function delCookie(){
        if($this->sessionExists()){
            $params = session_get_cookie_params();
            setcookie(session_name(), '', time() -4200,
                $params['path'], $params['domain'],
                $params['secure'], $params['httponly']);
        }
    }

    // セッション終了処理
    static public function endProc(){
        $this->clear();
        $this->delCookie();
        session_destroy();
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