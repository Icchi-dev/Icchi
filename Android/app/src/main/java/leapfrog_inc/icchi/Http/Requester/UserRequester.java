package leapfrog_inc.icchi.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class UserRequester {

    public enum AgeType {
        u20,
        s20,
        s30,
        s40,
        s50,
        o60;

        public static AgeType init(String string) {
            if (string.equals("0")) {
                return AgeType.u20;
            } else if (string.equals("1")) {
                return AgeType.s20;
            } else if (string.equals("2")) {
                return AgeType.s30;
            } else if (string.equals("3")) {
                return AgeType.s40;
            } else if (string.equals("4")) {
                return AgeType.s50;
            } else if (string.equals("5")) {
                return AgeType.o60;
            } else {
                return null;
            }
        }

        public String convert() {
            if (this == AgeType.u20) {
                return "0";
            } else if (this == AgeType.s20) {
                return "1";
            } else if (this == AgeType.s30) {
                return "2";
            } else if (this == AgeType.s40) {
                return "3";
            } else if (this == AgeType.s50) {
                return "4";
            } else if (this == AgeType.o60) {
                return "5";
            } else {
                return "";
            }
        }

        public String display() {
            if (this == AgeType.u20) {
                return "20歳未満";
            } else if (this == AgeType.s20) {
                return "20代";
            } else if (this == AgeType.s30) {
                return "30代";
            } else if (this == AgeType.s40) {
                return "40代";
            } else if (this == AgeType.s50) {
                return "50代";
            } else if (this == AgeType.s50) {
                return "60歳以上";
            } else {
                return "";
            }
        }
    }

    public enum GenderType {
        male,
        female;

        public static GenderType init(String string) {
            if (string.equals("0")) {
                return GenderType.male;
            } else if (string.equals("1")) {
                return GenderType.female;
            } else {
                return null;
            }
        }

        public String convert() {
            if (this == GenderType.male) {
                return "0";
            } else if (this == GenderType.female) {
                return "1";
            } else {
                return "";
            }
        }

        public String display() {
            if (this == GenderType.male) {
                return "男性";
            } else if (this == GenderType.male) {
                return "女性";
            } else {
                return "";
            }
        }
    }

    public static class UserData {

        public String userId = "";
        public String name = "";
        public AgeType age = null;
        public GenderType gender = null;
        public ArrayList<String> likes = new ArrayList<String>();
        public ArrayList<String> hates = new ArrayList<String>();
        public String image = "";
        public String fbLink = "";

        static public UserData create(JSONObject json) {

            try {
                String userId = json.getString("userId");
                String name = json.getString("name");
                String age = json.getString("age");
                String gender = json.getString("gender");
                String[] likes = json.getString("likes").split("-");
                String[] hates = json.getString("hates").split("-");
                String image = json.getString("image");
                String fbLink = json.getString("fbLink");

                UserData userData = new UserData();
                userData.userId = userId;
                userData.name = name;
                userData.age = AgeType.init(age);
                userData.gender = GenderType.init(gender);

                userData.likes = new ArrayList<String>();
                for (int i = 0; i < likes.length; i++) {
                    userData.likes.add(likes[i]);
                }

                userData.hates = new ArrayList<String>();
                for (int i = 0; i < hates.length; i++) {
                    userData.hates.add(hates[i]);
                }

                userData.image = image;
                userData.fbLink = fbLink;

                return userData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static UserRequester requester = new UserRequester();

    private UserRequester(){}

    public static UserRequester getInstance(){
        return requester;
    }

    private ArrayList<UserData> mDataList = new ArrayList<UserData>();

    public void fetch(final UserRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            ArrayList<UserData> dataList = new ArrayList<UserData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UserData userData = UserData.create(jsonArray.getJSONObject(i));
                                if (userData != null) {
                                    dataList.add(userData);
                                }
                            }
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
        param.append("command=getUser");
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface UserRequesterCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<UserData> getDataList() {
        return mDataList;
    }

    public UserData query(String userId) {

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).userId.equals(userId)) {
                return mDataList.get(i);
            }
        }
        return null;
    }

    public int queryMatch(String targetId) {

        UserData targetUserData = query(targetId);
        UserData myUserData = query(SaveData.getInstance().userId);
        if ((targetUserData == null) || (myUserData == null)) {
            return 0;
        }

        int match = 0;

        for (int i = 0; i < targetUserData.likes.size(); i++) {
            String itemId = targetUserData.likes.get(i);
            if (myUserData.likes.contains(itemId)) {
                if (isMinorItem(itemId)) {
                    match += 20;
                } else {
                    match += 5;
                }
            }
        }
        for (int i = 0; i < targetUserData.hates.size(); i++) {
            String itemId = targetUserData.hates.get(i);
            if (myUserData.likes.contains(itemId)) {
                if (isMinorItem(itemId)) {
                    match += 20;
                } else {
                    match += 5;
                }
            }
        }
        if (match > 100) {
            match = 100;
        }
        return match;
    }

    private boolean isMinorItem(String itemId) {

        int count = 0;
        for (int i = 0; i < mDataList.size(); i++) {
            UserData userData = mDataList.get(i);
            if (userData.likes.contains(itemId)) {
                count++;
            } else if (userData.hates.contains(itemId)) {
                count++;
            }
        }
        if (count * 100 < mDataList.size()) {
            return true;
        } else {
            return false;
        }
    }
}
