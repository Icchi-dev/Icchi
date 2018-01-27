package leapfrog_inc.icchi.Fragment.Match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class MatchFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_match, null);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        MatchAdapter adapter = new MatchAdapter(getActivity());

        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getDataList();
        for (int i = 0; i < userList.size(); i++) {
            adapter.add(userList.get(i));
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserRequester.UserData userData = (UserRequester.UserData)adapterView.getItemAtPosition(i);
                if (userData.fbLink.length() > 0) {
                    Uri uri = Uri.parse(userData.fbLink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
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

    public static class MatchAdapter extends ArrayAdapter<UserRequester.UserData> {

        LayoutInflater mInflater;
        Context mContext;

        public MatchAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_match, parent, false);

            UserRequester.UserData userData = getItem(position);

            PicassoUtility.getPicassoRoundImage(mContext, userData.image, (ImageView)convertView.findViewById(R.id.faceImageView));
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(userData.name);

            int match = UserRequester.getInstance().queryMatch(userData.userId);
            ((TextView)convertView.findViewById(R.id.matchTextView)).setText(String.valueOf(match));

            return convertView;
        }
    }
}
