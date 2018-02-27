//
//  UIViewController+Alert.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

struct AlertAction {
    let title: String
    let action: ((_ title:String) -> ())?
    
    init(title: String, action: ((_ title:String) -> ())? = nil) {
        self.title = title
        self.action = action
    }
}

extension UIViewController {
    
    func showAlert(title: String? = nil, message: String? = nil, actions: [AlertAction]) {
        
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        actions.forEach { action in
            let alertAction = UIAlertAction(title: action.title, style: .default) { _ in
                action.action?(action.title)
            }
            alertController.addAction(alertAction)
        }
        self.present(alertController, animated: true)
    }
}

