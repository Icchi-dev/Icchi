package leapfrog_inc.icchi.Fragment.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

import leapfrog_inc.icchi.Activity.MainActivity;
import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Function.CommonUtility;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
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

        EditText searchEditText = (EditText)view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
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

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER) {
                    createItem((EditText)view);
                    return true;
                }
                return false;
            }
        });

        if (!mIsLike) {
            ((TextView)view.findViewById(R.id.isLikeTextView)).setTextColor(Color.rgb(120, 120, 255));
            ((TextView)view.findViewById(R.id.isLikeTextView)).setText("嫌い");
            ((ImageView)view.findViewById(R.id.grassImageView)).setImageResource(R.drawable.profileadd_grass_hate);
            ((LinearLayout) view.findViewById(R.id.contentsBaseLayout)).setBackgroundResource(R.layout.shape_profile_add_base_hate);
            ((LinearLayout)view.findViewById(R.id.searchLayout)).setBackgroundResource(R.layout.shape_profile_add_search_hate);
        }

        return view;
    }

    private void resetListView(ListView listView, String text) {

        ProfileAddAdapter adapter = new ProfileAddAdapter(getActivity(), mIsLike);

        ArrayList<ItemRequester.ItemData> itemDatas = ItemRequester.getInstance().getDataList();

        if (text.length() == 0) {
            for (int i = 0; i < 10; i++) {
                if (itemDatas.size() > i) {
                    adapter.add(itemDatas.get(i).name);
                }
            }
        } else {
            for (int i = 0; i < itemDatas.size(); i++) {
                ItemRequester.ItemData itemData = itemDatas.get(i);
                if ((itemData.name.indexOf(text) != -1) || (itemData.kana.indexOf(text) != -1)) {
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
                    notifyToProfile(itemId);
                }
            }
        });
    }

    private void createItem(EditText editText) {

        String itemName = editText.getText().toString();
        if (itemName.length() > 0) {

            ((MainActivity)getActivity()).startLoading();

            ItemRequester.creteItem(itemName, new ItemRequester.CreateItemCallback() {
                @Override
                public void didCreateItem(boolean result, String itemId) {

                    final String fItemId = itemId;

                    if (result) {
                        ItemRequester.getInstance().fetch(new ItemRequester.ItemRequesterCallback() {
                            @Override
                            public void didReceiveData(boolean result) {
                                ((MainActivity)getActivity()).stopLoading();
                                if (result) {
                                    notifyToProfile(fItemId);
                                } else {
                                    AlertUtility.showAlert(getActivity(), "エラー", "通信に失敗しました", "OK", null);
                                }
                            }
                        });
                    } else {
                        ((MainActivity)getActivity()).stopLoading();
                        AlertUtility.showAlert(getActivity(), "エラー", "通信に失敗しました", "OK", null);
                    }
                }
            });
        }
    }

    private void notifyToProfile(String itemId) {

        ProfileFragment profileFragment = (ProfileFragment) FragmentController.getInstance().getPreviousFragment();
        profileFragment.addItemId(itemId, mIsLike);

        FragmentController.getInstance().pop(FragmentController.AnimationType.horizontal);
    }

    public static class ProfileAddAdapter extends ArrayAdapter<String> {

        LayoutInflater mInflater;
        Context mContext;
        boolean mIsLike;

        public ProfileAddAdapter(Context context, boolean isLike){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mIsLike = isLike;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_profileadd, parent, false);

            if (!mIsLike) {
                ((LinearLayout)convertView.findViewById(R.id.baseLayout)).setBackgroundResource(R.layout.shape_profileadd_content_hate);
            }

            String text = getItem(position);
            ((TextView)convertView.findViewById(R.id.contentTextView)).setText(text);

            return convertView;
        }
    }
}
