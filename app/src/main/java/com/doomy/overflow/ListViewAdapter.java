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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<Contact> {

    // Declare your view and variables
    private Activity mActivity;
    private List<Contact> mItems;
    private int mRow;
    private Contact mContact;

    public ListViewAdapter(Activity activity, int row, List<Contact> items) {
        super(activity, row, items);

        this.mActivity = activity;
        this.mRow = row;
        this.mItems = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        ViewHolder mHolder;
        if (mView == null) {
            LayoutInflater mInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(mRow, null);

            mHolder = new ViewHolder();
            mView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) mView.getTag();
        }

        if ((mItems == null) || ((position + 1) > mItems.size()))
            return mView;

        mContact = mItems.get(position);

        mHolder.myFullName = (TextView) mView.findViewById(R.id.textViewName);
        mHolder.myPhoneNumber = (TextView) mView.findViewById(R.id.textViewNumb);
        mHolder.myImageContact = (ImageView) mView.findViewById(R.id.imageViewLogo);

        if (mHolder.myFullName != null && null != mContact.getFullName()
                && mContact.getFullName().trim().length() > 0) {
            mHolder.myFullName.setText(Html.fromHtml(mContact.getFullName()));
        }
        if (mHolder.myPhoneNumber != null && null != mContact.getPhoneNumber()
                && mContact.getPhoneNumber().trim().length() > 0) {
            mHolder.myPhoneNumber.setText(Html.fromHtml(mContact.getPhoneNumber()));
        }
        mHolder.myFullName.setTextColor(getContext().getResources().getColor(mContact.getColorize()));
        mHolder.myImageContact.setColorFilter(getContext().getResources().getColor(mContact.getColorize()));
        return mView;
    }

    public class ViewHolder {
        public ImageView myImageContact;
        public TextView myFullName, myPhoneNumber;
    }
}
