//
//  ProfileAddViewController.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ProfileAddViewController: UIViewController {

    @IBOutlet private weak var sampleView: UIView!
    
    private var isLike = true
    
    func set(isLike: Bool) {
        self.isLike = isLike
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        var offsetX = CGFloat(0)
        var offsetY = CGFloat(0)
        
        for _ in 0..<50 {
            let sampleView = UINib(nibName: "ProfileAddSampleView", bundle: nil).instantiate(withOwner: self, options: nil).first as! ProfileAddSampleView
            sampleView.configure(text: "test")
            if offsetX + sampleView.frame.size.width > self.sampleView.frame.size.width {
                offsetX = 0
                offsetY += self.sampleView.frame.size.height + 10
            }
            if offsetY + sampleView.frame.size.height > self.sampleView.frame.size.height {
                break
            }
            sampleView.frame = CGRect(x: offsetX, y: offsetY, width: sampleView.frame.size.width, height: sampleView.frame.size.height)
            self.sampleView.addSubview(sampleView)
            
            offsetX += sampleView.frame.size.width
            offsetX += 10
        }
    }
    
    @IBAction func onTapMenu(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
}
