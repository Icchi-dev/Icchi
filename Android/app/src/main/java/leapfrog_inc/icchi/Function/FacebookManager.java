package leapfrog_inc.icchi.Function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import leapfrog_inc.icchi.Http.Requester.UserRequester;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class FacebookManager {

    public static class FacebookUserData {
        public String id = "";
        public String name = "";
        public String gender = "";
        public String link = "";
        public String birthday = "";

        public static FacebookUserData create(JSONObject json) {

            FacebookUserData userData = new FacebookUserData();

            try {
                userData.id = json.getString("id");
            } catch(Exception e) {}
            try {
                userData.name = json.getString("name");
            } catch(Exception e) {}
            try {
                userData.gender = json.getString("gender");
            } catch(Exception e) {}
            try {
                userData.link = json.getString("link");
            } catch(Exception e) {}
            try {
                userData.birthday = json.getString("birthday");
            } catch(Exception e) {}

            return userData;
        }

        public UserRequester.UserData toUserData() {

            UserRequester.UserData userData = new UserRequester.UserData();
            userData.name = this.name;
            userData.fbId = this.id;
            userData.image = "https://graph.facebook.com/" + this.id + "/picture";
            userData.fbLink = this.link;

            return userData;
        }
    }

    private static FacebookManager container = null;

    private FacebookManagerCallback mFacebookCallback;
    private CallbackManager mCallbackManager;

    private FacebookManager(){}

    public static void initialize(Context context) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(context);

        container = new FacebookManager();
        container.mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(container.mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fetchUserData();
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {
                Log.d("", "");
            }
        });
    }

    public static void setCallback(FacebookManagerCallback callback) {
        container.mFacebookCallback = callback;
    }

    public static void fetchUserData() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            container.mFacebookCallback.didLogin(false, null);
            return;
        }

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                FacebookUserData userData = FacebookUserData.create(object);
                container.mFacebookCallback.didLogin(true, userData);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        container.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface FacebookManagerCallback {
        void didLogin(boolean result, FacebookUserData fbUserData);
    }
}
