package leapfrog_inc.icchi.Function;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class FacebookManager {

    private static FacebookManager container = null;

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
                Log.d("", "");
            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("", "");
            }
        });
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        container.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
