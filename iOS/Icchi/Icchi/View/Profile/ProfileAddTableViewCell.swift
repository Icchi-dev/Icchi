//
//  ProfileAddTableViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/03/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddTableViewCell: UITableViewCell {

    @IBOutlet private weak var title: UILabel!
    
    public func configure(with:ItemRequester.ItemData?, isLike:Bool) {
        self.title.text = with?.name
        self.backgroundColor = isLike ? UIColor.likeRed : UIColor.hateBlue
        self.layer.cornerRadius = 20
    }
}
