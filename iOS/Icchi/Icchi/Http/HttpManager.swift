//
//  HttpManager.swift
//  Icchi
//
//  Created by oonaka on 2018/02/16.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

/** HTTPクラス */
class HttpManager {
    
    // GETリクエスト
    class func get(url: String, completion: @escaping ((Bool, Data?) -> ())) {
        
        // パラメータはurlに付与済みを想定
        
        // リクエスト実施
        HttpManager.request(url: url, method: "GET", body: nil, completion: completion)
    }
    
    // POSTリクエスト
    class func post(url: String, params: [String: String]?, completion: @escaping ((Bool, Data?) -> ())) {
        
        // リクエストパラメータをクエリ化
        var paramsString = ""
        params?.forEach { param in
            if paramsString.count > 0 {
                paramsString += "&"
            }
            paramsString += param.key
            paramsString += "="
            paramsString += param.value
        }
        
        // UTF-8でDATA化
        let paramsData = paramsString.data(using: .utf8)
        
        // リクエスト実施
        HttpManager.request(url: url, method: "POST", body: paramsData, completion: completion)
    }
    
    // リクエスト処理
    class func request(url: String, method: String, body: Data?, additionalHeader: [String: String]? = nil, completion: @escaping ((Bool, Data?) -> ())) {
        
        // URLチェック
        guard let urlRaw = URL(string: url) else {
            
            // 失敗通知
            completion(false, nil)
            return
        }
        
        // リクエストクラスを作成
        var request = URLRequest(url: urlRaw, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: Constants.HttpTimeOutInterval)
        
        // ヘッダーを付与
        additionalHeader?.keys.forEach { key in
            if let value = additionalHeader?[key] {
                request.addValue(value, forHTTPHeaderField: key)
            }
        }
        
        // リクエスト種別
        request.httpMethod = method
        // Body
        request.httpBody = body
        
        // リクエストタスク生成
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
            
            // レスポンス時はメインスレッド化
            DispatchQueue.main.async {
                

                // レスポンス内容確認
                if error == nil, let data = data {
                    
                    // 成功＋受信データ
                    completion(true, data)
                } else {
                    
                    // 失敗通知
                    completion(false, nil)
                }
            }
        }
        
        // タスク実行
        task.resume()
    }
}
