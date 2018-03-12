//
//  ImageStorage.swift
//  SchoolChat
//
//  Created by Leapfrog-Software on 2017/12/09.
//  Copyright © 2017年 Leapfrog-Inc. All rights reserved.
//

import UIKit

private struct ImageStorageData {
    
    let url: String
    let image: UIImage
}

private struct ImageRequestData {
    
    let url: String
    let imageView: UIImageView
}

class ImageStorage {
    
    // シングルトン
    static let shared = ImageStorage()
    // 取得済み画像リスト
    private var imageList = [ImageStorageData]()
    // リクエスト中のリスト
    private var requestList = [ImageRequestData]()
    
    // 画像を取得
    func fetch(url: String, imageView: UIImageView, defaultImage: UIImage? = nil) {
        
        // 取得済みの処理
        if let image = (self.imageList.filter { $0.url == url }).first?.image {
            imageView.image = image
            return
        }
        
        // 初期化
        imageView.image = nil
        
        // リクエスト中の場合はキャンセルする
        self.cancelRequest(imageView: imageView)
        
        // リクエストデータを作成
        let request = ImageRequestData(url: url, imageView: imageView)
        
        // リクエスト中として登録
        self.requestList.append(request)
        
        // リクエスト開始
        HttpManager.get(url: url) { [weak self] (result, data) in
            
            // リクエスト中リストにない場合は処理終了
            guard let requestData = (self?.requestList.filter{ $0.url == url })?.first else {
                return
            }
            
            // レスポンスデータから画像データを取得
            var resultImage: UIImage?
            if result, let data = data, let image = UIImage(data: data) {
                resultImage = image
            } else {
                
                // 取得失敗時
                if let defaultImage = defaultImage {
                    // デフォルト画像
                    resultImage = defaultImage
                } else {
                    // noImage画像
                    resultImage = UIImage(named: "common_no_face")
                }
            }
            
            // 取得完了画像クラス生成
            let storageData = ImageStorageData(url: url, image: resultImage!)
            
            // リスト登録
            self?.imageList.append(storageData)
            
            // リクエスト中リストから除外
            self?.cancelRequest(imageView: requestData.imageView)
            
            // イメージ設定
            requestData.imageView.image = resultImage!
        }
    }
    
    // URLから画像を検索
    func query(url: String) -> UIImage? {
        return (self.imageList.filter { $0.url == url }).first?.image
    }
    
    // キャンセル処理
    func cancelRequest(imageView: UIImageView) {
        
        // キャンセル対象のインデックスリスト
        var sameIndexes = [Int]()
        
        // リクエスト中データから
        for i in 0..<self.requestList.count {
            
            // キャンセル対象を抽出
            if self.requestList[i].imageView == imageView {
                sameIndexes.append(i)
            }
        }
        
        // リクエスト中データから削除
        sameIndexes.forEach{ self.requestList.remove(at: $0) }
    }
}
