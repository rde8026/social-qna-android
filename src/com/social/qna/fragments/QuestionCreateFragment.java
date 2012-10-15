package com.social.qna.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;
import com.kinvey.util.ScalarCallback;
import com.social.qna.R;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.controllers.QuestionController;
import com.social.qna.events.LocationEvent;
import com.social.qna.events.NewQuestionEvent;
import com.social.qna.events.QuestionCreatedEvent;
import com.social.qna.model.QuestionModel;
import com.social.qna.robolock.fragment.RoboLockFragment;
import com.social.qna.util.KeyboardUtil;
import com.social.qna.util.ReverseLocation;
import com.squareup.otto.Subscribe;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 11:06 AM
 */
public class QuestionCreateFragment extends RoboLockFragment implements LocationListener {

    private static final String TAG = QuestionCreateFragment.class.getSimpleName();


    @Inject BusController busController;
    @Inject ReverseLocation reverseLocation;
    @Inject LoginController loginController;
    @Inject QuestionController questionController;
    @InjectView(R.id.edtQuestionText) private EditText questionText;
    @InjectView(R.id.currentLocation) private EditText currentLocation;

    private ProgressDialog pd = null;
    @Inject private LocationManager locationManager;

    @Override
    public void onResume() {
        super.onResume();
        busController.getBus().register(this);
        getSherlockActivity().getSupportActionBar().setTitle(getResources().getString(R.string.create_question_label));
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ask_question, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        pd = new ProgressDialog(getSherlockActivity());
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(getResources().getString(R.string.locating_user_text));
        pd.show();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_new_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_question:
                KeyboardUtil.dismissKeyboard(questionText);
                pd.setMessage(getResources().getString(R.string.saving_question_text));
                pd.show();

                Location location = (Location) currentLocation.getTag();

                questionController.createQuestion(questionText.getText().toString(), location.getLatitude(), location.getLongitude(), loginController.getUser(), createQuestionCallback);
            return true;
            default:
            return false;
        }
    }

    private ScalarCallback<QuestionModel> createQuestionCallback = new ScalarCallback<QuestionModel>() {
        @Override
        public void onSuccess(QuestionModel questionModel) {
            pd.dismiss();
            Toast.makeText(getSherlockActivity(), "Question Created", Toast.LENGTH_SHORT).show();
            busController.getBus().post(new QuestionCreatedEvent());
        }

        @Override
        public void onFailure(Throwable throwable) {
            super.onFailure(throwable);
            Log.e(TAG, "Error", throwable);
            Toast.makeText(getSherlockActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Subscribe
    public void locationEvent(final LocationEvent locationEvent) {
        getSherlockActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locationManager.removeUpdates(QuestionCreateFragment.this);
                pd.dismiss();
                KeyboardUtil.requestKeyboard(questionText);
                if (locationEvent.getCode() > 0) {
                    currentLocation.setText("No Location");
                } else {
                    currentLocation.setText(locationEvent.getAddress());
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation.setTag(location);
        reverseLocation.reverseLocationLookup(location.getLatitude(), location.getLongitude(), new ReverseLocation.ReverseLookupCallback() {
            @Override
            public void onSuccess(String address) {
                busController.getBus().post(new LocationEvent(address));
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
                busController.getBus().post(new LocationEvent(error, 1));
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