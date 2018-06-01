package leapfrog_inc.icchi.Fragment.Profile;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.common.Common;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import leapfrog_inc.icchi.Activity.MainActivity;
import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Match.MatchFragment;
import leapfrog_inc.icchi.Function.CommonUtility;
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

    private ArrayList<String> mSelectedLikeItemNames = new ArrayList<String>();
    private ArrayList<String> mSelectedHateItemNames = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_like_list, null);

        initContents(view);

        ((Button)view.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapOk();
            }
        });

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
            }
        });

        ((ImageButton)view.findViewById(R.id.reloadButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
            }
        });

        return view;
    }

    private void reload() {
        initContents(getView());
    }

    private void onTapOk() {

        UserRequester.UserData myUserData = UserRequester.getInstance().query(SaveData.getInstance().userId);

        for (int i = 0; i < mSelectedLikeItemNames.size(); i++) {
            String itemId = ItemRequester.getInstance().queryId(mSelectedLikeItemNames.get(i));
            if (itemId != null) {
                myUserData.likes.add(itemId);
            }
        }
        for (int i = 0; i < mSelectedHateItemNames.size(); i++) {
            String itemId = ItemRequester.getInstance().queryId(mSelectedHateItemNames.get(i));
            if (itemId != null) {
                myUserData.hates.add(itemId);
            }
        }

        ((MainActivity)getActivity()).startLoading();

        AccountRequester.update(myUserData, new AccountRequester.UpdateCallback() {
            @Override
            public void didReceive(boolean result) {
                ((MainActivity)getActivity()).stopLoading();

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

    private int getContentsWidth() {
        return CommonUtility.getWindowSize(getActivity()).x - (int)(CommonUtility.getDeviceDensity(getActivity()) * (float)50);
    }

    public static int getTextViewWidth(Activity activity, String text) {
        return (int)(CommonUtility.getDeviceDensity(activity) * 14 * text.length()) + 40;
    }

    private void initContents(View view) {

        addItems((LinearLayout)view.findViewById (R.id.likeBaseLayout), (ListView) view.findViewById(R.id.likeListView), true);
        addItems((LinearLayout)view.findViewById (R.id.hateBaseLayout), (ListView)view.findViewById(R.id.hateListView), false);
        
        ((HorizontalScrollView)view.findViewById(R.id.horizontalScrollView)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int targetX = 0;
                    int contentsWidth = getContentsWidth();
                    if (view.getScrollX() > contentsWidth / 2) {
                        targetX = contentsWidth;
                    }
                    final int fTargetX = targetX;
                    final HorizontalScrollView scrollView = (HorizontalScrollView)view;
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(fTargetX, 0);
                        }
                    });
                }
                return false;
            }
        });
    }

    private void addItems(LinearLayout baseLayout, ListView listView, final boolean isLike) {

        ViewGroup.LayoutParams baseParams = baseLayout.getLayoutParams();
        baseParams.width = getContentsWidth();
        baseLayout.setLayoutParams(baseParams);

        LikeListAdapter adapter = new LikeListAdapter(getActivity(), isLike, new LikeListAdapter.LikeListAdapterCallback() {
            @Override
            public void didTapItem(TextView textView, boolean isLike) {
                didTapItemName(textView, isLike);
            }
        });

        ArrayList<String> itemIdList = new ArrayList<String>();
        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getDataList();
        for (int i = 0; i < userList.size(); i++) {
            UserRequester.UserData userData = userList.get(i);
            ArrayList<String> userItemNos = null;
            if (isLike) {
                userItemNos = userData.likes;
            } else {
                userItemNos = userData.hates;
            }
            for (int j = 0; j < userItemNos.size(); j++) {
                String itemId = userItemNos.get(j);
                if (!itemIdList.contains(itemId)) {
                    ItemRequester.ItemData itemData = ItemRequester.getInstance().query(itemId);
                    if (itemData != null) {
                        itemIdList.add(itemData.itemId);
                    }
                }
            }
        }
        Collections.shuffle(itemIdList);

        // 選択済みのアイテムを先頭にもってくる
        ArrayList<String> selectedNames = (isLike) ? mSelectedLikeItemNames : mSelectedHateItemNames;
        for (int i = 0; i < selectedNames.size(); i++) {
            String itemId = ItemRequester.getInstance().queryId(selectedNames.get(i));
            if (itemId != null) {
                itemIdList.remove(itemId);
                itemIdList.add(0, itemId);
            }
        }

        ArrayList<LikeListAdapterData> adapterDatas = new ArrayList<LikeListAdapterData>();
        int currentOffset = 0;

        for (int i = 0; i < itemIdList.size(); i++) {
            ItemRequester.ItemData itemData = ItemRequester.getInstance().query(itemIdList.get(i));
            String itemName = itemData.name;
            int width = getTextViewWidth(getActivity(), itemName);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(width, (int)(CommonUtility.getDeviceDensity(getActivity()) * 30)));
            int margin = (int)(CommonUtility.getDeviceDensity(getActivity()) * 8);
            params.topMargin = margin;
            params.leftMargin = margin;

            LikeListAdapterData adapterData = new LikeListAdapterData();
            adapterData.itemName = itemName;
            adapterData.isSelected = selectedNames.contains(itemName);
            adapterDatas.add(adapterData);

            currentOffset += (width + margin);

            if (i != itemIdList.size() - 1) {
                String nextItemName = ItemRequester.getInstance().query(itemIdList.get(i + 1)).name;
                int nextTextViewWidth = getTextViewWidth(getActivity(), nextItemName) + 2 * margin;
                if (currentOffset + nextTextViewWidth > getContentsWidth()) {
                    adapter.add(adapterDatas);
                    currentOffset = 0;
                    adapterDatas = new ArrayList<LikeListAdapterData>();
                }
            } else {
                adapter.add(adapterDatas);
                currentOffset = 0;
                adapterDatas = new ArrayList<LikeListAdapterData>();
            }
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void didTapItemName(TextView textView, boolean isLike) {

        String itemName = textView.getText().toString();

        if (isLike) {
            if (mSelectedLikeItemNames.contains(itemName)) {
                mSelectedLikeItemNames.remove(itemName);
                textView.setBackgroundResource(R.layout.shape_profile_likecontents);
            } else {
                mSelectedLikeItemNames.add(itemName);
                textView.setBackgroundResource(R.layout.shape_profile_selected_contents);
            }
        } else {
            if (mSelectedHateItemNames.contains(itemName)) {
                mSelectedHateItemNames.remove(itemName);
                textView.setBackgroundResource(R.layout.shape_profile_hatecontents);
            } else {
                mSelectedHateItemNames.add(itemName);
                textView.setBackgroundResource(R.layout.shape_profile_selected_contents);
            }
        }
    }

    static class LikeListAdapterData {
        String itemName;
        boolean isSelected;
    }

    public static class LikeListAdapter extends ArrayAdapter<ArrayList<LikeListAdapterData>> {

        LayoutInflater mInflater;
        Context mContext;
        LikeListAdapterCallback mCallback;
        boolean mIsLike;

        public LikeListAdapter(Context context, boolean isLike, LikeListAdapterCallback callback){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mIsLike = isLike;
            mCallback = callback;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_like_list, parent, false);

            ArrayList<LikeListAdapterData> adapterDatas = getItem(position);

            for (int i = 0; i < adapterDatas.size(); i++) {
                LikeListAdapterData adapterData = adapterDatas.get(i);
                final TextView textView = new TextView(mContext);
                textView.setText(adapterData.itemName);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                textView.setTextColor(Color.WHITE);

                if (adapterData.isSelected) {
                    textView.setBackgroundResource(R.layout.shape_profile_selected_contents);
                } else {
                    if (mIsLike) {
                        textView.setBackgroundResource(R.layout.shape_profile_likecontents);
                    } else {
                        textView.setBackgroundResource(R.layout.shape_profile_hatecontents);
                    }
                }
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                int width = LikeListFragment.getTextViewWidth((Activity) mContext, adapterData.itemName);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(width, (int)(CommonUtility.getDeviceDensity((Activity)mContext) * 30)));
                int margin = (int)(CommonUtility.getDeviceDensity((Activity) mContext) * 8);
                params.topMargin = margin;
                params.leftMargin = margin;
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER_VERTICAL);

                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.didTapItem(textView, mIsLike);
                    }
                });

                ((LinearLayout)convertView).addView(textView);
            }

            return convertView;
        }

        interface LikeListAdapterCallback {
            void didTapItem(TextView textView, boolean isLike);
        }
    }
}
