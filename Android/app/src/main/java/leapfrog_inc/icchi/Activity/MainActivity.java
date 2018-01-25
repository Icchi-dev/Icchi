package leapfrog_inc.icchi.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Login.LoginFragment;
import leapfrog_inc.icchi.Function.FacebookManager;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.R;

public class MainActivity extends AppCompatActivity {

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
                LoginFragment fragment = new LoginFragment();
                FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.vertical);
            }
        }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
