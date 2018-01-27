package leapfrog_inc.icchi.Fragment.Login;

import android.accounts.Account;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import leapfrog_inc.icchi.Activity.MainActivity;
import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.MyPage.MyPageFragment;
import leapfrog_inc.icchi.Function.FacebookManager;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.AccountRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.Parts.LoadingFragment;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class LoginFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_login, null);

        ((Button)view.findViewById(R.id.registerButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        FacebookManager.setCallback(new FacebookManager.FacebookManagerCallback() {
            @Override
            public void didLogin(boolean result, FacebookManager.FacebookUserData fbUserData) {

                if (!result) {
                    AlertUtility.showAlert(getActivity(), "エラー", "Facebook認証に失敗しました", "OK", null);
                    return;
                }

                ((MainActivity)getActivity()).startLoading();

                UserRequester.UserData userData = fbUserData.toUserData();
                AccountRequester.register("", "", userData.name, userData.image, userData.fbLink, new AccountRequester.RegisterCallback() {
                    @Override
                    public void didReceive(boolean result, String userId) {
                        ((MainActivity)getActivity()).stopLoading();

                        if (result) {
                            SaveData saveData = SaveData.getInstance();
                            saveData.userId = userId;
                            saveData.save();

                            stackMyPage();
                        } else {
                            AlertUtility.showAlert(getActivity(), "エラー", "ユーザー登録に失敗しました", "OK", null);
                        }
                    }
                });
            }
        });

        return view;
    }

    private void login() {

        View view = getView();
        if (view == null) return;

        String email = ((EditText)view.findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)view.findViewById(R.id.passwordEditText)).getText().toString();

        if ((email.length() == 0) || (password.length() == 0)) {
            AlertUtility.showAlert(getActivity(), "エラー", "入力エラー", "OK", null);
            return;
        }

        final LoadingFragment loading = LoadingFragment.start((MainActivity) getActivity());

        AccountRequester.login(email, password, new AccountRequester.LoginCallback() {
            @Override
            public void didReceive(boolean result, String userId) {
                loading.stop((MainActivity)getActivity());
                if (result) {
                    SaveData saveData = SaveData.getInstance();
                    saveData.userId = userId;
                    saveData.save();

                    stackMyPage();
                } else {
                    AlertUtility.showAlert(getActivity(), "エラー", "ログインに失敗しました", "OK", null);
                }
            }
        });
    }

    private void stackMyPage() {
        MyPageFragment fragment = new MyPageFragment();
        FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
    }
}
