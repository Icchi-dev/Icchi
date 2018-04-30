//
//  TutorialViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class TutorialViewController: UIViewController {

    @IBOutlet weak var typeTextView: UILabel!
    @IBOutlet weak var countTextView: UILabel!
    @IBOutlet private weak var contentsView: UIView!
    @IBOutlet private weak var questionLabel: UILabel!
    @IBOutlet private weak var textField: UITextField!

    private var pageIndex = 0
    private var questions = [
        "趣味は？"
        , "好きな\nアーティストは？"
        , "好きな食べ物は？"
        , "好きな言葉は？"
        , "集めているものは？"
        , "好きなスポーツは？"
        , "好きなお笑い芸人は？"
        , "今欲しいものは？"
        , "特技は？"
        , "好きな漫画・アニメは？"
        , "怖いものは？"
        , "嫌いな食べ物は？"
        , "嫌いな言葉は？"
        , "嫌いな時間や状況は？"
        , "苦手なことは？"
        , "嫌いな教科は？"
    ]
    
    private var likeAnswers:[String] = []
    private var hateAnswers:[String] = []
    private var likeCreateIndex = 0
    private var hateCreateIndex = 0
    private var tmpUserData:UserRequester.UserData? = nil
    private var mIsAnimating = false
    
    @IBAction func didEndEditTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setQuestion()
        
        self.tmpUserData = UserRequester.sharedManager.query(SaveData.shared.userId)
    }
    
    @IBAction func onTapNext(_ sender: UITextField) {
        
        guard !self.mIsAnimating else {
            return;
        }
        
        guard self.checkText() else {
            let action = AlertAction(title:"OK")
            self.showAlert(title: "エラー", message: "入力がありません", actions: [action])
            return;
        }
        self.toNext()
    }
    
    
    @IBAction func onTapSkip(_ sender: Any) {
        
        guard !self.mIsAnimating else {
            return;
        }
        self.toNext()
    }
    
    private func checkText() -> Bool {
        
        if let text = self.textField.text {
            if text.count > 0 {
                if self.pageIndex < 10 {
                    self.likeAnswers.append(text)
                }
                else {
                    self.hateAnswers.append(text)
                }
                return true
            }
        }
        
        return false
    }
    
    
    private func toNext() {
        
        self.pageIndex += 1
        
        if self.pageIndex < self.questions.count {
        
            self.mIsAnimating = true
            
            UIView.animate(withDuration: 0.2, animations: {
                self.contentsView.alpha = 0
            }, completion: { [weak self] _ in
                
                self?.setQuestion()
                self?.textField.text = ""
                
                UIView.animate(withDuration: 0.2, animations: {
                    self?.contentsView.alpha = 1
                }, completion: { [weak self] _ in
                    self?.mIsAnimating = false
                })
            })
            
        }
        else {
            
            // 好き・嫌い登録通信を開始する
            self.likeCreateIndex = 0
            self.hateCreateIndex = 0
            self.createItems()
        }
    }
    
    private func setQuestion() {
        
        if self.pageIndex < 10 {
            self.typeTextView.text = "『好き』を登録するための質問";
            self.countTextView.text = String(self.pageIndex + 1) + String(" / 10");
        } else {
            self.typeTextView.text = "『嫌い』を登録するための質問";
            self.countTextView.text = String(self.pageIndex - 9) + String(" / 6");
        }
        
        self.questionLabel.text = self.questions[self.pageIndex]
    }
    
    // 好き・嫌い登録通信を開始する
    private func createItems() {
        
        var text:String
        
        if self.likeCreateIndex < self.likeAnswers.count {
            text = likeAnswers[self.likeCreateIndex]
        }
        else if self.hateCreateIndex < self.hateAnswers.count {
            text = self.hateAnswers[self.hateCreateIndex]
        }
        else {
            // ユーザ情報を保存して次の画面へ
            self.gotoLikeList()
            return;
        }
        
        
        ItemRequester.sharedManager.creteItem(text, completion:{ (result, itemId) in
            
            if result {
                
                let isLike = (self.likeCreateIndex < self.likeAnswers.count)
                if isLike {
                    
                    if let itemId = itemId
                        , let likes = self.tmpUserData?.likes
                        , likes.contains(itemId) {
                        self.tmpUserData?.likes?.append(itemId)
                    }
                    self.likeCreateIndex += 1;
                }
                else {
                    
                    if let itemId = itemId
                        , let hates = self.tmpUserData?.hates
                        , hates.contains(itemId) {
                        self.tmpUserData?.hates?.append(itemId)
                    }
                    self.hateCreateIndex += 1;
                }
                // 次の通信を行う
                self.createItems()
            }
            else {
                
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
            }
        })
        
    }
    
    // ユーザ情報を保存して、他の人はこんな物を登録しています画面へ遷移する
    private func gotoLikeList() {
    
        AccountRequester.sharedManager.update(userData: self.tmpUserData, completion:{(result) in
        
            if result {
                
                let saveData = SaveData.shared
                saveData.isInitialized = true
                saveData.save()
    
                let myPageViewController = self.viewController(storyboard: "Main", identifier: "MyPageViewController") as! MyPageViewController
                self.stack(viewController: myPageViewController, animationType: .vertical)
            }
            else {
                
                let action = AlertAction(title:"OK")
                self.showAlert(title: "エラー", message: "通信に失敗しました", actions: [action])
            }
        })
    }
    
}
