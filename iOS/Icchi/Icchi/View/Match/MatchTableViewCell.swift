//
//  MatchTableViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/03/09.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MatchTableViewCell: UITableViewCell {

    @IBOutlet weak var faceImageView: UIImageView!
    @IBOutlet weak var nameTextView: UILabel!
    
    @IBOutlet weak var matchTextView: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    public func configure(with:UserRequester.UserData?) {
        
        self.nameTextView.text = with?.name
        if let image = with?.image {
            ImageStorage.shared.fetch(url: image, imageView: self.faceImageView)
        }
        let match = UserRequester.sharedManager.queryMatch(with?.userId)
        self.matchTextView.text = String(match)
    }

}
