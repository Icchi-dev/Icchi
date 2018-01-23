//
//  MyPageViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageViewController: UIViewController {

    @IBAction func onTapProfile(_ sender: Any) {
        
        if SaveData.shared.isInitialized {
            let tutorialViewController = self.viewController(storyboard: "Main", identifier: "TutorialViewController")
            self.stack(viewController: tutorialViewController, animationType: .horizontal)
        } else {
            let profileViewController = self.viewController(storyboard: "Main", identifier: "ProfileViewController")
            self.stack(viewController: profileViewController, animationType: .horizontal)
        }
    }

    @IBAction func onTapIcchi(_ sender: Any) {
        
    }
    
    @IBAction func onTapMyPost(_ sender: Any) {
        
    }
}
