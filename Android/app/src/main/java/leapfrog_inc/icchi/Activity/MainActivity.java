package leapfrog_inc.icchi.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
