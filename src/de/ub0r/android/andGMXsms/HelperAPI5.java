/*
 * Copyright (C) 2009 Felix Bechstein
 * 
 * This file is part of WebSMS.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */
package de.ub0r.android.andGMXsms;

import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Helper class to set/unset background for api5 systems.
 * 
 * @author flx
 */
public class HelperAPI5 {

	/** Sort Order. */
	private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Phone.STARRED
			+ " DESC, "
			+ ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED
			+ " DESC, "
			+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
			+ " ASC, " + ContactsContract.CommonDataKinds.Phone.TYPE;

	/** Cursor's projection. */
	private static final String[] PROJECTION = { //  
	BaseColumns._ID, // 0
			ContactsContract.Data.DISPLAY_NAME, // 1
			ContactsContract.CommonDataKinds.Phone.NUMBER, // 2
			ContactsContract.CommonDataKinds.Phone.TYPE // 3
	};

	/**
	 * Run Query to update data.
	 * 
	 * @param mContentResolver
	 *            a Content Resolver
	 * @param constraint
	 *            filter string
	 * @return cursor
	 */
	final Cursor runQueryOnBackgroundThread(
			final ContentResolver mContentResolver,
			final CharSequence constraint) {
		String where = null;

		if (constraint != null) {
			String filter = DatabaseUtils.sqlEscapeString('%' + constraint
					.toString() + '%');

			StringBuilder s = new StringBuilder();
			s.append("(" + ContactsContract.Data.DISPLAY_NAME + " LIKE ");
			s.append(filter);
			s.append(") OR (" + ContactsContract.CommonDataKinds.Phone.DATA1
					+ " LIKE ");
			s.append(filter);
			s.append(")");

			where = s.toString();
		}
		return mContentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION,
				where, null, SORT_ORDER);
	}

	/**
	 * Run Service in foreground.
	 * 
	 * @see Service.startForeground()
	 * @param service
	 *            the Service
	 * @param id
	 *            notification id
	 * @param notification
	 *            notification
	 */
	final void startForeground(final Service service, final int id,
			final Notification notification) {
		service.startForeground(id, notification);
	}

	/**
	 * Run Service in background.
	 * 
	 * @see Service.stopForeground()
	 * @param service
	 *            Service
	 * @param removeNotification
	 *            remove notification?
	 */
	final void stopForeground(final Service service,
			final boolean removeNotification) {
		service.stopForeground(removeNotification);
	}
}