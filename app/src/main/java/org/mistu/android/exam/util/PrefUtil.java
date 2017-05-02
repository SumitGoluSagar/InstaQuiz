/*
 * Copyright (C) 2016 The Android Open Source Project
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
package org.mistu.android.exam.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public final class PrefUtil {

    private static final String KEY_FIRST_LAUNCH = "isFirstLaunch";

    public static String getTeacherName() {
        return "Teacher";
    }

    /*synchronized public static void setCredentials(Context context, Player player) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FIRST_LAUNCH, "false");
        editor.putString(KEY_USER_NAME, player.getName());
        editor.putString(KEY_STD, player.getStd());
        editor.putString(KEY_TEAM, player.getTeam());
        editor.apply();
    }*/

    synchronized public static void setFirstLaunch(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FIRST_LAUNCH, "false");
        editor.apply();
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.contains(KEY_FIRST_LAUNCH)){
            return true;
        }
        return false;
    }

    /*synchronized public static void setPushId(Context context, String id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.contains(KEY_PUSH_ID)) {
            Log.i("PREF_UTILS ", "Push Id already set");
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PUSH_ID, id);
        editor.apply();
    }

    public static String getPushId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_PUSH_ID, null);
    }

    public static void removePushId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_PUSH_ID);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_USER_NAME, null);
    }

    public static String getStd(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_STD, null);
    }

    public static String getTeam(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_TEAM, null);
    }*/

   /* public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassesOfWater = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        return glassesOfWater;
    }

    synchronized public static void incrementWaterCount(Context context) {
        int waterCount = PreferenceUtilities.getWaterCount(context);
        PreferenceUtilities.setWaterCount(context, ++waterCount);
    }

    synchronized public static void incrementChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders);
        editor.apply();
    }

    public static int getChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        return chargingReminders;
    }*/
}