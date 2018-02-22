//
//  RegisterViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/02/20.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class RegisterViewController: KeyboardRespondableViewController, UITextFieldDelegate {

    @IBOutlet weak var textMail: UITextField!
    @IBOutlet weak var textPass: UITextField!
    @IBOutlet weak var textName: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.textMail.delegate = self
        self.textPass.delegate = self
        self.textName.delegate = self
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // ログインボタンタップ
    @IBAction func onLogin(_ sender: Any) {
        register()
    }
    
    // キャンセルボタンタップ
    @IBAction func onCancel(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    // キーボードリターン
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        textField.resignFirstResponder()
        if textField == self.textMail {
            self.textPass.becomeFirstResponder()
        } else if textField == self.textPass {
            self.textName.becomeFirstResponder()
        } else {
            return true
        }
        return false
    }
    
    // キーボードが出ている状態で、キーボード以外をタップしたらキーボードを閉じる
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // 非表示にする
        [self.textMail,self.textPass,self.textName].forEach({ textField in
            if textField!.isFirstResponder {
                textField!.resignFirstResponder()
            }
        })
    }
    
    // 登録
    func register() {
        
        let email = self.textMail!.text!
        let password = self.textPass!.text!
        let name = self.textName!.text!
        
        // 入力チェック
        guard !email.isEmpty && !password.isEmpty && !name.isEmpty else {
            
            let action = AlertAction(title:"OK")
            self.showAlert(title: "エラー", message: "入力エラー", actions: [action])
            return;
        }
        
        // ローディング
        Loading.start()
        
        AccountRequester.sharedManager.register(email: email, password: password, name: name, image: "", fbLink: "") { (result:Bool, userId:String?) in
            
            // ローディング
            Loading.stop()
            
            // 入力チェック
            guard result, let userId = userId else {
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: "新規登録に失敗しました", actions: [action])
                return;
            }
            
            // ユーザーID登録
            SaveData.shared.userId = userId;
            SaveData.shared.save()
            
            // データ更新
            self.fetch()
        }
    }
    
    // 情報更新
    func fetch() -> Void {
        
        // ローディング
        Loading.start()
        
        // ユーザー一覧取得
        UserRequester.sharedManager.fetch() { (result) in
            
            // ローディング
            Loading.stop()
            
            // 入力チェック
            guard result else {
                let action = AlertAction(title:"リトライ", action: {()in self.fetch()})
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
                return;
            }
            
            // マイページへ移動
            let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
            self.stack(viewController: myPageViewController, animationType: .horizontal)
        }
    }
}
