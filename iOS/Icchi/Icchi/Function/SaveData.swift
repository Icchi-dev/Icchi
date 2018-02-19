//
//  SaveData.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class SaveData {
    
    static let shared = SaveData()
    
    var userId = ""
    var isInitialized = false
    
    init() {
        let userDefaults = UserDefaults()
        self.userId = userDefaults.string(forKey: Constants.UserDefaultsKey.UserId) ?? ""
        self.isInitialized = userDefaults.bool(forKey: Constants.UserDefaultsKey.IsInitialized)
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        userDefaults.set(self.userId, forKey: Constants.UserDefaultsKey.UserId)
        userDefaults.set(self.isInitialized, forKey: Constants.UserDefaultsKey.IsInitialized)

        userDefaults.synchronize()
    }
}

