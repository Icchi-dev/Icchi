//
//  AppDelegate.swift
//  Icchi
//
//  Created by Leapfrog-Software on 2018/01/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import FBSDKCoreKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        
        return true
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplicationOpenURLOptionsKey : Any] = [:]) -> Bool {
        
        return FBSDKApplicationDelegate.sharedInstance().application(app,
                                                                     open: url,
                                                                     sourceApplication: options[UIApplicationOpenURLOptionsKey.sourceApplication] as? String ?? "",
                                                                     annotation: options[UIApplicationOpenURLOptionsKey.annotation] as? String ?? "")
    }
}

