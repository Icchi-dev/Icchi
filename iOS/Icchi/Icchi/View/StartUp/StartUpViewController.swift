//
//  StartUpViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/02/20.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class StartUpViewController: UIViewController {

    private enum FetchResult: Int {
        case ok
        case error
        case progress
    }
    private var fetchUserResult = FetchResult.progress;
    private var fetchItemResult = FetchResult.progress;
    private var fetchPostResult = FetchResult.progress;
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // 初期データ取得
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            self.fetch()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /* データ取得 */
    private func fetch() {
        
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
    
    // 受信チェック
    private func checkResult() {
        
        // 受信待ち
        if (self.fetchUserResult == .progress
            || self.fetchItemResult == .progress
            || self.fetchPostResult == .progress) {
            return
        }
        
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
        
        // ID取得済み
        if SaveData.shared.userId.count > 0 {
            
            let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
            self.stack(viewController: myPageViewController, animationType: .vertical)
        }
        // 未取得
        else {
            
            let loginViewController = self.viewController(storyboard: "Main", identifier: "LoginViewController") as! LoginViewController
            self.stack(viewController: loginViewController, animationType: .vertical)
        }
        
    }

}
