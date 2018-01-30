package leapfrog_inc.icchi.Fragment.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.MyPost.MyPostFragment;
import leapfrog_inc.icchi.Function.CommonUtility;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Http.Requester.PostRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.PicassoUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileAddFragment extends BaseFragment {

    private boolean mIsLike = true;

    public void setIsLike(boolean isLike) {
        mIsLike = isLike;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_add, null);

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
            }
        });

        resetListView((ListView)view.findViewById(R.id.listView), "");

        ((EditText)view.findViewById(R.id.searchEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                View view = getView();
                if (view == null) return;

                ListView listView = (ListView)view.findViewById(R.id.listView);
                String text = editable.toString();
                if ((listView != null) && (text != null)) {
                    resetListView(listView, text);
                }
            }
        });

        return view;
    }

    private void resetListView(ListView listView, String text) {

        ProfileAddAdapter adapter = new ProfileAddAdapter(getActivity());

        ArrayList<ItemRequester.ItemData> itemDatas = ItemRequester.getInstance().getDataList();

        if (text.length() == 0) {
            for (int i = 0; i < 10; i++) {
                if (itemDatas.size() > i) {
                    adapter.add(itemDatas.get(i).name);
                }
            }
        } else {
            for (int i = 0; i < itemDatas.size(); i++) {
                if (itemDatas.get(i).name.indexOf(text) != -1) {
                    adapter.add(itemDatas.get(i).name);
                }
            }
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemName = (String)adapterView.getItemAtPosition(i);
                String itemId = ItemRequester.getInstance().queryId(itemName);
                if (itemId != null) {
                    ProfileFragment profileFragment = (ProfileFragment) FragmentController.getInstance().getPreviousFragment();
                    profileFragment.addItemId(itemId, mIsLike);

                    FragmentController.getInstance().pop(FragmentController.AnimationType.horizontal);
                }
            }
        });
    }

    public static class ProfileAddAdapter extends ArrayAdapter<String> {

        LayoutInflater mInflater;
        Context mContext;

        public ProfileAddAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_profileadd, parent, false);

            String text = getItem(position);
            ((TextView)convertView.findViewById(R.id.contentTextView)).setText(text);


            return convertView;
        }
    }
}
