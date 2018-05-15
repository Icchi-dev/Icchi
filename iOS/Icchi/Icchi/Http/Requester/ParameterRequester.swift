//
//  ParameterRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/03/12.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class ParameterRequester {

    // レスポンスデータ型
    private struct fetchResult : Codable {
        var result:Bool
        var pointPerItem:String
        var pointPerMinorItem:String
        var minorThreshold:String
        private enum CodingKeys: String, CodingKey {
            case result
            case pointPerItem
            case pointPerMinorItem
            case minorThreshold
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).isEqual(to: "0")
            pointPerItem = try values.decode(String.self, forKey: .pointPerItem)
            pointPerMinorItem = try values.decode(String.self, forKey: .pointPerMinorItem)
            minorThreshold = try values.decode(String.self, forKey: .minorThreshold)
        }
    }
    
    var mPointPerItem:Int = 0
    var mPointPerMinorItem:Int = 0
    var mMinorThreshold:Int = 0
    
    // シングルトン
    static let sharedManager = ParameterRequester()
    private init() {
    }
    
    /** データ取得 */
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        // リクエストデータ作成
        let params = ["command": "getMatchParameter"]
        // リクエスト実施
        HttpManager.post(url: Constants.ServerApiUrl, params: params) { (result:Bool, data:Data?) in
            
            // レスポンスチェック
            guard result, let data = data else {
                // 失敗
                completion(false)
                return
            }
            
            do {
                // レスポンス取得
                let result = try JSONDecoder().decode(fetchResult.self, from: data)
                
                // ポストデータ配列を登録
                if result.result {
                    self.mPointPerItem = Int(result.pointPerItem) ?? 0
                    self.mPointPerMinorItem = Int(result.pointPerMinorItem) ?? 0
                    self.mMinorThreshold = Int(result.minorThreshold) ?? 0
                }
                
                // 成功
                completion(result.result)
            } catch {
                // 失敗
                completion(false)
            }
        }
    }
}
