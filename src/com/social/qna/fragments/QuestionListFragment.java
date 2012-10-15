package com.social.qna.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;
import com.kinvey.util.ListCallback;
import com.kinvey.util.ScalarCallback;
import com.social.qna.R;
import com.social.qna.adapter.QuestionAdapter;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.controllers.QuestionController;
import com.social.qna.events.AllQuestionsEvent;
import com.social.qna.events.LogoutEvent;
import com.social.qna.events.NewQuestionEvent;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockListFragment;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/15/12
 * Time: 7:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuestionListFragment extends RoboLockListFragment {

    private static final String TAG = QuestionListFragment.class.getSimpleName();

    @Inject BusController busController;
    @Inject QuestionController questionController;
    @Inject LoginController loginController;

    @InjectView(R.id.loaderLayout) private RelativeLayout loaderLayout;
    @InjectView(R.id.listLayout) private LinearLayout listLayout;

    private ProgressDialog pd = null;

    private QuestionAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        getSherlockActivity().getSupportActionBar().setTitle(getResources().getString(R.string.show_question_label));
        busController.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().register(this);
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
        pd = new ProgressDialog(getSherlockActivity());
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        getListView().setOnItemLongClickListener(deleteQuestion);

        questionController.getQuestions(loginController.getUser(), loadQuestionsCallback);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private ListCallback<QuestionModel> loadQuestionsCallback = new ListCallback<QuestionModel>() {
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

    private AdapterView.OnItemLongClickListener deleteQuestion = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            final QuestionModel model = adapter.getItem(i);
            displayConfirm(getResources().getString(R.string.confirm_msg_title), getResources().getString(R.string.remove_question_message), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    pd.setMessage(getResources().getString(R.string.remove_question_loading_text));
                    pd.show();
                    questionController.removeQuestion(model.getId(), deleteCallback);
                }
            },
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            return true;
        }
    };

    private ScalarCallback<Void> deleteCallback = new ScalarCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            pd.dismiss();
            loaderLayout.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            questionController.getQuestions(loginController.getUser(), loadQuestionsCallback);
        }

        @Override
        public void onFailure(Throwable throwable) {
            super.onFailure(throwable);
            Log.e(TAG, "", throwable);
            displayError(getResources().getString(R.string.error_msg_title), throwable.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_question:
                busController.getBus().post(new NewQuestionEvent());
            return true;
            case R.id.show_all_questions:
                busController.getBus().post(new AllQuestionsEvent());
            return true;
            case R.id.logout:
                busController.getBus().post(new LogoutEvent());
            return true;
            default:
            return false;
        }
    }
}
