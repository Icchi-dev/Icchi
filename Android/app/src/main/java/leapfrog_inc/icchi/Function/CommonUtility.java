package leapfrog_inc.icchi.Function;

import android.app.Activity;
import android.graphics.Point;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class CommonUtility {

    public static Point getWindowSize(Activity activity) {

        WindowManager wm = (WindowManager)activity.getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);
        return size;
    }

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
