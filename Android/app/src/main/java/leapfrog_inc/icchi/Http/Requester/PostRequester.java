package leapfrog_inc.icchi.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class PostRequester {

    public static class PostData {

        public String title;
        public String source;
        public ArrayList<String> relates;
        public String sumbnail;
        public String link;
        public int order;

        static public PostData create(JSONObject json) {

            try {
                String title = json.getString("title");
                String source = json.getString("source");
                String[] relates = json.getString("relates").split("-");
                String sumbnail = json.getString("sumbnail");
                String link = json.getString("link");
                int order = Integer.parseInt(json.getString("order"));
                PostData postData = new PostData();
                postData.title = title;
                postData.source = source;
                postData.relates = new ArrayList<String>();
                for (int i = 0; i < relates.length; i++) {
                    postData.relates.add(relates[i]);
                }
                postData.sumbnail = sumbnail;
                postData.link = link;
                postData.order = order;

                return postData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static PostRequester requester = new PostRequester();

    private PostRequester(){}

    public static PostRequester getInstance(){
        return requester;
    }

    private ArrayList<PostData> mDataList = new ArrayList<PostData>();

    public void fetch(final PostRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("posts");
                            ArrayList<PostData> dataList = new ArrayList<PostData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PostData postData = PostData.create(jsonArray.getJSONObject(i));
                                if (postData != null) {
                                    dataList.add(postData);
                                }
                            }
                            Collections.sort(dataList, new Comparator<PostData>() {
                                @Override
                                public int compare(PostData p1, PostData p2) {
                                    if (p1.order == p2.order) {
                                        return 0;
                                    } else if (p1.order > p2.order) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            });
                            mDataList = dataList;

                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=getPost");
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public ArrayList<PostData> getDataList() {
        return mDataList;
    }

    public interface PostRequesterCallback {
        void didReceiveData(boolean result);
    }
}
