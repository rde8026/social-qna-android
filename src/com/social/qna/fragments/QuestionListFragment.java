package com.social.qna.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
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
import com.social.qna.events.QuestionAnswerEvent;
import com.social.qna.fragments.callbacks.RemoveQuestionsCallback;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockListFragment;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/15/12
 * Time: 7:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuestionListFragment extends RoboLockListFragment implements RemoveQuestionsCallback {

    private static final String TAG = QuestionListFragment.class.getSimpleName();

    @Inject BusController busController;
    @Inject QuestionController questionController;
    @Inject LoginController loginController;

    @InjectView(R.id.loaderLayout) private RelativeLayout loaderLayout;
    @InjectView(R.id.listLayout) private LinearLayout listLayout;

    private ProgressDialog pd = null;

    private QuestionAdapter adapter;
    private ActionMode mMode;
    private List<QuestionModel> mModels;

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

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setItemsCanFocus(false);

        questionController.getQuestions(loginController.getUser(), loadQuestionsCallback);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        QuestionModel model = (QuestionModel)l.getAdapter().getItem(position);
        busController.getBus().post(new QuestionAnswerEvent(model));
    }

    @Override
    public void itemSelected(final SparseBooleanArray array) {
        if (mMode == null) {
            mMode = getSherlockActivity().startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.question_delete_cab_menu, menu);
                    mode.setTitle(countCheckedItems(array) + " " + getResources().getString(R.string.selected_cab_text));
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            pd.setMessage(getResources().getString(R.string.remove_question_loading_text));
                            pd.show();
                            questionController.removeQuestions(getSelectedItems(adapter.getSparseBooleanArray()), deleteMultiCallback);
                        return true;

                        default:
                        return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    if (mMode != null) {
                        mMode = null;
                    }
                    array.clear();
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            mMode.setTitle(countCheckedItems(array) + " " + getResources().getString(R.string.selected_cab_text));
        }
    }

    @Override
    public void itemDeSelected(SparseBooleanArray array) {
        if (mMode == null) {
            //should never happen...
        } else {
            if (countCheckedItems(array) > 0) {
                mMode.setTitle(countCheckedItems(array) + " " + getResources().getString(R.string.selected_cab_text));
            } else {
                array.clear();
                adapter.notifyDataSetChanged();
                mMode.finish();
                mMode = null;
            }
        }
    }

    private int countCheckedItems(SparseBooleanArray array) {
        int select = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(array.keyAt(i))) {
                select++;
            }
        }
        return select;
    }

    private ArrayList<String> getSelectedItems(SparseBooleanArray array) {
        ArrayList<String> items = new ArrayList<String>();

        for (int i = 0; i < array.size(); i++) {
            if (array.get(array.keyAt(i))) {
                QuestionModel m = mModels.get(array.keyAt(i));
                items.add(m.getId());
            }
        }

        return items;
    }

    private QuestionController.MultiRemoveCallback deleteMultiCallback = new QuestionController.MultiRemoveCallback() {
        @Override
        public void onSuccess() {
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    if (mMode != null) {
                        mMode.finish();
                    }
                    loaderLayout.setVisibility(View.VISIBLE);
                    listLayout.setVisibility(View.GONE);
                    questionController.getQuestions(loginController.getUser(), loadQuestionsCallback);
                }
            });
        }

        @Override
        public void onFailure(final String error) {
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    if (mMode != null) {
                        mMode.finish();
                    }

                    displayError(getResources().getString(R.string.error_msg_title), error, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            loaderLayout.setVisibility(View.VISIBLE);
                            listLayout.setVisibility(View.GONE);
                            questionController.getQuestions(loginController.getUser(), loadQuestionsCallback);
                        }
                    });

                }
            });
        }
    };

    private ListCallback<QuestionModel> loadQuestionsCallback = new ListCallback<QuestionModel>() {
        @Override
        public void onSuccess(List<QuestionModel> questionModels) {
            loaderLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            mModels = questionModels;

            adapter = new QuestionAdapter(getSherlockActivity(), R.layout.adatper_question, mModels, QuestionListFragment.this);
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
