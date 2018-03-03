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
import leapfrog_inc.icchi.Http.External.YahooNewsRequester;
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

        ArrayList<MyPostData> datas = createMyPostDatas();
        for (int i = 0; i < datas.size(); i++) {
            adapter.add(datas.get(i));
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyPostData myPostData = (MyPostData)adapterView.getItemAtPosition(i);
                if (myPostData.link.length() > 0) {
                    Uri uri = Uri.parse(myPostData.link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        if (datas.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.INVISIBLE);
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<MyPostData> createMyPostDatas() {

        ArrayList<MyPostData> myPostDatas = new ArrayList<MyPostData>();

        UserRequester.UserData myUserData = UserRequester.getInstance().query(SaveData.getInstance().userId);
        if (myUserData == null) {
            return myPostDatas;
        }

        // Yahooニュースの取得結果
        ArrayList<YahooNewsRequester.YahooNewsData> yahooList = YahooNewsRequester.getInstance().getDataList();
        for (int i = 0; i < 5; i++) {       // TODO: 表示条件は後で検討
            if (yahooList.size() > i) {
                YahooNewsRequester.YahooNewsData yahooData = yahooList.get(i);
                MyPostData myPostData = new MyPostData();
                myPostData.title = yahooData.title;
                myPostData.source = "Yahoo!ニュース";
                myPostData.sumbnail = "";
                myPostData.link = yahooData.link;
                myPostDatas.add(myPostData);
            }
        }

        // MyPost APIの結果
        ArrayList<PostRequester.PostData> postList = PostRequester.getInstance().getDataList();
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
                MyPostData myPostData = new MyPostData();
                myPostData.title = postData.title;
                myPostData.source = postData.source;
                myPostData.sumbnail = postData.sumbnail;
                myPostData.link = postData.link;
                myPostDatas.add(myPostData);
            }
        }

        return myPostDatas;
    }

    public static class MyPostData {
        String title;
        String source;
        String link;
        String sumbnail;
    }

    public static class MyPostAdapter extends ArrayAdapter<MyPostData> {

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

            MyPostData myPostData = getItem(position);

            if (myPostData.sumbnail.length() > 0) {
                PicassoUtility.getPicassoRoundImage(mContext, myPostData.sumbnail, (ImageView) convertView.findViewById(R.id.postImageView));
            } else {
                ((ImageView)convertView.findViewById(R.id.postImageView)).setVisibility(View.GONE);
            }
            ((TextView)convertView.findViewById(R.id.postTextView)).setText(myPostData.title);
            ((TextView)convertView.findViewById(R.id.sourceTextView)).setText(myPostData.source);

            return convertView;
        }
    }
}
