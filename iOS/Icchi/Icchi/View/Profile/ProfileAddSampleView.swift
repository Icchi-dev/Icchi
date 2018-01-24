//
//  ProfileAddSampleView.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddSampleView: UIView {

    @IBOutlet private weak var sampleLabel: UILabel!
    
    func configure(text: String) {
        self.sampleLabel.text = text
        self.sampleLabel.sizeToFit()
        self.frame = CGRect(x: 0, y: 0, width: self.sampleLabel.frame.size.width + 20, height: self.sampleLabel.frame.size.height + 20)
    }
}
