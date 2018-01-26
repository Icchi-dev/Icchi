package leapfrog_inc.icchi.Fragment.Profile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile, null);

        ((Button)view.findViewById(R.id.ageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAge();
            }
        });

        ((Button)view.findViewById(R.id.genderButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGender();
            }
        });

        final LinearLayout likeContentsLayout = (LinearLayout)view.findViewById(R.id.likeContentsBaseLayout);
        for (int i = 0; i < 10; i++) {
            final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_profile_contents, null);
            ((Button)layout.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeContentsLayout.removeView(layout);
                }
            });
            likeContentsLayout.addView(layout);
        }

        final LinearLayout hateContentsLayout = (LinearLayout)view.findViewById(R.id.hateContentsBaseLayout);
        for (int i = 0; i < 10; i++) {
            final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_profile_contents, null);
            ((LinearLayout)layout.findViewById(R.id.baseLayout)).setBackgroundResource(R.layout.shape_profile_hatecontents);
            ((Button)layout.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hateContentsLayout.removeView(layout);
                }
            });
            hateContentsLayout.addView(layout);
        }

        return view;
    }

    private void onClickAge() {

        final String[] items = {
                "20歳未満",
                "20代",
                "30代",
                "40代",
                "50代",
                "60歳以上"
        };
        AlertUtility.showPicker(getActivity(), "年齢", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View view = getView();
                if (view == null) return;

                String item = items[i];
                Button ageButton = (Button)view.findViewById(R.id.ageButton);
                ageButton.setText(item);
                ageButton.setTextColor(Color.BLACK);
            }
        });
    }

    private void onClickGender() {

        final String[] items = {
                "男性",
                "女性"
        };
        AlertUtility.showPicker(getActivity(), "性別", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View view = getView();
                if (view == null) return;

                String item = items[i];
                Button genderButton = (Button)view.findViewById(R.id.genderButton);
                genderButton.setText(item);
                genderButton.setTextColor(Color.BLACK);
            }
        });
    }
}
