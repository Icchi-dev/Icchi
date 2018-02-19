//
//  Constants.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct Constants {
    
    static let HttpTimeOutInterval = TimeInterval(10)
    static let ServerApiUrl = "http://lfrogs.sakura.ne.jp/icchi/srv.php"
    static let StringEncoding = String.Encoding.utf8
    
    struct UserDefaultsKey {
        static let Key = "Icchi"
        static let UserId = "UserId"
        static let IsInitialized = "IsInitialized"
    }
}
