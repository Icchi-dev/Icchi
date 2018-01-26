package leapfrog_inc.icchi.Fragment.Profile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class ProfileFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile, null);

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

        return view;
    }

    private void onClickAge() {

        final String[] items = {
                "20歳未満",
                "20代",
                "30代",
                "40代",
                "50代",
                "60歳以上"
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
                "男性",
                "女性"
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
}
