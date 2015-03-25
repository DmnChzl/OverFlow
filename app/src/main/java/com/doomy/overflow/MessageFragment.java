/**
 * Copyright (C) 2013 Damien Chazoule
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

import com.doomy.library.DiscreteSeekBar;

public class MessageFragment extends Fragment {

    // Declare your view and variables
    private FloatingActionButton mFAB;
	private Vibrator mVibe;
    // private SeekBar mSeekBar;
    private DiscreteSeekBar mDiscreteSeekBar;
    private EditText mMessage;
    // private TextView mTextView;
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
        // mTextView = (TextView) getActivity().findViewById(R.id.textViewSeekBar);
        /*
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
        */

        mDiscreteSeekBar = (DiscreteSeekBar) getActivity().findViewById(R.id.myDiscreteSeekBar);
        mDiscreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {
                mProgressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                if (mProgressChanged == 1) {
                    mMessage.setHint(getString(R.string.write));
                } else {
                    mMessage.setHint(getString(R.string.write) + "  x" + mProgressChanged);
                }
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
