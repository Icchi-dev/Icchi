package leapfrog_inc.icchi.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.icchi.Fragment.BaseFragment;
import leapfrog_inc.icchi.Fragment.FragmentController;
import leapfrog_inc.icchi.Function.SaveData;
import leapfrog_inc.icchi.Parts.AlertUtility;
import leapfrog_inc.icchi.R;

/**
 * Created by Leapfrog-Software on 2018/01/26.
 */

public class TutorialFragment extends BaseFragment {

    private int pageIndex = 0;
    String[] questions = {"趣味は？", "好きな\nアーティストは？", "好きな食べ物は？", "好きな言葉は？", "集めているものは？", "好きなスポーツは？", "好きなお笑い芸人は？", "今欲しいものは？", "特技は？", "好きな漫画・アニメは？", "怖いものは？", "嫌いな食べ物は？", "嫌いな言葉は？", "嫌いな時間や状況は？", "苦手なことは？", "嫌いな教科は？"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_tutorial, null);

        ((Button)view.findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkText()) {
                    toNext();
                } else {
                    AlertUtility.showAlert(getActivity(), "エラー", "入力がありません", "OK", null);
                }
            }
        });

        ((Button)view.findViewById(R.id.skipButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNext();
            }
        });

        setQuestion(view);

        return view;
    }

    private boolean checkText() {

        View view = getView();
        if (view == null){
            return true;
        }
        String text = ((EditText)view.findViewById(R.id.answerEditText)).getText().toString();
        return text.length() > 0;
    }

    private void toNext() {

        View view = getView();
        if (view == null) return;

        pageIndex += 1;

        if (pageIndex < questions.length) {
            final LinearLayout alphaLayout = (LinearLayout)view.findViewById(R.id.alphaLayout);
            animateAlpha(alphaLayout, false, new AnimationDidEndCallback() {
                @Override
                public void didFinish() {
                    View view = getView();
                    if (view == null) return;

                    setQuestion(view);
                    ((EditText)view.findViewById(R.id.answerEditText)).setText("");

                    animateAlpha(alphaLayout, true, new AnimationDidEndCallback() {
                        @Override
                        public void didFinish() {}
                    });
                }
            });
        } else {
            SaveData saveData = SaveData.getInstance();
            saveData.isInitialized = true;
            saveData.save();

            LikeListFragment fragment = new LikeListFragment();
            FragmentController.getInstance().stack(fragment, FragmentController.AnimationType.horizontal);
        }
    }

    private void setQuestion(View view) {

        TextView typeTextView = (TextView)view.findViewById(R.id.typeTextView);
        TextView countTextView = (TextView)view.findViewById(R.id.countTextView);
        TextView questionTextView = (TextView)view.findViewById(R.id.questionTextView);

        if (pageIndex < 10) {
            typeTextView.setText("『好き』を登録するための質問");
            countTextView.setText(String.format("%d / 10", pageIndex + 1));
        } else {
            typeTextView.setText("『嫌い』を登録するための質問");
            countTextView.setText(String.format("%d / 6", pageIndex - 9));
        }
        questionTextView.setText(questions[pageIndex]);
    }

    private void animateAlpha(LinearLayout layout, boolean visible, final AnimationDidEndCallback callback) {

        AlphaAnimation animation;
        if (visible) {
            animation = new AlphaAnimation(0, 1);
        } else {
            animation = new AlphaAnimation(1, 0);
        }
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                callback.didFinish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        layout.startAnimation(animation);
    }

    interface AnimationDidEndCallback {
        void didFinish();
    }
}
