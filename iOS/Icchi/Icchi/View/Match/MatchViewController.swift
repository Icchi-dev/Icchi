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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchTableViewCell") as! MatchTableViewCell?
        
        cell!.nameTextView?.text =  UserRequester.sharedManager.mDataList[indexPath.row].name

        return cell!
    }
    
    
}
