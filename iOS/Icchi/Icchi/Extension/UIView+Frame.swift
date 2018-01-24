//
//  UIView+Frame.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIView {
    
    func absoluteRect() -> CGRect {
        
        guard let window = UIApplication.shared.keyWindow else {
            return .zero
        }
        return self.convert(self.bounds, to: window)
    }
}
