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
import android.widget.TextView;

import leapfrog_inc.icchi.Activity.MainActivity;
import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.AccountRequester;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileFragment extends BaseFragment {

    private UserRequester.UserData tmpUserData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile, null);

        UserRequester.UserData userData = UserRequester.getInstance().query(SaveData.getInstance().userId);
        if (userData != null) {
            tmpUserData = userData;
            setProfile(view, userData);
        }

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
                fragment.setIsLike(true);
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.addHateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAddFragment fragment = new ProfileAddFragment();
                fragment.setIsLike(false);
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
            }
        });

        return view;
    }

    private void setProfile(View view, UserRequester.UserData userData) {

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
        for (int i = 0; i < userData.likes.size(); i++) {
            final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_profile_contents, null);

            final ItemRequester.ItemData itemData = ItemRequester.getInstance().query(userData.likes.get(i));
            if (itemData != null) {
                ((TextView) layout.findViewById(R.id.itemTextView)).setText(itemData.name);
            }
            ((Button)layout.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeContentsLayout.removeView(layout);
                    tmpUserData.likes.remove(itemData.itemId);
                }
            });
            likeContentsLayout.addView(layout);
        }

        final LinearLayout hateContentsLayout = (LinearLayout)view.findViewById(R.id.hateContentsBaseLayout);
        for (int i = 0; i < userData.hates.size(); i++) {
            final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_profile_contents, null);
            ((LinearLayout)layout.findViewById(R.id.baseLayout)).setBackgroundResource(R.layout.shape_profile_hatecontents);

            final ItemRequester.ItemData itemData = ItemRequester.getInstance().query(userData.hates.get(i));
            if (itemData != null) {
                ((TextView) layout.findViewById(R.id.itemTextView)).setText(itemData.name);
            }

            ((Button)layout.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hateContentsLayout.removeView(layout);
                    tmpUserData.hates.remove(itemData.itemId);
                }
            });
            hateContentsLayout.addView(layout);
        }
    }

    private void onClickAge() {

        String[] items = {
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

                UserRequester.AgeType[] ages = {
                        UserRequester.AgeType.u20,
                        UserRequester.AgeType.s20,
                        UserRequester.AgeType.s30,
                        UserRequester.AgeType.s40,
                        UserRequester.AgeType.s50,
                        UserRequester.AgeType.o60
                };
                UserRequester.AgeType age = ages[i];

                Button ageButton = (Button)view.findViewById(R.id.ageButton);
                ageButton.setText(age.display());
                ageButton.setTextColor(Color.BLACK);

                tmpUserData.age = age;
            }
        });
    }

    private void onClickGender() {

        String[] items = {
                UserRequester.GenderType.male.display(),
                UserRequester.GenderType.female.display()
        };
        AlertUtility.showPicker(getActivity(), "性別", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View view = getView();
                if (view == null) return;

                UserRequester.GenderType[] genders = {
                        UserRequester.GenderType.male,
                        UserRequester.GenderType.female
                };
                UserRequester.GenderType gender = genders[i];

                String item = gender.display();
                Button genderButton = (Button)view.findViewById(R.id.genderButton);
                genderButton.setText(item);
                genderButton.setTextColor(Color.BLACK);

                tmpUserData.gender = gender;
            }
        });
    }

    private void onClickMenu() {

        View view = getView();
        if (view == null) return;

        String name = ((EditText)view.findViewById(R.id.nameEditText)).getText().toString();
        tmpUserData.name = name;

        ((MainActivity)getActivity()).startLoading();

        AccountRequester.update(tmpUserData, new AccountRequester.UpdateCallback() {
            @Override
            public void didReceive(boolean result) {
                if (result) {
                    UserRequester.getInstance().fetch(new UserRequester.UserRequesterCallback() {
                        @Override
                        public void didReceiveData(boolean result) {

                            ((MainActivity)getActivity()).stopLoading();

                            if (result) {
                                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
                            } else {
                                showError();
                            }
                        }
                    });
                } else {
                    ((MainActivity)getActivity()).stopLoading();
                    showError();
                }
            }
        });
    }

    private void showError() {
        AlertUtility.showAlert(getActivity(), "エラー", "通信に失敗しました", "OK", null);
    }

    public void addItemId(String itemId, boolean isLike) {

        if (isLike) {
            if (!tmpUserData.likes.contains(itemId)) {
                tmpUserData.likes.add(itemId);
            }
        } else {
            if (!tmpUserData.hates.contains(itemId)) {
                tmpUserData.hates.add(itemId);
            }
        }

        View view = getView();
        if (view == null) return;

        setProfile(view, tmpUserData);
    }
}
