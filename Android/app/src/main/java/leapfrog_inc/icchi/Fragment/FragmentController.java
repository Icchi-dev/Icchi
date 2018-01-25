package leapfrog_inc.icchi.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class FragmentController {

    private static FragmentController singleton = new FragmentController();
    private FragmentController(){}
    public static FragmentController getInstance(){
        return singleton;
    }

    private FragmentManager mFragmentManager = null;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int mContainerId = 0;

    public void initialize(FragmentManager fm, int containerId) {
        mFragmentManager = fm;
        mContainerId = containerId;
    }

    public void stack(BaseFragment fragment, AnimationType animationType) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(animationType == AnimationType.horizontal){
            ft.setCustomAnimations(R.anim.stack_from_right, R.anim.close_for_left);
        } else if (animationType == AnimationType.vertical) {
            ft.setCustomAnimations(R.anim.stack_from_bottom, R.anim.close_for_top);
        }
        for(int i=0;i<mFragmentList.size();i++){
            ft.remove(mFragmentList.get(i));
        }
        ft.add(mContainerId, fragment);
        ft.commitAllowingStateLoss();
        mFragmentList.add(fragment);
    }

    public enum AnimationType {
        vertical,
        horizontal,
        none
    }
}
