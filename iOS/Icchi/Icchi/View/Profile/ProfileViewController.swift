//
//  ProfileViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {

    @IBOutlet private weak var likeContentsStackView: UIStackView!
    @IBOutlet private weak var hateContentsStackView: UIStackView!
    @IBOutlet private weak var contentsHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var likeContentsView: UIView!
    @IBOutlet private weak var hateContentsView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for i in 0..<10 {
            [true, false].forEach { isLike in
                let hobbyView = UINib(nibName: "ProfileHobbyView", bundle: nil).instantiate(withOwner: self, options: nil).first as! ProfileHobbyView
                hobbyView.tag = i
                hobbyView.set(isLike: isLike, title: "test", didTapDelete: { [weak self] in
                    self?.deleteHobby(isLike: isLike, tag: i)
                })
                let stackView = isLike ? self.likeContentsStackView : self.hateContentsStackView
                stackView?.addArrangedSubview(hobbyView)
            }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        let height = self.likeContentsView.frame.size.height > self.hateContentsView.frame.size.height ? self.likeContentsView.frame.size.height : self.hateContentsView.frame.size.height
        self.contentsHeightConstraint.constant = height
    }

    private func deleteHobby(isLike: Bool, tag: Int) {
        
        let stackView = isLike ? self.likeContentsStackView : self.hateContentsStackView
        if let deleteHobbyView = (stackView?.arrangedSubviews.filter { $0.tag == tag })?.first {
            stackView?.removeArrangedSubview(deleteHobbyView)
            UIView.animate(withDuration: 0.2, animations: {
                deleteHobbyView.isHidden = true
                self.view.layoutIfNeeded()
            })
        }
    }
    
    @IBAction func onTapMenu(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
}
