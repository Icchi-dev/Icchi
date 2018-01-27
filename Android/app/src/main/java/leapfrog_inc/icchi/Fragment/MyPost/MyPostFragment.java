package leapfrog_inc.icchi.Fragment.MyPost;

import android.content.Context;
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
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class MyPostFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypost, null);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        MyPostAdapter adapter = new MyPostAdapter(getActivity());
        adapter.add("test1");
        adapter.add("test2");
        adapter.add("test3");
        adapter.add("test4");
        adapter.add("test5");
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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

    public static class MyPostAdapter extends ArrayAdapter<String> {

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

            String name = getItem(position);

            PicassoUtility.getPicassoRoundImage(mContext, "", (ImageView)convertView.findViewById(R.id.postImageView));

            ((TextView)convertView.findViewById(R.id.postTextView)).setText(name);

            return convertView;
        }
    }
}
