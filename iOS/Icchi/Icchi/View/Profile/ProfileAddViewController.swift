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
    
    @IBOutlet weak var addButton: UIButton!
    
    @IBOutlet weak var collectionView: UICollectionView!
    
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
            addButton.setTitleColor(UIColor.likeRed, for: .normal)
        }
        else {
            isLikeTextView.text = "嫌い"
            isLikeTextView.textColor = UIColor.hateBlue
            contentsBaseLayout.layer.borderColor = UIColor.hateBlue.cgColor
            searchLayout.layer.borderColor = UIColor.hateBlue.cgColor
            addButton.setTitleColor(UIColor.hateBlue, for: .normal)
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
        resetCollectionView(search:self.searchEditText.text)
    }
    
    func resetCollectionView(search:String?) {

        var shuffledItemDatas = ItemRequester.sharedManager.mDataList
        shuffledItemDatas = shuffledItemDatas.shuffled
        
        var itemDatas:[ItemRequester.ItemData] = []
        
        // 検索文字列あり
        if let search = search, search.count > 0 {
            
            // 検索にヒットしたアイテムを全て表示
            shuffledItemDatas.filter({return $0.name?.contains(search) ?? false
                ||  $0.kana?.contains(search) ?? false}).forEach {
                    itemDatas.append($0)
            }
        }
        else {
        
            // ランダム表示
            shuffledItemDatas.forEach {
                itemDatas.append($0)
            }
        }
        
        // 表示対象に登録
        self.itemDatas = itemDatas
        
        // 再表示
        self.collectionView.reloadData()
    }
    
    @IBAction func onSearchEditChanged(_ sender: Any) {
        resetCollectionView(search:self.searchEditText.text)
    }
    
    @IBAction func onDidEndExit(_ sender: Any) {
        resetCollectionView(search:self.searchEditText.text)
    }
    
    @IBAction func onTapAdd(_ sender: Any) {
        createItem()
    }
    
    @IBAction func onTapLogo(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapReload(_ sender: UIButton) {
        self.reload()
    }
    
    
    private func createItem() {
        
        guard let addString = self.searchEditText.text else {
            return
        }

        guard addString.count <= 10 else {
            self.showAlert(title: "エラー", message: "10文字以内で入力してください", actions: [AlertAction(title:"OK")])
            return
        }
        
        if addString.count > 0 {
            
            Loading.start()
            
            ItemRequester.sharedManager.creteItem(addString, completion:{ (result, itemId) in
                
                if result, let itemId = itemId {
                    
                    ItemRequester.sharedManager.fetch(completion: { (result) in
                        
                        Loading.stop()
                        
                        if result {
                            self.notifyToProfile(itemId:itemId)
                        }
                        else {
                            
                            let action = AlertAction(title:"OK")
                            self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
                        }
                    })
                }
                else {
                    
                    Loading.stop()
                    
                    let action = AlertAction(title:"OK")
                    self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
                }
            })
        }
    }
    
    private func notifyToProfile(itemId:String?) {
    
        if let profileViewController = self.parent as? ProfileViewController {
        
            let result = profileViewController.addItemId(itemId: itemId, isLike: isLike)
            
            if result  {
                self.pop(animationType: .horizontal)
            }
            else {
                var message = "\"好き\"に登録されたコンテンツです";
                if isLike {
                    message = "\"嫌い\"に登録されたコンテンツです";
                }
                
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: message, actions: [action])
            }
        }
    }
    
    private func reload() {
        
        Loading.start()
        
        ItemRequester.sharedManager.fetch(completion: { (result) in
            
            Loading.stop()
            
            if result {
                
                self.resetCollectionView(search:"")
            }
            else {
                
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
            }
            
        })
        
    }
}

extension ProfileAddViewController:UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.itemDatas?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {

        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ProfileAddCollectionViewCell", for:indexPath) as! ProfileAddCollectionViewCell

        cell.configure(with: self.itemDatas?[indexPath.row], isLike:self.isLike)
        return cell
        
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        let selectData = self.itemDatas?[indexPath.row]
        self.notifyToProfile(itemId: selectData?.itemId)
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {

        let selectData = self.itemDatas?[indexPath.row]

        return self.newSize(selectData?.name);
    }

    public func newSize(_ name:String?) -> CGSize {
        
        let label = UILabel()
        label.text = name!
        label.font = UIFont.systemFont(ofSize: 16)
        let newSize = label.sizeThatFits(CGSize(width:400, height:300))
        return CGSize(width:newSize.width + 20, height:30)
    }
    
}
