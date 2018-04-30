//
//  ProfileAddViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddViewController: UIViewController {
    
    @IBOutlet weak var contentsBaseLayout: UIView!
    @IBOutlet weak var isLikeTextView: UILabel!
    @IBOutlet weak var searchLayout: UIView!
    @IBOutlet weak var searchEditText: UITextField!
    
    @IBOutlet weak var grassImageView: UIImageView!
    
    @IBOutlet weak var tableView: UITableView!
    var itemDatas:[ItemRequester.ItemData]?
    
    private var isLike:Bool = true
    private var exceptItemIdList:[String]? = nil
    func set(isLike: Bool, exceptItemIdList:[String]?) {
        self.isLike = isLike
        self.exceptItemIdList = exceptItemIdList
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.isLike {
            isLikeTextView.text = "好き"
            isLikeTextView.textColor = UIColor.likeRed
            contentsBaseLayout.layer.borderColor = UIColor.likeRed.cgColor
            searchLayout.layer.borderColor = UIColor.likeRed.cgColor
            grassImageView.image = UIImage(named:"profileadd_grass_like")
        }
        else {
            isLikeTextView.text = "嫌い"
            isLikeTextView.textColor = UIColor.hateBlue
            contentsBaseLayout.layer.borderColor = UIColor.hateBlue.cgColor
            searchLayout.layer.borderColor = UIColor.hateBlue.cgColor
            grassImageView.image = UIImage(named:"profileadd_grass_hate")
        }
        
        self.itemDatas = ItemRequester.sharedManager.mDataList.filter({ (data) -> Bool in
        
            guard let exceptItemIdList = exceptItemIdList else {
                return true;
            }
            guard let itemId = data.itemId else {
                return false;
            }
            
            return !exceptItemIdList.contains(itemId)
        })
        resetTableView(search:self.searchEditText.text)
    }
    
    func resetTableView(search:String?) {
        
        if let search = search, search.count > 0 {
            self.itemDatas = ItemRequester.sharedManager.mDataList.filter({return $0.name?.contains(search) ?? false})
        }
        self.tableView.reloadData()
    }
    
    @IBAction func onDidEndExit(_ sender: Any) {
        resetTableView(search:self.searchEditText.text)
    }
    
    @IBAction func onSearchEditChanged(_ sender: Any) {
        resetTableView(search:self.searchEditText.text)
    }
    
    @IBAction func onTapLogo(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension ProfileAddViewController:UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return self.itemDatas?.count ?? 0
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileAddTableViewCell", for: indexPath) as! ProfileAddTableViewCell
        cell.selectionStyle = UITableViewCellSelectionStyle.none
        
        cell.configure(with: self.itemDatas?[indexPath.section], isLike:self.isLike)
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 5
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 5
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let selectData = self.itemDatas?[indexPath.section]
        
        if let profile = self.parent as? ProfileViewController {
            profile.addItemId(itemId: selectData?.itemId, isLike: self.isLike)
        }
        
        self.pop(animationType: .horizontal)
    }
    
    public func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let v = UIView()
        v.backgroundColor = UIColor.clear
        return v
    }
    
    public func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let v = UIView()
        v.backgroundColor = UIColor.clear
        return v
    }
}
