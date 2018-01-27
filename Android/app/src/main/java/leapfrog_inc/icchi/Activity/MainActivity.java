package leapfrog_inc.icchi.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Login.LoginFragment;
import leapfrog_inc.icchi.Function.FacebookManager;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Http.Requester.ItemRequester;
import leapfrog_inc.icchi.Http.Requester.PostRequester;
import leapfrog_inc.icchi.Http.Requester.UserRequester;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.Parts.LoadingFragment;
import leapfrog_inc.icchi.R;

public class MainActivity extends AppCompatActivity {

    enum FetchResult {
        ok,
        error,
        progress
    }

    private FetchResult fetchUserResult = FetchResult.progress;
    private FetchResult fetchItemResult = FetchResult.progress;
    private FetchResult fetchPostResult = FetchResult.progress;
    private LoadingFragment mLoadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SaveData.getInstance().initialize(getApplicationContext());
        FacebookManager.initialize(this);
        FragmentController.getInstance().initialize(getSupportFragmentManager(), R.id.container);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch();
            }
        }, 1000);
    }

    private void fetch() {

        mLoadingFragment = LoadingFragment.start(this);

        UserRequester.getInstance().fetch(new UserRequester.UserRequesterCallback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) fetchUserResult = FetchResult.ok;
                else        fetchUserResult = FetchResult.error;
                checkResult();
            }
        });

        ItemRequester.getInstance().fetch(new ItemRequester.ItemRequesterCallback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) fetchItemResult = FetchResult.ok;
                else        fetchItemResult = FetchResult.error;
                checkResult();
            }
        });

        PostRequester.getInstance().fetch(new PostRequester.PostRequesterCallback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) fetchPostResult = FetchResult.ok;
                else        fetchPostResult = FetchResult.error;
                checkResult();
            }
        });
    }

    private void checkResult() {

        if ((fetchUserResult == FetchResult.progress)
                || (fetchItemResult == FetchResult.progress)
                || (fetchPostResult == FetchResult.progress)) {
            return;
        }

        if (mLoadingFragment != null) {
            mLoadingFragment.stop(this);
            mLoadingFragment = null;
        }

        if ((fetchUserResult == FetchResult.error)
                || (fetchItemResult == FetchResult.error)
                || (fetchPostResult == FetchResult.error)) {
            AlertUtility.showAlert(this, "エラー", "通信に失敗しました", "リトライ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fetch();
                }
            });
            return;
        }

        LoginFragment fragment = new LoginFragment();
        FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.vertical);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
