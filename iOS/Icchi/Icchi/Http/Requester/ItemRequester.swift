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
    
    // レスポンスデータ型
    private struct createResult : Codable {
        var result:Bool
        var itemId:String?
        private enum CodingKeys: String, CodingKey {
            case result
            case itemId
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).isEqual(to: "0")
            itemId = try values.decode(String.self, forKey: .itemId)
        }
    }
    
    
    // シングルトン
    static let sharedManager = ItemRequester()
    private init() {
    }
    
    // アイテムデータ配列
    fileprivate(set) var mDataList:[ItemData] = []
    
    
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
                    self.mDataList = users
                }
                
                // 成功
                completion(result.result)
            }
            catch {
                // 失敗
                completion(false)
            }
        }
    }
    
    /** データ取得 */
    func query(_ itemId:String) -> ItemData? {
        return mDataList.first(where: { return $0.itemId == itemId})
    }
    
    /** データID取得 */
    func queryId(_ itemName:String) -> String? {
        let data = mDataList.first(where: { return $0.name == itemName})
        return data?.itemId
    }
    
    /** データ作成 */
    func creteItem(_ itemName:String, completion: @escaping ((Bool, String?) -> ())) {
        
        // リクエストデータ作成
        let params = [
            "command": "createItem",
            "itemName": "itemName"
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
                let result = try JSONDecoder().decode(createResult.self, from: data)
                
                // 成功
                completion(result.result, result.itemId)
            }
            catch {
                // 失敗
                completion(false, nil)
            }
        }
    }
    
}
