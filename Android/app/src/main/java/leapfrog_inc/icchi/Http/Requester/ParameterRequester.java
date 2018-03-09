package leapfrog_inc.icchi.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/03/09.
 */

public class ParameterRequester {

    private static ParameterRequester requester = new ParameterRequester();

    private ParameterRequester(){}

    public static ParameterRequester getInstance(){
        return requester;
    }

    public int mPointPerItem;
    public int mPointPerMinorItem;
    public int mMinorThreshold;

    public void fetch(final ParameterRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            int pointPerItem = Integer.parseInt(jsonObject.getString("pointPerItem"));
                            int pointPerMinorItem = Integer.parseInt(jsonObject.getString("pointPerMinorItem"));
                            int minorThreshold = Integer.parseInt(jsonObject.getString("minorThreshold"));

                            mPointPerItem = pointPerItem;
                            mPointPerMinorItem = pointPerMinorItem;
                            mMinorThreshold = minorThreshold;

                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=getMatchParameter");
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface ParameterRequesterCallback {
        void didReceiveData(boolean result);
    }

}
