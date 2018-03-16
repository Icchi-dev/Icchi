//
//  MyPostViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/03/15.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPostViewController: UIViewController {

    struct MyPostData {
        var title:String?
        var source:String?
        var link:String?
        var sumbnail:String?
    }
    
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var noDataLabel: UILabel!
    
    private let refreshControl = UIRefreshControl()
    
    private var myPostDatas:[MyPostData] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.table.refreshControl = self.refreshControl
        self.refreshControl.addTarget(self, action: #selector(MyPostViewController.refreshTableView(sender:)), for: .valueChanged)
        
        self.table.isHidden = false
        self.noDataLabel.isHidden = true
        
        // データ取得&表示更新
        self.refresh()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // ロゴタップ
    @IBAction func onTapLogo(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    // 引っ張って更新
    @objc func refreshTableView(sender: UIRefreshControl) {
        
        // データ取得&表示更新
        refresh();
        self.refreshControl.endRefreshing()
    }
    
    // データ取得&表示更新
    func refresh() {
        PostRequester.sharedManager.fetch {[weak self] _ in
            self?.setTableView()
        }
    }
    
    // テーブル更新
    func setTableView() {
        
        // データ更新
        self.myPostDatas = createMyPostDatas();
        
        // 表示反映
        self.table.reloadData()
        if self.myPostDatas.count <= 0 {
            self.table.isHidden = true
            self.noDataLabel.isHidden = false
        }
        else {
            self.table.isHidden = false
            self.noDataLabel.isHidden = true
        }
    }
    
    // テーブルデータ取得
    func createMyPostDatas() -> [MyPostData] {
        
        guard let myUserData = UserRequester.sharedManager.query(SaveData.shared.userId) else {
            return []
        }
        
        var myPostDatas:[MyPostData] = []
        
        // Yahooニュースの取得結果
        let yahooList = YahooNewsRequester.sharedManager.mNewsList
        for i in 0...5 where i < yahooList.count {
            let yahooData = yahooList[i]
            
            var myPostData = MyPostData()
            myPostData.title = yahooData.title;
            myPostData.source = "Yahoo!ニュース";
            myPostData.sumbnail = "";
            myPostData.link = yahooData.link;
            
            myPostDatas.append(myPostData)
        }
        
        // MyPost APIの結果
        let postList = PostRequester.sharedManager.postDatas
        postList.forEach { (postData) in
            var hasRelates = false
            if let relates = postData.relates {
                if relates.contains("*") {
                    hasRelates = true
                } else {
                    for (_, relate) in relates.enumerated() {
                        if let _ = myUserData.likes?.contains(relate) {
                            hasRelates = true
                            break
                        }
                    }
                }
            }
            
            if hasRelates {
                var myPostData = MyPostData()
                myPostData.title = postData.title
                myPostData.source = postData.source
                myPostData.sumbnail = postData.sumbnail
                myPostData.link = postData.link
                
                myPostDatas.append(myPostData)
            }
        }
        
        return myPostDatas
    }
}

extension MyPostViewController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.myPostDatas.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyPostTableViewCell") as! MyPostTableViewCell?
        
        cell!.configure(with:self.myPostDatas[indexPath.row])
        
        return cell!
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        guard let urlString = self.myPostDatas[indexPath.row].link
            , let url = URL(string: urlString)
            , UIApplication.shared.canOpenURL(url) else {
                print("linkなし")
                return;
        }
        
        // ブラウザ起動
        UIApplication.shared.open(url, options: [:], completionHandler:nil)
    }
    
    
}
