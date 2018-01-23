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

class LoginViewController: UIViewController {

    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        let loginButton = FBSDKLoginButton()
        loginButton.frame = CGRect(x: 30, y: self.view.frame.size.height - 60, width: self.view.frame.size.width - 60, height: 40)
        loginButton.delegate = self
        self.view.addSubview(loginButton)
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

