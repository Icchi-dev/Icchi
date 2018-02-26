//
//  UserRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/02/15.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation


class UserRequester {
    
    // 年齢
    enum AgeType: String, Codable {
        case u20 = "0"
        case s20 = "1"
        case s30 = "2"
        case s40 = "3"
        case s50 = "4"
        case o60 = "5"
        
        func convert() -> String {
            if (self == AgeType.u20
                || self == AgeType.s20
                || self == AgeType.s30
                || self == AgeType.s40
                || self == AgeType.s50
                || self == AgeType.o60
                ) {
                return self.rawValue;
            } else {
                return "";
            }
        }
        
        func display() -> String {
            if (self == AgeType.u20) {
                return "20歳未満";
            } else if (self == AgeType.s20) {
                return "20代";
            } else if (self == AgeType.s30) {
                return "30代";
            } else if (self == AgeType.s40) {
                return "40代";
            } else if (self == AgeType.s50) {
                return "50代";
            } else if (self == AgeType.o60) {
                return "60歳以上";
            } else {
                return "";
            }
        }
    }
    
    // 性別
    enum GenderType: String, Codable {
        case male = "0"
        case female = "1"
    
        func convert() -> String {
            if (self == GenderType.male
                || self == GenderType.female) {
                return self.rawValue;
            } else {
                return "";
            }
        }
        
        func display() -> String {
            if (self == GenderType.male) {
                return "男性";
            } else if (self == GenderType.female) {
                return "女性";
            } else {
                return "";
            }
        }
    }
    
    // レスポンスデータ型
    private struct fetchResult : Codable {
        var result:Bool
        var users:[UserData]?
        private enum CodingKeys: String, CodingKey {
            case result
            case users
        }
        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            result = NSString(string:try values.decode(String.self, forKey: .result)).isEqual(to: "0")
            users = try values.decode([UserData].self, forKey: .users)
        }
    }
    
    // ユーザーデータ
    struct UserData : Codable {
        var userId:String? = nil
        var name:String? = nil
        var age:AgeType? = nil
        var gender:GenderType? = nil
        var likes:[String]? = [""]
        var hates:[String]? = [""]
        var image:String? = ""
        var fbLink:String? = ""

        private enum CodingKeys: String, CodingKey {
            case userId
            case name
            case age
            case gender
            case likes
            case hates
            case image
            case fbLink
        }

        init(from decoder: Decoder) throws {
            let values = try decoder.container(keyedBy: CodingKeys.self)
            userId = try? values.decode(String.self, forKey: .userId)
            name = try? values.decode(String.self, forKey: .name)
            age = AgeType(rawValue:try values.decode(String.self, forKey: .age))
            gender = GenderType(rawValue:try values.decode(String.self, forKey: .gender))
            likes = try? values.decode(String.self, forKey: .likes).components(separatedBy: "-")
            hates = try? values.decode(String.self, forKey: .hates).components(separatedBy: "-")
            image = try? values.decode(String.self, forKey: .image)
            fbLink = try? values.decode(String.self, forKey: .fbLink)
        }
        
    }
    
    // シングルトン
    static let sharedManager = UserRequester()
    private init() {
    }
    
    // ユーザーデータ配列
    fileprivate(set) var mDataList:[UserData] = []
    
    /** データ取得 */
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        // リクエストデータ作成
        let params = ["command": "getUser"]
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
                
                // ユーザーIDが存在するリストを登録
                if let users = result.users {
                    self.mDataList = users.filter({ (userData) -> Bool in
                        if let userId = userData.userId {
                            return userId.count > 0
                        }
                        return false
                    })
                }
                
                // 成功
                completion(result.result)
            }
            catch let error {
                // 失敗
                print("UserRequester decode error = \(error)")
                completion(false)
            }
        }
    }
    
    /** データ取得 */
    func query(userId:String) -> UserData? {
        return mDataList.first(where: { return $0.userId == userId})
    }
}

