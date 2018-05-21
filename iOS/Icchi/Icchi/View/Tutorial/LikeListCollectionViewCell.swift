//
//  LikeListCollectionViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/05/21.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class LikeListCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var title: UILabel!
    
    public func configure(with:LikeListViewController.CellData?, isLike:Bool) {
        
        self.title.text = ""
        if let itemData = with?.itemData, let name = itemData.name {
            self.title.text = name
        }
        
        if let selected = with?.selected, selected {
            self.backgroundColor = UIColor.selectedColor
        }
        else {
            self.backgroundColor = isLike ? UIColor.likeRed : UIColor.hateBlue
        }
    }
    
}
