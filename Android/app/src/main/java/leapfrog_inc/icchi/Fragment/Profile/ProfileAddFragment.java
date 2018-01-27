package leapfrog_inc.icchi.Fragment.Profile;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Function.CommonUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileAddFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_add, null);

        ((ImageButton)view.findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentController.getInstance().popToMyPage(FragmentController.AnimationType.horizontal);
            }
        });

        LinearLayout contentsLayout = (LinearLayout)view.findViewById(R.id.contentsLayout);
        for (int i = 0; i < 8; i++) {
            TextView textView = new TextView(getActivity());
            textView.setText(String.format("test-%d", i));
            textView.setTextColor(Color.BLACK);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setBackgroundColor(Color.rgb(255, 100, 100));
            int padding = 20 * (int)CommonUtility.getDeviceDensity(getActivity());
            textView.setPadding(padding, 0, padding, 0);
            contentsLayout.addView(textView);
        }

        return view;
    }


}
