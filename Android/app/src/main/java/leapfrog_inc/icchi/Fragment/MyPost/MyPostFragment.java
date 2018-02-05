package leapfrog_inc.icchi.Fragment.MyPost;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Match.MatchFragment;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.PostRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class MyPostFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypost, null);

        setListView((ListView)view.findViewById(R.id.listView), (TextView)view.findViewById(R.id.noDataTextView));

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
            }
        });

        return view;
    }

    private void setListView(ListView listView, TextView noDataView) {

        final MyPostAdapter adapter = new MyPostAdapter(getActivity());

        ArrayList<PostRequester.PostData> postList = PostRequester.getInstance().getDataList();
        UserRequester.UserData myUserData = UserRequester.getInstance().query(SaveData.getInstance().userId);
        if (myUserData == null) {
            return;
        }

        int count = 0;

        for (int i = 0; i < postList.size(); i++) {
            PostRequester.PostData postData = postList.get(i);
            boolean relates = false;
            if (postData.relates.contains("*")) {
                relates = true;
            } else {
                for (int j = 0; j < postData.relates.size(); j++) {
                    if (myUserData.likes.contains(postData.relates.get(j))) {
                        relates = true;
                        break;
                    }
                }
            }
            if (relates) {
                adapter.add(postData);
                count++;
            }
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PostRequester.PostData postData = (PostRequester.PostData)adapterView.getItemAtPosition(i);
                if (postData.link.length() > 0) {
                    Uri uri = Uri.parse(postData.link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        if (count > 0) {
            listView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.INVISIBLE);
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    public static class MyPostAdapter extends ArrayAdapter<PostRequester.PostData> {

        LayoutInflater mInflater;
        Context mContext;

        public MyPostAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_mypost, parent, false);

            PostRequester.PostData postData = getItem(position);

            PicassoUtility.getPicassoRoundImage(mContext, postData.sumbnail, (ImageView)convertView.findViewById(R.id.postImageView));

            ((TextView)convertView.findViewById(R.id.postTextView)).setText(postData.title);
            ((TextView)convertView.findViewById(R.id.sourceTextView)).setText(postData.source);

            return convertView;
        }
    }
}
