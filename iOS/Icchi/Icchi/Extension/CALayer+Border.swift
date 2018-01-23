//
//  CALayer+Border.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension CALayer {
    
    @objc var borderUIColor: UIColor? {
        get {
            return borderColor == nil ? nil : UIColor(cgColor: borderColor!)
        }
        set {
            borderColor = newValue == nil ? nil : newValue!.cgColor
        }
    }
}
