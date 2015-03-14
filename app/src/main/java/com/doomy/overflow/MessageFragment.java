/**
 * Copyright (C) 2013 Damien Chazoule
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doomy.overflow;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MessageFragment extends Fragment {

    // Declare your view and variables
    private FloatingActionButton mFAB;
	private Vibrator mVibe;
    private SeekBar mSeekBar;
    private EditText mMessage;
    private TextView mTextView;
    private int mProgressChanged;
    private DataBase mDB;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate
     * the fragment (e.g. upon screen orientation changes).
     */
    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDB = new DataBase(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView =inflater.inflate(R.layout.fragment_message,container,false);

        mProgressChanged = 1;
		mVibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mMessage = (EditText) mView.findViewById(R.id.editTextMessage);
        mSeekBar = (SeekBar) getActivity().findViewById(R.id.mySeekBar);
        mTextView = (TextView) getActivity().findViewById(R.id.textViewSeekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                mProgressChanged = progress+1;
                mTextView.setText(mProgressChanged+"");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mTextView.setText(mProgressChanged+"");
            }
        });

        mFAB = (FloatingActionButton) mView.findViewById(R.id.sendMessage);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mExtra = getActivity().getIntent().getExtras();

                String mFullName = mExtra.getString("fullname");
                int mColorize = mExtra.getInt("colorize");

                String myMessage = mMessage.getText().toString().trim();
                int mSeekBar = mProgressChanged;

                if (!myMessage.equals("")) {
					for (int i = 1; i <= mSeekBar; i++) {
                        sendMessage(myMessage);
						mVibe.vibrate(50);
                    }
                    Message mMessage = new Message(mFullName, mColorize, myMessage, "(" + mSeekBar + ")");
                    mDB.addOne(mMessage);
                    showNotification(mFullName);
                    MainActivity.syncRows();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mView;
    }

    private void sendMessage(String mMessage) {
        Bundle mExtra = getActivity().getIntent().getExtras();

        String mPhoneNumber = mExtra.getString("phonenumber");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mPhoneNumber, null, mMessage, null, null);
            ContactActivity.getInstance().finish();
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String mText) {

        Intent mIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), 0, mIntent, 0);

        Notification mNotification = new Notification.Builder(getActivity())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.go) + " " + mText + " (" + mProgressChanged + ")")
                .setSmallIcon(R.drawable.ic_overflow)
                .setContentIntent(mPendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager mNotificationManager = (NotificationManager)
                getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mNotification);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
