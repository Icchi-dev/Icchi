package leapfrog_inc.icchi.Http.Requester;

import android.util.Base64;

import org.json.JSONObject;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class AccountRequester {

    public static void register(String email, String password, String name, String image, String fbLink, final RegisterCallback callback){

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
        param.append("&");
        param.append("name=" + Base64.encodeToString(name.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        param.append("&");
        param.append("image=" + image);
        param.append("&");
        param.append("fbLink=" + fbLink);
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

    public static void update(UserRequester.UserData userData, final UpdateCallback callback) {

        if (userData == null) {
            callback.didReceive(false);
            return;
        }

        HttpManager http = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            callback.didReceive(true);
                            return;
                        }
                    } catch (Exception e) {}
                }
                callback.didReceive(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=updateAccount");
        param.append("&");
        param.append("userId=" + userData.userId);
        param.append("&");

        param.append("name=" + Base64.encodeToString(userData.name.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        param.append("&");
        param.append("age=");
        if (userData.age != null) {
            param.append(userData.age.convert());
        }
        param.append("&");
        param.append("gender=");
        if (userData.gender != null) {
            param.append(userData.gender.convert());
        }
        param.append("&");

        String likes = "";
        for (int i = 0; i < userData.likes.size(); i++) {
            if (likes.length() > 0) {
                likes += "-";
            }
            likes += userData.likes.get(i);
        }
        param.append("likes=" + likes);
        param.append("&");

        String hates = "";
        for (int i = 0; i < userData.hates.size(); i++) {
            if (hates.length() > 0) {
                hates += "-";
            }
            hates += userData.hates.get(i);
        }
        param.append("hates=" + hates);

        param.append("&");
        param.append("image=" + userData.image);
        param.append("&");
        param.append("fbLink=" + userData.fbLink);

        http.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface UpdateCallback {
        void didReceive(boolean result);
    }
}
