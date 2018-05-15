//
//  LikeListViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/04/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class LikeListViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onTapLogo(_ sender: Any) {
        
        let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
        self.stack(viewController: myPageViewController, animationType: .vertical)

    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
