//
//  LikeListViewController.swift
//  Icchi
//
//  Created by oonaka on 2018/04/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class LikeListViewController: UIViewController {

    
    @IBOutlet weak var likeCollectionView: UICollectionView!
    @IBOutlet weak var hateCollectionView: UICollectionView!
    
    // アイテムデータ
    class CellData {
        var itemData:ItemRequester.ItemData? = nil
        var selected:Bool = false
    }
    
    var likeCellDatas:[CellData]?
    var hateCellDatas:[CellData]?
    
    var mSelectedLikeItemNames: [String] = []
    var mSelectedHateItemNames: [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.initContents()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onTapReload(_ sender: Any) {
        self.initContents()
    }
    
    private func initContents() {
        
        addItems(isLike: true)
        addItems(isLike: false)
    }
    
    private func addItems(isLike:Bool) {
        
        var itemDataList:[ItemRequester.ItemData] = []
        
        let userList = UserRequester.sharedManager.mDataList;
        for userData in userList {
            var userItemNos:[String]?
            
            if isLike {
                userItemNos = userData.likes;
            } else {
                userItemNos = userData.hates;
            }
        
            for itemNo in userItemNos ?? [] {
                if !(itemDataList.compactMap({ $0 })
                    .contains(where: { itemNo.elementsEqual($0.itemId!) })
                    )
                {
                    if let itemData = ItemRequester.sharedManager.query(itemNo) ,let _ = itemData.itemId {
                        itemDataList.append(itemData)
                    }
                }
            }
        }
        
        itemDataList = itemDataList.shuffled;
        
        // 選択済みのアイテムを先頭にもってくる
        let selectedList = isLike ? mSelectedLikeItemNames : mSelectedHateItemNames
        let newList1 = itemDataList.filter({ selectedList.contains($0.itemId!) })
        let newList2 = itemDataList.filter({ !selectedList.contains($0.itemId!) })
        
        // セルデータ配列を作成
        var cellDataList: [CellData] = []
        newList1.forEach {
            let cellData = CellData()
            cellData.itemData = $0
            cellData.selected = true
            cellDataList.append(cellData)
        }
        newList2.forEach {
            let cellData = CellData()
            cellData.itemData = $0
            cellData.selected = false
            cellDataList.append(cellData)
        }
        
        // テーブル更新
        if isLike {
            likeCellDatas = cellDataList
            self.likeCollectionView.reloadData()
        }
        else {
            hateCellDatas = cellDataList
            self.hateCollectionView.reloadData()
        }
    }

    
    @IBAction func onTapOk(_ sender: UIButton) {
        
        
        var myUserData = UserRequester.sharedManager.query(SaveData.shared.userId)
        
        for likeItemName in mSelectedLikeItemNames {
            myUserData?.likesAddChange(like: likeItemName)
        }
        
        for hateItemName in mSelectedHateItemNames {
            myUserData?.hatesAddChange(hate: hateItemName)
        }
        
        Loading.start()
        
        AccountRequester.sharedManager.update(userData: myUserData, completion:{(result) in
            
            if result {
                
                // ユーザー一覧取得
                UserRequester.sharedManager.fetch() { (result) in

                    // ローディング
                    Loading.stop()

                    // 入力チェック
                    guard result else {
                        let action = AlertAction(title:"OK")
                        self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
                        return;
                    }

                    // プロフィール画面へ
                    let ProfileViewController = self.viewController(storyboard: "Main", identifier: "ProfileViewController") as! ProfileViewController
                    self.stack(viewController: ProfileViewController, animationType: .horizontal)
                }
            }
            else {
                
                // ローディング
                Loading.stop()
                
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
            }
        })
        
    }
    
    @IBAction func onTapLogo(_ sender: Any) {
        
        let MyPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
        self.stack(viewController: MyPageViewController, animationType: .vertical)
    }
    
    
}

extension LikeListViewController:UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == self.likeCollectionView {
            return self.likeCellDatas?.count ?? 0
        }
        else {
            return self.hateCellDatas?.count ?? 0
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"LikeListCollectionViewCell", for:indexPath) as! LikeListCollectionViewCell
        
        if collectionView == self.likeCollectionView {
            cell.configure(with: self.likeCellDatas?[indexPath.row], isLike:true)
        }
        else {
            cell.configure(with: self.hateCellDatas?[indexPath.row], isLike:false)
        }
        return cell
        
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        if collectionView == self.likeCollectionView {
            let selectData = self.likeCellDatas?[indexPath.row];
            selectData?.selected = true
            self.didTapItemName(itemData: selectData?.itemData, isLike: true)
        }
        else {
            let selectData = self.hateCellDatas?[indexPath.row];
            selectData?.selected = true
            self.didTapItemName(itemData: selectData?.itemData, isLike: false)
        }
        
        collectionView.reloadData()
    }
    
    func didTapItemName(itemData: ItemRequester.ItemData?, isLike: Bool) {
        
        guard let itemName = itemData?.itemId else {
            return
        }
        
        if isLike {
            if mSelectedLikeItemNames.contains(itemName) {
                if let idx = mSelectedLikeItemNames.index(where: { itemName.elementsEqual($0) }) {
                    mSelectedLikeItemNames.remove(at: idx)
                }
            } else {
                mSelectedLikeItemNames.append(itemName);
            }
        } else {
            if mSelectedHateItemNames.contains(itemName) {
                if let idx = mSelectedHateItemNames.index(where: { itemName.elementsEqual($0) }) {
                    mSelectedHateItemNames.remove(at: idx)
                }
            } else {
                mSelectedHateItemNames.append(itemName);
            }
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        var selectData:CellData?
        
        if collectionView == self.likeCollectionView {
            selectData = self.likeCellDatas?[indexPath.row];
        }
        else {
            selectData = self.hateCellDatas?[indexPath.row];
        }
        
        var resizeName = ""
        if let itemData = selectData?.itemData, let name = itemData.name {
            resizeName = name
        }
        
        return self.newSize(resizeName);
    }
    
    public func newSize(_ name:String?) -> CGSize {
        
        let label = UILabel()
        label.text = name!
        label.font = UIFont.systemFont(ofSize: 16)
        let newSize = label.sizeThatFits(CGSize(width:400, height:300))
        return CGSize(width:newSize.width + 20, height:30)
    }
}

