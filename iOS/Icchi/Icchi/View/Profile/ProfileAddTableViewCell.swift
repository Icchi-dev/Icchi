//
//  ProfileAddTableViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/03/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddTableViewCell: UITableViewCell {

    @IBOutlet weak var title: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    public func configure(with:ItemRequester.ItemData?, isLike:Bool) {
        self.title.text = with?.name
        self.backgroundColor = isLike ? UIColor.likeRed : UIColor.hateBlue
        self.layer.cornerRadius = 20
    }

}
