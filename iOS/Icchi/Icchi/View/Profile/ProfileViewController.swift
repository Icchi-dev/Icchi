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
        
        // ユーザーデータ取得
        if let userData = UserRequester.sharedManager.query(SaveData.shared.userId) {
            self.tmpUserData = userData
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
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.setProfile(userData: self.tmpUserData)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        self.refreshScrollSize()
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
        var alertActions:[AlertAction] = []
        
        let items:[String] = [
            UserRequester.AgeType.u20.display(),
            UserRequester.AgeType.s20.display(),
            UserRequester.AgeType.s30.display(),
            UserRequester.AgeType.s40.display(),
            UserRequester.AgeType.s50.display(),
            UserRequester.AgeType.o60.display()
        ];
        
        items.forEach { (item) in
            let action = AlertAction(title: item, action: { (title:String) in
                self.age.text = title
                self.age.textColor = UIColor.black
                
                let items:[UserRequester.AgeType] = [
                    UserRequester.AgeType.u20,
                    UserRequester.AgeType.s20,
                    UserRequester.AgeType.s30,
                    UserRequester.AgeType.s40,
                    UserRequester.AgeType.s50,
                    UserRequester.AgeType.o60
                ];
                
                self.tmpUserData?.age = items.first(where: {return $0.display() == title})
            })
            alertActions.append(action)
        }
        let cancelAction = AlertAction(title: "キャンセル")
        alertActions.append(cancelAction)
        self.showAlert(title: "年齢", message: nil, actions: alertActions)
    }
    
    // 性別タップ
    @objc func onTapSex(_ sender: UITapGestureRecognizer) {
        var alertActions:[AlertAction] = []
        
        let items:[String] = [
            UserRequester.GenderType.male.display(),
            UserRequester.GenderType.female.display(),
            ];
        
        items.forEach { (item) in
            let action = AlertAction(title: item, action: { (title:String) in
                self.gender.text = title
                self.gender.textColor = UIColor.black
                
                let items:[UserRequester.GenderType] = [
                    UserRequester.GenderType.male,
                    UserRequester.GenderType.female,
                    ];
                self.tmpUserData?.gender = items.first(where: {return $0.display() == title})
            })
            alertActions.append(action)
        }
        let cancelAction = AlertAction(title: "キャンセル")
        alertActions.append(cancelAction)
        self.showAlert(title: "性別", message: nil, actions: alertActions)
    }
    
    private func setProfile(userData:UserRequester.UserData?) {
        
        if let name = userData?.name {
            self.name.text = name
        }
        
        if let age = userData?.age {
            self.age.text = age.display()
            self.age.textColor = UIColor.black
        }
        if let gender = userData?.gender {
            self.gender.text = gender.display()
            self.gender.textColor = UIColor.black
        }
        
        if let likes =  userData?.likes {
            for (i,itemId) in likes.enumerated() {
                let itemData = ItemRequester.sharedManager.query(itemId);
                if let likeString = itemData?.name {
                    self.addHobby(isLike: true, tag:i, itemId:itemId, hobbyText:likeString)
                }
            }
        }
        
        if let hates =  userData?.hates {
            for (i,itemId) in hates.enumerated() {
                let itemData = ItemRequester.sharedManager.query(itemId);
                if let heiteString = itemData?.name {
                    self.addHobby(isLike: false, tag:i, itemId:itemId, hobbyText:heiteString)
                }
            }
        }
        
        self.refreshScrollSize()
    }
    
    private func refreshScrollSize() -> Void {
        let height = self.likeContentsView.frame.size.height > self.hateContentsView.frame.size.height ? self.likeContentsView.frame.size.height : self.hateContentsView.frame.size.height
        self.contentsHeightConstraint.constant = height
        
        self.view.layoutIfNeeded()
    }

    private func addHobby(isLike: Bool, tag:Int, itemId:String, hobbyText:String) {
        
        let hobbyView = UINib(nibName: "ProfileHobbyView", bundle: nil).instantiate(withOwner: self, options: nil).first as! ProfileHobbyView
        hobbyView.tag = tag
        hobbyView.set(isLike: isLike, title: hobbyText, didTapDelete: { [weak self] in
            self?.deleteHobby(isLike: isLike, tag:tag, itemId: itemId)
        })
        let stackView = isLike ? self.likeContentsStackView : self.hateContentsStackView
        stackView?.addArrangedSubview(hobbyView)
    }
    
    private func deleteHobby(isLike: Bool, tag:Int, itemId: String) {
        
        let stackView = isLike ? self.likeContentsStackView : self.hateContentsStackView
        if let deleteHobbyView = (stackView?.arrangedSubviews.filter { $0.tag == tag })?.first {
            stackView?.removeArrangedSubview(deleteHobbyView)
            UIView.animate(withDuration: 0.2, animations: {
                deleteHobbyView.isHidden = true
                self.refreshScrollSize()
            })
        }
        if isLike {
            let likes = self.tmpUserData?.likes?.filter({return !($0==itemId)})
            self.tmpUserData?.likes = likes
        }
        else {
            let hates = self.tmpUserData?.hates?.filter({return !($0==itemId)})
            self.tmpUserData?.hates = hates
        }
    }
    
    private func onClickMenu() {
        
        self.tmpUserData?.name = self.name.text
        
        Loading.start()
        
        AccountRequester.sharedManager.update(userData: self.tmpUserData) { [weak self] result in
            if result {
                UserRequester.sharedManager.fetch(completion: { [weak self] (result) in
                    Loading.stop()
                    if result {
                        self?.pop(animationType: .horizontal)
                    }
                    else {
                        self?.showAlert(title: "エラー", message: "通信に失敗しました", actions: [AlertAction(title:"OK")])
                    }
                })
            }
            else {
                Loading.stop()
                self?.showAlert(title: "エラー", message: "通信に失敗しました", actions: [AlertAction(title:"OK")])
            }
            
        }
        
        self.pop(animationType: .horizontal)
    }

    public func addItemId(itemId:String? , isLike:Bool) {
        
        guard let itemId = itemId else {
            return;
        }
        
        if isLike {
            if let likes = self.tmpUserData?.likes, !likes.contains(itemId) {
                self.tmpUserData?.likes?.append(itemId)
            }
        }
        else {
            if let hates = self.tmpUserData?.hates, !hates.contains(itemId) {
                self.tmpUserData?.hates?.append(itemId)
            }
        }
        self.likeContentsStackView.arrangedSubviews.forEach({subView in
            self.likeContentsStackView.removeArrangedSubview(subView)
        })
        self.hateContentsStackView.arrangedSubviews.forEach({subView in
            self.hateContentsStackView.removeArrangedSubview(subView)
        })
        self.setProfile(userData: self.tmpUserData)
    }
    
}
