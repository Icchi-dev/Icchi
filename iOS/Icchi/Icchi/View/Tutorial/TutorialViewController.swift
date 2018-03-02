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
        "好きなアーティストは？",
        "質問2",
        "質問3",
        "質問4",
        "質問5",
        "質問6",
        "質問7",
        "質問8",
        "質問9",
        "質問10",
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
        
        UIView.animate(withDuration: 0.15, animations: {
            self.contentsView.alpha = 0
        }, completion: { [weak self] _ in            
            self?.questionLabel.text = nextQuestion
            self?.textField.text = ""
            
            UIView.animate(withDuration: 0.15, animations: {
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
