package leapfrog_inc.icchi.Parts;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class AlertUtility {

    public static void showAlert(Activity activity, String title, String message, String buttonTitle, DialogInterface.OnClickListener listener){

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonTitle, listener)
                .show();
    }

    public static void showConfirm(Activity activity, String title, String message, String positiveButtonTitle, String negativeButtonTitle, DialogInterface.OnClickListener listener){

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitle, listener)
                .setNegativeButton(negativeButtonTitle, null)
                .show();
    }

    public static void showPicker(Activity activity, String title, String[] items, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, listener)
                .setNegativeButton("キャンセル", null)
                .show();
    }
}
