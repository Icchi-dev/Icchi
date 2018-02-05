package leapfrog_inc.icchi.Parts;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/27.
 */

public class PicassoUtility {

    public static void getPicassoRoundImage(Context context, String url, ImageView imageView) {
        if (url.length() == 0) {
            imageView.setImageResource(R.drawable.common_no_face);
            return;
        }
        Picasso.with(context)
                .load(url).networkPolicy(NetworkPolicy.NO_STORE)
                .noFade()
                .placeholder(R.drawable.common_no_face)
                .error(R.drawable.common_no_face)
                .transform(new CircleTransformation())
                .into(imageView);
    }
}
