//
//  MyPostTableViewCell.swift
//  Icchi
//
//  Created by oonaka on 2018/03/16.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPostTableViewCell: UITableViewCell {

    @IBOutlet weak var postTextView: UILabel!
    @IBOutlet weak var sourceTextView: UILabel!
    @IBOutlet weak var postImageView: UIImageView!
    @IBOutlet weak var postImageWidth: NSLayoutConstraint!
    
    public func configure(with:MyPostViewController.MyPostData?) {
        
        if let title = with?.title {
            self.postTextView.text = title
        }
        
        if let source = with?.source {
            self.sourceTextView.text = source
        }
        
        if let sumbnail = with?.sumbnail, sumbnail.count > 0 {
            ImageStorage.shared.fetch(url: sumbnail, imageView: self.postImageView)
        }
        else {
            self.postImageWidth.constant = 0.0;
        }
    }


}
