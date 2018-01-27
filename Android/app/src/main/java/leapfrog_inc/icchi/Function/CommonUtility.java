package leapfrog_inc.icchi.Function;

import android.app.Activity;
import android.util.Size;
import android.view.View;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class CommonUtility {

    public static float getDeviceDensity(Activity activity){
        return activity.getResources().getDisplayMetrics().density;
    }

    public static Size getMeasuredSize(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredWidth();
        return new Size(width, height);
    }
}
