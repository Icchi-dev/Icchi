package leapfrog_inc.icchi.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class AccountRequester {

    public static void register(String email, String password, final RegisterCallback callback){

        HttpManager http = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        String userId = jsonObject.getString("userId");
                        callback.didReceive(ret.equals("0"), userId);
                        return;
                    } catch(Exception e) {}
                }
                callback.didReceive(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=register");
        param.append("&");
        param.append("email=" + email);
        param.append("&");
        param.append("password=" + password);
        http.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface RegisterCallback {
        void didReceive(boolean result, String userId);
    }

    public static void login(String email, String password, final LoginCallback callback){

        HttpManager http = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        String userId = jsonObject.getString("userId");
                        callback.didReceive(ret.equals("0"), userId);
                        return;
                    } catch(Exception e) {}
                }
                callback.didReceive(false, null);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=login");
        param.append("&");
        param.append("email=" + email);
        param.append("&");
        param.append("password=" + password);
        http.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface LoginCallback {
        void didReceive(boolean result, String userId);
    }
}
