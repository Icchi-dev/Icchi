//
//  AccountRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/02/22.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class AccountRequester {
    
    // レスポンスデータ型
    private struct RegisterResult : Codable {
        var result:Bool
        var userId:String
        private enum CodingKeys: String, CodingKey {
            case result
            case userId
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).isEqual(to: "0")
            userId = try values.decode(String.self, forKey: .userId)
        }
    }
    
    // シングルトン
    static let sharedManager = AccountRequester()
    private init() {
    }
    
    /** 登録 */
    func register(email:String, password:String, name:String, image:String, fbLink:String,  completion:@escaping((Bool, String?)->Void)) -> Void {
        
        // リクエストデータ作成
        let params = ["command": "register"
                    , "email":email
                    , "password":password
                    , "name":name
                    , "image":image
                    , "fbLink":fbLink
                    ]
        // リクエスト実施
        HttpManager.post(url: Constants.ServerApiUrl, params: params) { (result:Bool, data:Data?) in
            
            // レスポンスチェック
            guard result, let data = data else {
                // 失敗
                completion(false, nil)
                return
            }
            
            do {
                // レスポンス取得
                let result = try JSONDecoder().decode(RegisterResult.self, from: data)
                
                // 成功
                completion(result.result, result.userId)
            }
            catch let error {
                // 失敗
                print("AccountRequester regist decode error = \(error)")
                completion(false, nil)
            }
        }
    }
    
    /** ログイン */
    func login(email:String, password:String, completion:@escaping((Bool, String?)->Void)) -> Void {
        
        // リクエストデータ作成
        let params = ["command": "login"
            , "email":email
            , "password":password
        ]
        // リクエスト実施
        HttpManager.post(url: Constants.ServerApiUrl, params: params) { (result:Bool, data:Data?) in
            
            // レスポンスチェック
            guard result, let data = data else {
                // 失敗
                completion(false, nil)
                return
            }
            
            do {
                // レスポンス取得
                let result = try JSONDecoder().decode(RegisterResult.self, from: data)
                
                // 成功
                completion(result.result, result.userId)
            }
            catch let error {
                // 失敗
                print("AccountRequester login decode error = \(error)")
                completion(false, nil)
            }
        }
    }
}
