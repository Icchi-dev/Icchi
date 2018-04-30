//
//  MatchViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/03/09.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MatchViewController: UIViewController {

    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var table: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // ロゴタップ
        let logoTap = UITapGestureRecognizer(target:self, action:#selector(self.onTapLogo(_:)))
        self.logo!.addGestureRecognizer(logoTap)
    }
    
    // ロゴタップ
    @objc func onTapLogo(_ sender: UITapGestureRecognizer) {
        self.pop(animationType: .horizontal)
    }
}

extension MatchViewController:UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return UserRequester.sharedManager.mDataList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchTableViewCell") as! MatchTableViewCell
        cell.configure(with:UserRequester.sharedManager.mDataList[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        guard let urlString = UserRequester.sharedManager.mDataList[indexPath.row].fbLink,
            let url = URL(string: urlString),
            UIApplication.shared.canOpenURL(url) else {
            return
        }
        
        // ブラウザ起動
        UIApplication.shared.open(url, options: [:], completionHandler:nil)
    }
    
}
