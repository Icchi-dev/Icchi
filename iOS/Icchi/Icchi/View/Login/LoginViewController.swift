//
//  LoginViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit

class LoginViewController: KeyboardRespondableViewController, UITextFieldDelegate {

    @IBOutlet private weak var contentsViewCenterYConstraint: NSLayoutConstraint!
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    @IBOutlet private weak var loginButtonView: UIView!
    @IBOutlet weak var registBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // ボタンの丸み
        let path = UIBezierPath(roundedRect: registBtn.bounds
            , byRoundingCorners: [.topLeft, .topRight]
            , cornerRadii: CGSize(width: 15, height: 15))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        registBtn.layer.mask = mask
        
        self.emailTextField.delegate = self
        self.passwordTextField.delegate = self
        
        // facebookログイン作成
        let loginButton = FBSDKLoginButton()
        loginButton.delegate = self
        self.view.addSubview(loginButton)
        loginButton.translatesAutoresizingMaskIntoConstraints = false
        loginButton.topAnchor.constraint(equalTo: self.loginButtonView.topAnchor).isActive = true
        loginButton.leadingAnchor.constraint(equalTo: self.loginButtonView.leadingAnchor).isActive = true
        loginButton.trailingAnchor.constraint(equalTo: self.loginButtonView.trailingAnchor).isActive = true
        loginButton.bottomAnchor.constraint(equalTo: self.loginButtonView.bottomAnchor).isActive = true
        
    }
    
    
    override func animate(with: KeyboardAnimation) {
        
        if with.height > 0 {
            guard let textField = ([emailTextField, passwordTextField].filter { $0.isFirstResponder }).first,
                let window = UIApplication.shared.keyWindow else {
                return
            }
            let rect = textField.absoluteRect()
            let bottom =  rect.origin.y + rect.size.height
            if window.frame.size.height - bottom < with.height {
                self.contentsViewCenterYConstraint.constant = with.height - window.frame.size.height + bottom + 30
            }
        } else {
            self.contentsViewCenterYConstraint.constant = 0
        }
        
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: { [weak self] in
            self?.view.layoutIfNeeded()
        })
    }
    
    // 新規登録ボタン
    @IBAction func onTapRegister(_ sender: Any) {
        
        let registerViewController = self.viewController(storyboard: "Main", identifier: "RegisterViewController") as! RegisterViewController
        self.stack(viewController: registerViewController, animationType: .horizontal)
    }
    
    // ログインボタン
    @IBAction func onTapLogin(_ sender: Any) {

        self.login()
    }
    
    // キーボードリターン
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        textField.resignFirstResponder()
        if textField == self.emailTextField {
            self.passwordTextField.becomeFirstResponder()
        } else {
            return true
        }
        return false
    }
    
    // キーボードが出ている状態で、キーボード以外をタップしたらキーボードを閉じる
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // 非表示にする
        [self.emailTextField,self.passwordTextField].forEach({ textField in
            if textField!.isFirstResponder {
                textField!.resignFirstResponder()
            }
        })
    }
    
    // ログイン
    func login() {
        
        let email = self.emailTextField!.text!
        let password = self.passwordTextField!.text!
        
        // 入力チェック
        guard !email.isEmpty && !password.isEmpty else {
            
            let action = AlertAction(title:"OK")
            self.showAlert(title: "エラー", message: "入力エラー", actions: [action])
            return;
        }
        
        // ローディング
        Loading.start()
        
        AccountRequester.sharedManager.login(email: email, password: password) { (result:Bool, userId:String?) in
            
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
                let action = AlertAction(title:"リトライ", action: {(_)in self.fetch()})
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
                return;
            }
            
            // マイページへ移動
            let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
            self.stack(viewController: myPageViewController, animationType: .horizontal)
        }
    }
}

extension LoginViewController: FBSDKLoginButtonDelegate {
    
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        
    }
    
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        
    }
    
    func loginButtonWillLogin(_ loginButton: FBSDKLoginButton!) -> Bool {
        return true
    }
}

