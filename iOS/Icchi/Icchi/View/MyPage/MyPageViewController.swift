//
//  MyPageViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageViewController: UIViewController {

    // プロフィール変更・登録タップ
    @IBAction func onTapProfile(_ sender: Any) {
        
        if SaveData.shared.isInitialized {
            let profileViewController = self.viewController(storyboard: "Main", identifier: "ProfileViewController")
            self.stack(viewController: profileViewController, animationType: .horizontal)
        } else {
            let tutorialViewController = self.viewController(storyboard: "Main", identifier: "TutorialViewController")
            self.stack(viewController: tutorialViewController, animationType: .horizontal)
        }
    }

    // 一致度をはかるをタップ
    @IBAction func onTapIcchi(_ sender: Any) {
        
        let matchViewController = self.viewController(storyboard: "Main", identifier: "MatchViewController")
        self.stack(viewController: matchViewController, animationType: .horizontal)
    }
    
    // マイポストをタップ
    @IBAction func onTapMyPost(_ sender: Any) {
        
        let myPostViewController = self.viewController(storyboard: "Main", identifier: "MyPostViewController")
        self.stack(viewController: myPostViewController, animationType: .horizontal)
    }
}
