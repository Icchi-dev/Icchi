package leapfrog_inc.icchi.Fragment.Profile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.AccountRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile, null);

        setProfile(view);

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMenu();
            }
        });

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

        ((Button)view.findViewById(R.id.addLikeButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAddFragment fragment = new ProfileAddFragment();
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.addHateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAddFragment fragment = new ProfileAddFragment();
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
            }
        });

        return view;
    }

    private void setProfile(View view) {

        UserRequester.UserData userData = UserRequester.getInstance().query(SaveData.getInstance().userId);
        if (userData == null) {
            return;
        }

        ((EditText)view.findViewById(R.id.nameEditText)).setText(userData.name);

        if (userData.age != null) {
            Button ageButton = (Button)view.findViewById(R.id.ageButton);
            ageButton.setText(userData.age.display());
            ageButton.setTextColor(Color.BLACK);
        }
        if (userData.gender != null) {
            Button genderButton = (Button)view.findViewById(R.id.genderButton);
            genderButton.setText(userData.gender.display());
            genderButton.setTextColor(Color.BLACK);
        }

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
    }

    private void onClickAge() {

        final String[] items = {
                UserRequester.AgeType.u20.display(),
                UserRequester.AgeType.s20.display(),
                UserRequester.AgeType.s30.display(),
                UserRequester.AgeType.s40.display(),
                UserRequester.AgeType.s50.display(),
                UserRequester.AgeType.o60.display()
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
                UserRequester.GenderType.male.display(),
                UserRequester.GenderType.female.display()
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

    private void onClickMenu() {




        FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
    }
}
