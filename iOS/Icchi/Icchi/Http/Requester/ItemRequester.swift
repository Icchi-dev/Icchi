//
//  ItemRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/02/19.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class ItemRequester {
    
    // レスポンスデータ型
    private struct fetchResult : Codable {
        var result:Bool
        var items:[ItemData]?
        private enum CodingKeys: String, CodingKey {
            case result
            case items
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).isEqual(to: "0")
            items = try values.decode([ItemData].self, forKey: .items)
        }
    }
    // アイテムデータ
    struct ItemData : Codable {
        var itemId:String? = nil
        var name:String? = nil
        var kana:String? = nil
    }
    
    // シングルトン
    static let sharedManager = ItemRequester()
    private init() {
    }
    
    // アイテムデータ配列
    fileprivate(set) var itemDatas:[ItemData] = []
    
    
    /** データ取得 */
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        // リクエストデータ作成
        let params = ["command": "getItem"]
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
                
                // アイテムデータ配列を登録
                if let users = result.items {
                    self.itemDatas = users
                }
                
                // 成功
                completion(result.result)
            }
            catch let error {
                // 失敗
                print("ItemRequester decode error = \(error)")
                completion(false)
            }
        }
    }
}
