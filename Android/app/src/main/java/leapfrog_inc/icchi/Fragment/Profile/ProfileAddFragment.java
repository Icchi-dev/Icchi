package leapfrog_inc.icchi.Fragment.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

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

        resetContents((LinearLayout)view.findViewById(R.id.contentsLayout), "");

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
                    resetContents((LinearLayout)view.findViewById(R.id.contentsLayout), text);
                }
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER) {
                    createItem();
                    return true;
                }
                return false;
            }
        });

        ((Button)view.findViewById(R.id.addButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });

        if (!mIsLike) {
            ((TextView)view.findViewById(R.id.isLikeTextView)).setTextColor(Color.rgb(120, 120, 255));
            ((TextView)view.findViewById(R.id.isLikeTextView)).setText("嫌い");
            ((Button)view.findViewById(R.id.addButton)).setTextColor(ContextCompat.getColor(getActivity(), R.color.hateBlue));
            ((LinearLayout) view.findViewById(R.id.contentsBaseLayout)).setBackgroundResource(R.layout.shape_profile_add_base_hate);
            ((LinearLayout)view.findViewById(R.id.searchLayout)).setBackgroundResource(R.layout.shape_profile_add_search_hate);
        }

        return view;
    }

    private void resetContents(LinearLayout baseLayout, String text) {

        ArrayList<ItemRequester.ItemData> shuffledItemDatas = ItemRequester.getInstance().getDataList();
        Collections.shuffle(shuffledItemDatas);
        ArrayList<ItemRequester.ItemData> itemDatas = new ArrayList<ItemRequester.ItemData>();

        // 検索文字列あり
        if (text.length() > 0) {
            // 検索にヒットしたアイテムを全て表示
            for (int i = 0; i < shuffledItemDatas.size(); i++) {
                ItemRequester.ItemData itemData = shuffledItemDatas.get(i);
                if ((itemData.name.indexOf(text) != -1) || (itemData.kana.indexOf(text) != -1)) {
                    itemDatas.add(itemData);
                }
            }
        } else {
            // ランダムに10個表示
            for (int i = 0; i < 10; i++) {
                if (shuffledItemDatas.size() > i) {
                    itemDatas.add(shuffledItemDatas.get(i));
                }
            }
        }

        LinearLayout lineLayout = null;
        int currentOffset = 0;

        for (int i = 0; i < itemDatas.size(); i++) {
            if ((lineLayout == null) || (currentOffset == 0)) {
                lineLayout = new LinearLayout(getActivity());
                lineLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
            final ItemRequester.ItemData itemData = itemDatas.get(i);
            String itemName = itemData.name;
            TextView textView = new TextView(getActivity());
            textView.setText(itemName);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setTextColor(Color.WHITE);
            if (mIsLike) {
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
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyToProfile(itemData.itemId);
                }
            });

            lineLayout.addView(textView);

            currentOffset += (width + margin);

            if (i != itemDatas.size() - 1) {
                String nextItemName = itemDatas.get(i + 1).name;
                int nextTextViewWidth = getTextViewWidth(nextItemName) + 2 * margin;
                int contentsWidth = (int)(CommonUtility.getWindowSize(getActivity()).x - CommonUtility.getDeviceDensity(getActivity()) * 100);
                if (currentOffset + nextTextViewWidth > contentsWidth) {
                    baseLayout.addView(lineLayout);
                    currentOffset = 0;
                }
            } else {
                baseLayout.addView(lineLayout);
                currentOffset = 0;
            }
        }
    }

    private int getTextViewWidth(String text) {
        return (int)(CommonUtility.getDeviceDensity(getActivity()) * 14 * text.length()) + 40;
    }

    private void createItem() {

        View view = getView();
        if (view == null) return;

        EditText editText = (EditText)view.findViewById(R.id.searchEditText);
        if (editText == null) return;

        String itemName = editText.getText().toString();

        if (itemName.length() > 10) {
            AlertUtility.showAlert(getActivity(), "エラー", "10文字以内で入力してください", "OK", null);
            return;
        }

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
        boolean result = profileFragment.addItemId(itemId, mIsLike);

        if (result) {
            FragmentController.getInstance().pop(FragmentController.AnimationType.horizontal);
        } else {
            String message = "";
            if (mIsLike) {
                message = "\"嫌い\"に登録されたコンテンツです";
            } else {
                message = "\"好き\"に登録されたコンテンツです";
            }
            AlertUtility.showAlert(getActivity(), "エラー", message, "OK", null);
        }
    }
}
