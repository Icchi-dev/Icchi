package leapfrog_inc.icchi.Http.External;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;

/**
 * Created by Leapfrog-Software on 2018/03/02.
 */

public class YahooNewsRequester {

    public static class YahooNewsData {

        public String title;
        public String link;

    }

    private static YahooNewsRequester requester = new YahooNewsRequester();

    private YahooNewsRequester(){}

    public static YahooNewsRequester getInstance(){
        return requester;
    }

    private ArrayList<YahooNewsData> mDataList = new ArrayList<YahooNewsData>();
    private int mRssIndex = 0;

    private static ArrayList<String> rssList() {
        return new ArrayList<String>() {
            {
                add("https://headlines.yahoo.co.jp/rss/all-dom.xml");
                add("https://headlines.yahoo.co.jp/rss/all-dom.xml");
                add("https://headlines.yahoo.co.jp/rss/all-c_int.xml");
                add("https://headlines.yahoo.co.jp/rss/all-bus.xml");
                add("https://headlines.yahoo.co.jp/rss/all-c_ent.xml");
                add("https://headlines.yahoo.co.jp/rss/all-c_spo.xml");
                add("https://headlines.yahoo.co.jp/rss/all-c_sci.xml");
                add("https://headlines.yahoo.co.jp/rss/all-c_life.xml");
                add("https://headlines.yahoo.co.jp/rss/all-loc.xml");
            }
        };
    }

    public void fetch(final YahooNewsRequesterCallback callback) {

        mRssIndex = 0;
        mDataList.clear();

        fetchSingle(callback);
    }

    private void fetchSingle(final YahooNewsRequesterCallback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    analyze(data);

                    if (mRssIndex >= YahooNewsRequester.rssList().size() - 1) {
                        callback.didReceiveData(true);
                    } else {
                        mRssIndex += 1;
                        fetchSingle(callback);
                    }
                } else {
                    callback.didReceiveData(false);
                }
            }
        });
        httpManager.execute(YahooNewsRequester.rssList().get(mRssIndex), "GET", "");
    }

    private void analyze(String data) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(data));

            int eventType = parser.getEventType();
            boolean itemFound = false;
            String title = null;
            String link = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    String name = parser.getName();

                    if (name.equals("item")) {
                        itemFound = true;
                        title = null;
                        link = null;
                    } else if (itemFound) {
                        if (name.equals("title")) {
                            title = parser.nextText();
                        } else if (name.equals("link")) {
                            link = parser.nextText();
                        }
                        if ((title != null) && (link != null)) {
                            YahooNewsData newsData = new YahooNewsData();
                            newsData.title = title;
                            newsData.link = link;
                            mDataList.add(newsData);

                            itemFound = false;
                            title = null;
                            link = null;
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch(Exception e) {}
    }

    public interface YahooNewsRequesterCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<YahooNewsData> getDataList() {
        return mDataList;
    }
}
