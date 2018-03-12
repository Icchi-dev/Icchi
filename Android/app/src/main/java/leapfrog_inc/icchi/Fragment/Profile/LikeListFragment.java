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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Match.MatchFragment;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/03/09.
 */

public class LikeListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_like_list, null);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        LikeListAdapter adapter = new LikeListAdapter(getActivity());

        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getDataList();
        for (int i = userList.size() - 1; i >= 0; i--) {
            adapter.add(userList.get(i));
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        ((Button)view.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
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

            return convertView;
        }
    }
}
