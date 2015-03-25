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