//
//  ProfileViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {
    
    private var tmpUserData: UserRequester.UserData?
    
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var age: UILabel!
    @IBOutlet weak var gender: UILabel!
    
    @IBOutlet private weak var likeContentsStackView: UIStackView!
    @IBOutlet private weak var hateContentsStackView: UIStackView!
    @IBOutlet private weak var contentsHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var likeContentsView: UIView!
    @IBOutlet private weak var hateContentsView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let userData = UserRequester.sharedManager.query(userId: SaveData.shared.userId) {
            self.tmpUserData = userData
            self.setProfile(userData: userData)
        }
        
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
        
        // 年齢タップ
        let ageTapGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(self.onTapAge(_:)))
        self.age!.addGestureRecognizer(ageTapGesture)
        
        // 性別タップ
        let sexTapGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(self.onTapSex(_:)))
        self.gender!.addGestureRecognizer(sexTapGesture)
        
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
    
    private func stackAddViewController(isLike: Bool) {
        let addViewController = self.viewController(storyboard: "Main", identifier: "ProfileAddViewController") as! ProfileAddViewController
        addViewController.set(isLike: isLike)
        self.stack(viewController: addViewController, animationType: .horizontal)
    }
    
    @IBAction func onTapMenu(_ sender: Any) {
        self.onClickMenu()
    }
    
    @IBAction func onTapAddLike(_ sender: Any) {
        self.stackAddViewController(isLike: true)
    }
    
    @IBAction func onTapAddHate(_ sender: Any) {
        self.stackAddViewController(isLike: false)
    }
    
    // キーボードリターン
    @IBAction func onDidEndOnExit(_ sender: Any) {
    }
    
    // 年齢タップ
    @objc func onTapAge(_ sender: UITapGestureRecognizer) {
        let alert = UIAlertController(title:"年齢", message:nil, preferredStyle:.alert)
        
        let items:[String] = [
            UserRequester.AgeType.u20.display(),
            UserRequester.AgeType.s20.display(),
            UserRequester.AgeType.s30.display(),
            UserRequester.AgeType.s40.display(),
            UserRequester.AgeType.s50.display(),
            UserRequester.AgeType.o60.display()
        ];
        
        items.forEach { (item) in
            let action = UIAlertAction(title: item, style: .default) { (tapAction) in
                self.age.text = tapAction.title!
                self.age.textColor = UIColor.black
                
                let items:[UserRequester.AgeType] = [
                    UserRequester.AgeType.u20,
                    UserRequester.AgeType.s20,
                    UserRequester.AgeType.s30,
                    UserRequester.AgeType.s40,
                    UserRequester.AgeType.s50,
                    UserRequester.AgeType.o60
                ];
                
                self.tmpUserData?.age = items.first(where: {return $0.display() == tapAction.title!})
            }
            alert.addAction(action)
        }
        let cancelAction = UIAlertAction(title: "キャンセル", style: .cancel) {(tapAction) in}
        alert.addAction(cancelAction)
        present(alert, animated: true, completion: nil)
    }
    
    // 性別タップ
    @objc func onTapSex(_ sender: UITapGestureRecognizer) {
        let alert = UIAlertController(title:"性別", message:nil, preferredStyle:.alert)
        
        let items:[String] = [
            UserRequester.GenderType.male.display(),
            UserRequester.GenderType.female.display(),
            ];
        
        items.forEach { (item) in
            let action = UIAlertAction(title: item, style: .default) { (tapAction) in
                self.gender.text = tapAction.title!
                self.gender.textColor = UIColor.black
                
                let items:[UserRequester.GenderType] = [
                    UserRequester.GenderType.male,
                    UserRequester.GenderType.female,
                    ];
                self.tmpUserData?.gender = items.first(where: {return $0.display() == tapAction.title!})
            }
            alert.addAction(action)
        }
        let cancelAction = UIAlertAction(title: "キャンセル", style: .cancel) {(tapAction) in}
        alert.addAction(cancelAction)
        
        present(alert, animated: true, completion: nil)
    }
    
    private func setProfile(userData:UserRequester.UserData) {
        
        if let name = userData.name {
            self.name.text = name
        }
        
        if let age = userData.age {
            self.age.text = age.display()
            self.age.textColor = UIColor.black
        }
        if let gender = userData.gender {
            self.gender.text = gender.display()
            self.gender.textColor = UIColor.black
        }
    }
    
    private func onClickMenu() {
        
        self.pop(animationType: .horizontal)
    }

}
