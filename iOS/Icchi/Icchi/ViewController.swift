//
//  ViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit

class ViewController: UIViewController {


    override func viewDidLoad() {
        super.viewDidLoad()
        
        let loginButton = FBSDKLoginButton()
        loginButton.frame = CGRect(x: 10, y: 100, width: 200, height: 50)
        self.view.addSubview(loginButton)
    }


}

