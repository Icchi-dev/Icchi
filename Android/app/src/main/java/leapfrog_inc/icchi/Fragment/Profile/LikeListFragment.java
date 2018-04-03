package leapfrog_inc.icchi.Fragment.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Match.MatchFragment;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.AccountRequester;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/03/09.
 */

public class LikeListFragment extends BaseFragment {

    static ArrayList<Integer> mCheckList = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_like_list, null);

        initListView(view);

        ((Button)view.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            gotoProfile();
            }
        });

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
            }
        });

        return view;
    }

    private void initListView(View view) {

        ListView listView = (ListView)view.findViewById(R.id.listView);
        LikeListAdapter adapter = new LikeListAdapter(getActivity());

        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getDataList();
        for (int i = userList.size() - 1; i >= 0; i--) {
            adapter.add(userList.get(i));
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    if (mCheckList.contains(i)) mCheckList.remove(i);
                } else {
                    checkBox.setChecked(true);
                    if (!mCheckList.contains(i)) mCheckList.add(i);
                }
            }
        });
    }

    // ユーザ情報を更新してプロフィール画面へ遷移する
    private void gotoProfile() {

        UserRequester.UserData myUserData = UserRequester.getInstance().query(SaveData.getInstance().userId);
        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getDataList();

        for (int i = 0; i < mCheckList.size(); i++) {
            if (i < userList.size()) {
                UserRequester.UserData userData = userList.get(i);
                for (int j = 0; j < userData.likes.size(); j++) {
                    if (!myUserData.likes.contains(userData.likes.get(j))) {
                        myUserData.likes.add(userData.likes.get(j));
                    }
                }
            }
        }

        AccountRequester.update(myUserData, new AccountRequester.UpdateCallback() {
            @Override
            public void didReceive(boolean result) {
                if (result) {
                    // プロフィール画面へ
                    ProfileFragment fragment = new ProfileFragment();
                    FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
                } else {
                    AlertUtility.showAlert(getActivity(), "エラー", "通信に失敗しました", "OK", null);
                }
            }
        });
    }

    public static class LikeListAdapter extends ArrayAdapter<UserRequester.UserData> {

        LayoutInflater mInflater;
        Context mContext;

        public LikeListAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_like_list, parent, false);

            UserRequester.UserData userData = getItem(position);

            PicassoUtility.getPicassoRoundImage(mContext, userData.image, (ImageView)convertView.findViewById(R.id.faceImageView));
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(userData.name);

            StringBuffer likesString = new StringBuffer();
            for (int i = 0; i < userData.likes.size(); i++) {
                ItemRequester.ItemData itemData = ItemRequester.getInstance().query(userData.likes.get(i));
                if (itemData != null) {
                    if (i >= 1) {
                        likesString.append("、");
                    }
                    likesString.append(itemData.name);
                }
            }
            ((TextView)convertView.findViewById(R.id.likeTextView)).setText(likesString.toString());

            boolean isChecked = LikeListFragment.mCheckList.contains(position);
            ((CheckBox)convertView.findViewById(R.id.checkBox)).setChecked(isChecked);

            return convertView;
        }
    }
}
