package leapfrog_inc.icchi.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.icchi.Function.Constants;
import leapfrog_inc.icchi.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class ItemRequester {

    public static class ItemData {

        public String itemId;
        public String name;
        public String kana;

        static public ItemData create(JSONObject json) {

            try {
                String itemId = json.getString("itemId");
                String name = json.getString("name");
                String kana = json.getString("kana");

                ItemData itemData = new ItemData();
                itemData.itemId = itemId;
                itemData.name = name;
                itemData.kana = kana;

                return itemData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static ItemRequester requester = new ItemRequester();

    private ItemRequester(){}

    public static ItemRequester getInstance(){
        return requester;
    }

    private ArrayList<ItemData> mDataList = new ArrayList<ItemData>();

    public void fetch(final ItemRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            ArrayList<ItemData> dataList = new ArrayList<ItemData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ItemData itemData = ItemData.create(jsonArray.getJSONObject(i));
                                if (itemData != null) {
                                    dataList.add(itemData);
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
        param.append("command=getItem");
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface ItemRequesterCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<ItemData> getDataList() {
        return mDataList;
    }

    public ItemData query(String itemId) {

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).itemId.equals(itemId)) {
                return mDataList.get(i);
            }
        }
        return null;
    }

    public String queryId(String itemName) {

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).name.equals(itemName)) {
                return mDataList.get(i).itemId;
            }
        }
        return null;
    }
}
