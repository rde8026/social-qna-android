package com.social.qna.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.inject.Inject;
import com.kinvey.util.ListCallback;
import com.social.qna.R;
import com.social.qna.adapter.QuestionAdapter;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.controllers.QuestionController;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockFragment;
import com.social.qna.robolock.fragment.RoboLockListFragment;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 3:38 PM
 */
public class AllQuestionsFragment extends RoboLockListFragment {

    private static final String TAG = AllQuestionsFragment.class.getSimpleName();

    @Inject BusController busController;
    @Inject LoginController loginController;
    @Inject QuestionController questionController;

    @InjectView(R.id.loaderLayout) private RelativeLayout loaderLayout;
    @InjectView(R.id.listLayout) private LinearLayout listLayout;

    private QuestionAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        getSherlockActivity().getSupportActionBar().setTitle(getResources().getString(R.string.show_all_questions_label));
        busController.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        questionController.getAllQuestions(allQuestionsCallback);
    }

    private ListCallback<QuestionModel> allQuestionsCallback = new ListCallback<QuestionModel>() {
        @Override
        public void onSuccess(List<QuestionModel> questionModels) {

            loaderLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);

            adapter = new QuestionAdapter(getSherlockActivity(), R.layout.adatper_question, questionModels);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable throwable) {
            super.onFailure(throwable);
            displayError(getResources().getString(R.string.error_msg_title), throwable.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    };

}
