package com.social.qna.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.inject.Inject;
import com.social.qna.R;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockFragment;
import com.social.qna.util.ReverseLocation;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 5:00 PM
 */
public class QuestionAnswerFragment extends RoboLockFragment {

    private static final String TAG = QuestionAnswerFragment.class.getSimpleName();

    private static final String QUESTION_MODEL_EXTRA = "QUESTION_MODEL";
    private QuestionModel mModel;

    @Inject ReverseLocation reverseLocation;

    @InjectView(R.id.askedBy) private TextView askedBy;
    @InjectView(R.id.askedOn) private TextView askedOn;
    @InjectView(R.id.askedFrom) private TextView askedFrom;

    private ProgressDialog pd;

    public static QuestionAnswerFragment newInstance(QuestionModel questionModel) {
        QuestionAnswerFragment fragment = new QuestionAnswerFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_MODEL_EXTRA, questionModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_answer, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        pd = new ProgressDialog(getSherlockActivity());
        pd.setMessage(getResources().getString(R.string.locating_user_text));

        mModel = (QuestionModel) getArguments().getSerializable(QUESTION_MODEL_EXTRA);

        pd.show();
        reverseLocation.reverseLocationLookup(Double.valueOf(mModel.getLatitude()), Double.valueOf(mModel.getLongitude()), locationLookupCallback);

        askedBy.setText(mModel.getCreatedBy());
        askedOn.setText(mModel.getCreated());

    }

    private ReverseLocation.ReverseLookupCallback locationLookupCallback = new ReverseLocation.ReverseLookupCallback() {
        @Override
        public void onSuccess(final String address) {
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    askedOn.setText(address);
                }
            });
        }

        @Override
        public void onFailure(String error) {
            Log.e(TAG, error);
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    askedFrom.setVisibility(View.GONE);
                }
            });
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.answer_menu, menu);
    }
}
