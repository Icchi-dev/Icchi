//
//  Array+shuffle.swift
//  Icchi
//
//  Created by oonaka on 2018/05/14.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

extension Array {
    
    mutating func shuffle() {
        for i in 0..<self.count {
            let j = Int(arc4random_uniform(UInt32(self.indices.last!)))
            if i != j {
                self.swapAt(i, j)
            }
        }
    }
    
    var shuffled: Array {
        var copied = Array<Element>(self)
        copied.shuffle()
        return copied
    }
}
