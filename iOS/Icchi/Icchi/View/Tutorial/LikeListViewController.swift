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
    
    var likeItemDatas:[ItemRequester.ItemData]?
    
    var hateItemDatas:[ItemRequester.ItemData]?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        addItems(isLike: true)
        addItems(isLike: false)
        
    }
    
    private func addItems(isLike:Bool) {
        
        var itemIdList:[String] = []
        
        let userList = UserRequester.sharedManager.mDataList;
        
        var userItemNos:[String]?
        for userData in userList {
            if isLike {
                userItemNos = userData.likes;
            } else {
                userItemNos = userData.hates;
            }
        }
        
        for itemNo in userItemNos ?? [] {
            if !itemIdList.contains(itemNo) {
                if let itemData = ItemRequester.sharedManager.query(itemNo),
                    let itemId = itemData.itemId {
                    itemIdList.append(itemId)
                }
            }
        }
        
        itemIdList = itemIdList.shuffled;
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onTapOk(_ sender: UIButton) {
        
        let ProfileViewController = self.viewController(storyboard: "Main", identifier: "ProfileViewController") as! ProfileViewController
        self.stack(viewController: ProfileViewController, animationType: .horizontal)

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
            return self.likeItemDatas?.count ?? 0
        }
        else {
            return self.hateItemDatas?.count ?? 0
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ProfileAddCollectionViewCell", for:indexPath) as! ProfileAddCollectionViewCell
        
        if collectionView == self.likeCollectionView {
            cell.configure(with: self.likeItemDatas?[indexPath.row], isLike:true)
        }
        else {
            cell.configure(with: self.hateItemDatas?[indexPath.row], isLike:true)
        }
        return cell
        
    }
    
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        var selectData:ItemRequester.ItemData?
        
        if collectionView == self.likeCollectionView {
            selectData = self.likeItemDatas?[indexPath.row];
        }
        else {
            selectData = self.hateItemDatas?[indexPath.row];
        }
        
        return self.newSize(selectData?.name);
    }
    
    public func newSize(_ name:String?) -> CGSize {
        
        let label = UILabel()
        label.text = name!
        label.font = UIFont.boldSystemFont(ofSize: 20)
        let newSize = label.sizeThatFits(CGSize(width:400, height:300))
        return CGSize(width:newSize.width + 40, height:50)
    }
    
}

