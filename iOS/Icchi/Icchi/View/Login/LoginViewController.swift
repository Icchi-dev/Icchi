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
        
        let registerViewController = self.viewController(storyboard: "Main", identifier: "RegisterViewController") as! RegisterViewController
        self.stack(viewController: registerViewController, animationType: .horizontal)
    }
    
    @IBAction func onTapLogin(_ sender: Any) {

        let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
        self.stack(viewController: myPageViewController, animationType: .vertical)
    }
    
    // キーボード閉じる
    @IBAction func onDidEndOnExit(_ sender: Any) {
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

