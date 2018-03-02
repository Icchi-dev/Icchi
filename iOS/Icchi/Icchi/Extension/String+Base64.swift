//
//  String+Base64.swift
//  SchoolChat
//
//  Created by Leapfrog-Software on 2017/12/09.
//  Copyright © 2017年 Leapfrog-Inc. All rights reserved.
//

import Foundation

extension String {
    
    func base64Encode() -> String? {
        let data = self.data(using: Constants.StringEncoding)
        return data?.base64EncodedString()
    }
    
    func base64Decode() -> String? {
        let replaced = self.replacingOccurrences(of: " ", with: "+")
        if let data = Data(base64Encoded: replaced) {
            return String(data: data, encoding: Constants.StringEncoding)
        }
        return nil
    }
}
