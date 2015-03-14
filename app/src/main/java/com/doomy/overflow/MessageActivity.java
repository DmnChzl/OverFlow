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

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class MessageActivity extends Activity {

    // Declare your view and variables
    private ActionBar mActionBar;
    private String mRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle mExtra = getIntent().getExtras();

        mRecipient = getString(R.string.recipient) + " " + mExtra.getString("fullname");

        mActionBar = getActionBar();
        mActionBar.setTitle(mRecipient);

        MessageFragment mFragment = new MessageFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
    }
}