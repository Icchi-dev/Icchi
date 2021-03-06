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
        
        self.table.rowHeight = UITableViewAutomaticDimension
        self.table.refreshControl = self.refreshControl
        self.refreshControl.addTarget(self, action: #selector(MyPostViewController.refreshTableView(sender:)), for: .valueChanged)
        
        self.table.isHidden = false
        self.noDataLabel.isHidden = true
        
        // データ取得&表示更新
        self.refresh()
    }
    
    // ロゴタップ
    @IBAction func onTapLogo(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    // 引っ張って更新
    @objc func refreshTableView(sender: UIRefreshControl) {
        
        // データ取得&表示更新
        refresh();
    }
    
    // データ取得&表示更新
    func refresh() {
        
        Loading.start()
        
        PostRequester.sharedManager.fetch {[weak self] _ in
            
            Loading.stop()
            self?.setTableView()
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
                if let _ = self?.refreshControl.isRefreshing {
                    self?.refreshControl.endRefreshing()
                }
            })
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
        for yahooData in yahooList
            where myPostDatas.count < 16 {
            
            if let likes = myUserData.likes {
                for likeCd in likes {
                    if let itemData = ItemRequester.sharedManager.query(likeCd),
                        let yahooTitle = yahooData.title,
                        let itemDataName = itemData.name, yahooTitle.contains(itemDataName) {
                        
                        var myPostData = MyPostData()
                        myPostData.title = yahooData.title;
                        myPostData.source = "Yahoo!ニュース";
                        myPostData.sumbnail = "";
                        myPostData.link = yahooData.link;
                        
                        myPostDatas.append(myPostData)
                        break;
                    }
                }
            }
        }
        
        // MyPost APIの結果
        let postList = PostRequester.sharedManager.postDatas
        postList.forEach { (postData) in
            var hasRelates = false
            if postData.forAll == "1" {
                hasRelates = true
            } else {
                myUserData.likes?.compactMap { $0 }.compactMap { ItemRequester.sharedManager.query($0) }.forEach { itemData in
                    hasRelates = postData.title?.contains(itemData.name ?? "") ?? false
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
        return self.myPostDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyPostTableViewCell") as! MyPostTableViewCell
        cell.configure(with:self.myPostDatas[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        guard let urlString = self.myPostDatas[indexPath.row].link,
            let url = URL(string: urlString),
            UIApplication.shared.canOpenURL(url) else {
            return
        }
        
        // ブラウザ起動
        UIApplication.shared.open(url, options: [:], completionHandler:nil)
    }
}
