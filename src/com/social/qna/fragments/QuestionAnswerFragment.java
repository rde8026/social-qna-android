package com.social.qna.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;
import com.kinvey.util.ScalarCallback;
import com.social.qna.R;
import com.social.qna.controllers.AnswerController;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.model.AnswerModel;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockFragment;
import com.social.qna.util.ReverseLocation;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 5:00 PM
 */
public class QuestionAnswerFragment extends RoboLockFragment implements LocationListener {

    private static final String TAG = QuestionAnswerFragment.class.getSimpleName();

    private static final String QUESTION_MODEL_EXTRA = "QUESTION_MODEL";
    private QuestionModel mModel;
    private double lat = 0.0, lng = 0.0;

    @Inject ReverseLocation reverseLocation;
    @Inject AnswerController answerController;
    @Inject LoginController loginController;
    @Inject BusController busController;

    @Inject private LocationManager locationManager;

    @InjectView(R.id.askedBy) private TextView askedBy;
    @InjectView(R.id.askedOn) private TextView askedOn;
    @InjectView(R.id.askedFrom) private TextView askedFrom;
    @InjectView(R.id.askedFromLabel) private TextView askedFromLabel;
    @InjectView(R.id.answerText) private EditText answerText;

    private ProgressDialog pd;

    public static QuestionAnswerFragment newInstance(QuestionModel questionModel) {
        QuestionAnswerFragment fragment = new QuestionAnswerFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_MODEL_EXTRA, questionModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        busController.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().unregister(this);
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    private ReverseLocation.ReverseLookupCallback locationLookupCallback = new ReverseLocation.ReverseLookupCallback() {
        @Override
        public void onSuccess(final String address) {
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    askedFrom.setText(address);
                }
            });
        }

        @Override
        public void onFailure(String error) {
            Log.e(TAG, "Error: " + error);
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    askedFromLabel.setVisibility(View.GONE);
                    askedFrom.setVisibility(View.GONE);
                }
            });
        }
    };

    private ScalarCallback<AnswerModel> answerCallback = new ScalarCallback<AnswerModel>() {
        @Override
        public void onSuccess(AnswerModel answerModel) {
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    busController.getBus().post(new AnswerCompleteEvent());
                }
            });
        }

        @Override
        public void onFailure(final Throwable throwable) {
            super.onFailure(throwable);
            getSherlockActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    displayError(getResources().getString(R.string.error_msg_title), throwable.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            });
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.answer_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.answer:
                pd.setMessage(getResources().getString(R.string.submitting_answer_text));
                if (askedFrom.getTag() != null) {
                    Location location = (Location) askedFrom.getTag();
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
                pd.show();
                answerController.answerQuestion(mModel.getId(), answerText.getText().toString(), loginController.getUser(), lat, lng, answerCallback);
            return true;
            default:
            return false;
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject location = mModel.getLocation();
                    double lat = location.getDouble("latitude");
                    double  lng = location.getDouble("longitude");
                    reverseLocation.reverseLocationLookup(lat, lng, locationLookupCallback);
                } catch (JSONException je) {
                    Log.e(TAG, "JSONException: " + je.getMessage());
                }

                askedBy.setText(mModel.getCreatedBy());
                askedOn.setText(mModel.getCreated());

                askedFrom.setTag(location);
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}