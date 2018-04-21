package leapfrog_inc.icchi.Function;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class SaveData {

    private static SaveData container = null;

    public Context mContext;
    public String userId = "";
    public boolean isInitialized = false;

    private String mVersion = "3";

    private SaveData(){}

    public static SaveData getInstance(){
        if(container == null){
            container = new SaveData();
        }
        return container;
    }

    public void initialize(Context context) {

        mContext = context;

        SharedPreferences data = context.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);

        String savedVersion = data.getString(Constants.SharedPreferenceKey.Version, "");
        if (savedVersion.equals(mVersion)) {
            userId = data.getString(Constants.SharedPreferenceKey.UserId, "");
            isInitialized = data.getBoolean(Constants.SharedPreferenceKey.IsInitialized, false);
        } else {
            userId = "";
            isInitialized = false;
        }
    }

    public void save() {

        SharedPreferences data = mContext.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        editor.putString(Constants.SharedPreferenceKey.Version, mVersion);
        editor.putString(Constants.SharedPreferenceKey.UserId, userId);
        editor.putBoolean(Constants.SharedPreferenceKey.IsInitialized, isInitialized);

        editor.apply();
    }
}
