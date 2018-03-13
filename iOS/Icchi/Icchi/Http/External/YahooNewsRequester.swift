//
//  YahooNewsRequester.swift
//  Icchi
//
//  Created by oonaka on 2018/03/13.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class YahooNewsRequester: NSObject {
    
    // レスポンスデータ型
    private struct YahooNewsData : Codable {
        var title:String?
        var link:String?
        var category:String?
        var pubDate:String?
    }
    
    private var tmpNewsData:YahooNewsData? = nil
    private var titleOn = false;
    private var linkOn = false;
    private var categoryOn = false;
    private var pubDateOn = false;
    
    // データ配列
    private var mNewsList:[YahooNewsData] = []
    private var mRssIndex:Int = 0;
    
    // rss
    var rssType:[String] = [
        "https://headlines.yahoo.co.jp/rss/all-dom.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-dom.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-c_int.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-bus.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-c_ent.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-c_spo.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-c_sci.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-c_life.xml"
        ,"https://headlines.yahoo.co.jp/rss/all-loc.xml"]
    
    // シングルトン
    static let sharedManager = YahooNewsRequester()
    
    /** データ取得 */
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        self.mRssIndex = 0;
        self.mNewsList = []
        
        self.fetchSingle(completion: completion);
    }
    
    func fetchSingle(completion: @escaping ((Bool) -> ())) {
        
        // リクエストデータ作成
        let urlString = self.rssType[self.mRssIndex]
        
        // リクエスト実施
        HttpManager.get(url:urlString){ (result:Bool, data:Data?) in
            
            // レスポンスチェック
            guard result, let data = data else {
                // 失敗
                completion(false)
                return
            }
            
            // 解析
            if !self.analyze(data:data) {
                
                // 失敗
                completion(false)
            }
            
            // 成功
            completion(true)
        }
    }
    
    func analyze(data:Data?) -> Bool {
        guard let data = data else {
            return false;
        }
        
        // XML解析
        let parser = XMLParser(data: data)
        parser.delegate = self
        return parser.parse()
    }
}

extension YahooNewsRequester :XMLParserDelegate {
    
    // 解析開始
    public func parserDidStartDocument(_ parser: XMLParser) {
        print("XML解析開始")
    }
    // XML解析終了
    public func parserDidEndDocument(_ parser: XMLParser) {
        print("XML解析終了")
        
    }
    // エラーが発生
    public func parser(_ parser: XMLParser, parseErrorOccurred parseError: Error) {
        print("XML解析エラー:" + parseError.localizedDescription)
    }
    
    // タグの始まり
    public func parser(_ parser: XMLParser, didStartElement elementName: String, namespaceURI: String?, qualifiedName qName: String?, attributes attributeDict: [String : String] = [:]) {
        
        if "item".elementsEqual(elementName) {
            self.tmpNewsData = YahooNewsData();
        }
        if "title".elementsEqual(elementName) {
            self.titleOn = true;
        }
        if "link".elementsEqual(elementName) {
            self.linkOn = true;
        }
        if "category".elementsEqual(elementName) {
            self.categoryOn = true;
        }
        if "pubDate".elementsEqual(elementName) {
            self.pubDateOn = true;
        }
    }
    
    // タグ間の文字列
    public func parser(_ parser: XMLParser, foundCharacters string: String){
        
        if self.titleOn {
            self.tmpNewsData?.title = string
        }
        if self.linkOn {
            self.tmpNewsData?.link = string
        }
        if self.categoryOn {
            self.tmpNewsData?.category = string
        }
        if self.pubDateOn {
            self.tmpNewsData?.pubDate = string
        }
    }
    
    // タグの終わり
    public func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?){
        // 終了
        if "item".elementsEqual(elementName) {
            self.mNewsList.append(self.tmpNewsData!)
            self.tmpNewsData = nil;
        }
        if "title".elementsEqual(elementName) {
            self.titleOn = false;
        }
        if "link".elementsEqual(elementName) {
            self.linkOn = false;
        }
        if "category".elementsEqual(elementName) {
            self.categoryOn = false;
        }
        if "pubDate".elementsEqual(elementName) {
            self.pubDateOn = false;
        }
    }
    
}
