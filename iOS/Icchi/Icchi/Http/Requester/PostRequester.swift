//
//  PostRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/02/19.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class PostRequester {
    
    // レスポンスデータ型
    private struct fetchResult : Codable {
        var result:Bool
        var posts:[PostData]?
        private enum CodingKeys: String, CodingKey {
            case result
            case posts
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).boolValue
            posts = try values.decode([PostData].self, forKey: .posts)
        }
    }
    
    // ポストデータ
    struct PostData : Codable {
        var title:String? = nil
        var source:String? = nil
        var relates:[String]? = nil
        var sumbnail:String? = nil
        var link:String? = nil
        
        private enum CodingKeys: String, CodingKey {
            case title
            case source
            case relates
            case sumbnail
            case link
        }
        
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            title = try? values.decode(String.self, forKey: .title)
            source = try? values.decode(String.self, forKey: .source)
            relates = try? values.decode([String].self, forKey: .relates)
            sumbnail = try? values.decode(String.self, forKey: .sumbnail)
            link = try? values.decode(String.self, forKey: .link)
        }
    }
    
    // シングルトン
    static let sharedManager = PostRequester()
    private init() {
    }
    
    // ポストデータ配列
    fileprivate(set) var postDatas:[PostData] = []
    
    /** データ取得 */
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        // リクエストデータ作成
        let params = ["command": "getPost"]
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
                if let posts = result.posts {
                    self.postDatas = posts
                }
                
                // 成功
                completion(true)
            }
            catch let error {
                // 失敗
                print("PostRequester decode error = \(error)")
                completion(false)
            }
        }
    }
}