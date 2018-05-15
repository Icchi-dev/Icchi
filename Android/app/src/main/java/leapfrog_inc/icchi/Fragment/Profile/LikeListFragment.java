package leapfrog_inc.icchi.Fragment.Profile;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

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

    static ArrayList<Integer> mCheckList = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_like_list, null);

        initContents(view);

        ((Button)view.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // プロフィール画面へ
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

    private int getContentsWidth() {
        return CommonUtility.getWindowSize(getActivity()).x - (int)(CommonUtility.getDeviceDensity(getActivity()) * (float)50);
    }

    private int getTextViewWidth(String text) {
        return (int)(CommonUtility.getDeviceDensity(getActivity()) * 14 * text.length()) + 40;
    }

    private void initContents(View view) {

        addItems((LinearLayout)view.findViewById(R.id.likeBaseLayout), true);
        addItems((LinearLayout)view.findViewById(R.id.hateBaseLayout), false);
        
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

    private void addItems(LinearLayout baseLayout, boolean isLike) {

        ViewGroup.LayoutParams likeParams = baseLayout.getLayoutParams();
        likeParams.width = getContentsWidth();
        baseLayout.setLayoutParams(likeParams);

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

        LinearLayout lineLayout = null;
        int currentOffset = 0;

        for (int i = 0; i < itemIdList.size(); i++) {
            if ((lineLayout == null) || (currentOffset == 0)) {
                lineLayout = new LinearLayout(getActivity());
                lineLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
            ItemRequester.ItemData itemData = ItemRequester.getInstance().query(itemIdList.get(i));
            String itemName = itemData.name;
            TextView textView = new TextView(getActivity());
            textView.setText(itemName);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setTextColor(Color.WHITE);
            if (isLike) {
                textView.setBackgroundResource(R.layout.shape_profile_likecontents);
            } else {
                textView.setBackgroundResource(R.layout.shape_profile_hatecontents);
            }
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            int width = getTextViewWidth(itemName);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(width, (int)(CommonUtility.getDeviceDensity(getActivity()) * 30)));
            int margin = (int)(CommonUtility.getDeviceDensity(getActivity()) * 8);
            params.topMargin = margin;
            params.leftMargin = margin;
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            lineLayout.addView(textView);

            currentOffset += (width + margin);

            if (i != itemIdList.size() - 1) {
                String nextItemName = ItemRequester.getInstance().query(itemIdList.get(i + 1)).name;
                int nextTextViewWidth = getTextViewWidth(nextItemName) + 2 * margin;
                if (currentOffset + nextTextViewWidth > getContentsWidth()) {
                    baseLayout.addView(lineLayout);
                    currentOffset = 0;
                }
            } else {
                baseLayout.addView(lineLayout);
                currentOffset = 0;
            }
        }
    }
}
