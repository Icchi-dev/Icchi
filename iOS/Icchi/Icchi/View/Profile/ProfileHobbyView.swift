//
//  ProfileHobbyView.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileHobbyView: UIView {
    
    @IBOutlet private weak var titleLbale: UILabel!
    
    private var didTapDelete: (() -> ())?
    
    func set(isLike: Bool, title: String, didTapDelete: @escaping (() -> ())) {
        self.backgroundColor = isLike ? .likeRed : .hateBlue
        self.titleLbale.text = title
        self.didTapDelete = didTapDelete
    }

    @IBAction func onTapDelete(_ sender: Any) {
        self.didTapDelete?()
    }
}
