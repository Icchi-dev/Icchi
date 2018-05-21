//
//  ProfileAddCollectionViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/05/10.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet private weak var title: UILabel!
    
    public func configure(with:ItemRequester.ItemData?, isLike:Bool) {
        self.title.text = with?.name
        self.backgroundColor = isLike ? UIColor.likeRed : UIColor.hateBlue
    }
    
}
