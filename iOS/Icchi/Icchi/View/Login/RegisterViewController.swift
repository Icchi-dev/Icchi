//
//  RegisterViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/02/20.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class RegisterViewController: KeyboardRespondableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // ログインボタンタップ
    @IBAction func onLogin(_ sender: Any) {
    }
    
    // キャンセルボタンタップ
    @IBAction func onCancel(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
}
