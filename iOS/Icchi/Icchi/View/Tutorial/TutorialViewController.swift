//
//  TutorialViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class TutorialViewController: UIViewController {

    @IBOutlet private weak var contentsView: UIView!
    @IBOutlet private weak var questionLabel: UILabel!
    @IBOutlet private weak var textField: UITextField!

    private var currentQuestionIndex = 0
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
        , "嫌いな強化は？"
    ]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.questionLabel.text = self.questions[0]
    }
    
    private func goToNextQuestion() {
        
        if self.currentQuestionIndex >= self.questions.count - 1 {
            let saveData = SaveData.shared
            saveData.isInitialized = true
            saveData.save()            
            
            self.pop(animationType: .vertical)
            return
        }
        self.currentQuestionIndex += 1
        let nextQuestion = self.questions[self.currentQuestionIndex]
        
        UIView.animate(withDuration: 0.2, animations: {
            self.contentsView.alpha = 0
        }, completion: { [weak self] _ in            
            self?.questionLabel.text = nextQuestion
            self?.textField.text = ""
            
            UIView.animate(withDuration: 0.2, animations: {
                self?.contentsView.alpha = 1
            })
        })
    }
    
    @IBAction func didEndEditTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapNext(_ sender: UITextField) {
        
        guard self.checkText() else {
            let action = AlertAction(title:"OK")
            self.showAlert(title: "エラー", message: "入力がありません", actions: [action])
            return;
        }
        self.goToNextQuestion()
    }
    
    private func checkText() -> Bool {
        return self.textField.text?.count ?? 0 > 0;
    }
    
    @IBAction func onTapSkip(_ sender: Any) {
        self.goToNextQuestion()
    }
}
