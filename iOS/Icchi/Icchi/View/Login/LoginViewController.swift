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

class LoginViewController: KeyboardRespondableViewController {

    @IBOutlet weak var loginIndicator: UIActivityIndicatorView!
    
    private enum FetchResult: Int {
        case ok
        case error
        case progress
    }
    private var fetchUserResult = FetchResult.progress;
    private var fetchItemResult = FetchResult.progress;
    private var fetchPostResult = FetchResult.progress;
    
    @IBOutlet private weak var contentsViewCenterYConstraint: NSLayoutConstraint!
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    @IBOutlet private weak var loginButtonView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // facebookログイン作成
        let loginButton = FBSDKLoginButton()
        loginButton.delegate = self
        self.view.addSubview(loginButton)
        loginButton.translatesAutoresizingMaskIntoConstraints = false
        loginButton.topAnchor.constraint(equalTo: self.loginButtonView.topAnchor).isActive = true
        loginButton.leadingAnchor.constraint(equalTo: self.loginButtonView.leadingAnchor).isActive = true
        loginButton.trailingAnchor.constraint(equalTo: self.loginButtonView.trailingAnchor).isActive = true
        loginButton.bottomAnchor.constraint(equalTo: self.loginButtonView.bottomAnchor).isActive = true
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            self.fetch()
        }
    }
    
    /* データ取得 */
    private func fetch() {
        
        loginIndicator.isHidden = false;
        loginIndicator.startAnimating()
        
        // ユーザー一覧取得
        UserRequester.sharedManager.fetch() { (result) in
            if result {self.fetchUserResult = FetchResult.ok}
            else      {self.fetchUserResult = FetchResult.error}
            self.checkResult()
        }
        // アイテム一覧取得
        ItemRequester.sharedManager.fetch() { (result) in
            if result {self.fetchItemResult = FetchResult.ok}
            else      {self.fetchItemResult = FetchResult.error}
            self.checkResult()
        }
        // アイテム一覧取得
        PostRequester.sharedManager.fetch() { (result) in
            if result {self.fetchPostResult = FetchResult.ok}
            else      {self.fetchPostResult = FetchResult.error}
            self.checkResult()
        }
    }
    
    private func checkResult() {
        
        // 受信待ち
        if (self.fetchUserResult == .progress
            || self.fetchItemResult == .progress
            || self.fetchPostResult == .progress) {
            return
        }
        
        // インジケータ解除
        loginIndicator.stopAnimating()
        loginIndicator.isHidden = true;
        
        // 受信チェック
        if (self.fetchUserResult == .error
            || self.fetchItemResult == .error
            || self.fetchPostResult == .error) {
            
            let alert = UIAlertController(title:"エラー", message:"通信に失敗しました", preferredStyle:.alert)
            let cancelAction = UIAlertAction(title: "リトライ", style: .default) {(tapAction) in
                self.fetch()
            }
            alert.addAction(cancelAction)
            present(alert, animated: true, completion: nil)
        }

//        SaveData saveData = SaveData.getInstance();
//        if (saveData.userId.length() > 0) {
//            MyPageFragment fragment = new MyPageFragment();
//            FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.vertical);
//        } else {
//            LoginFragment fragment = new LoginFragment();
//            FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.vertical);
//        }
        
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
    
    @IBAction func onTapRegister(_ sender: Any) {
        
    }
    
    @IBAction func onTapLogin(_ sender: Any) {

        let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
        self.stack(viewController: myPageViewController, animationType: .vertical)
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

