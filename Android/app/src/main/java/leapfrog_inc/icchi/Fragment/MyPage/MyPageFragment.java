package leapfrog_inc.icchi.Fragment.MyPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Fragment.Profile.ProfileFragment;
import leapfrog_inc.icchi.Fragment.Profile.TutorialFragment;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class MyPageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage, null);

        ((Button)view.findViewById(R.id.profileButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SaveData.getInstance().isInitialized) {
                    ProfileFragment fragment = new ProfileFragment();
                    FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
                } else {
                    TutorialFragment fragment = new TutorialFragment();
                    FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
                }
            }
        });

        ((Button)view.findViewById(R.id.matchButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((Button)view.findViewById(R.id.myPostButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }
}
