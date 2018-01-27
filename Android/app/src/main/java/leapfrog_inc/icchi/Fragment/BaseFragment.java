package leapfrog_inc.icchi.Fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }
    }
}
