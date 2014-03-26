package com.rampgreen.acceldatacollector.service;

import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;

public class NotificationUpdater {
    public static void turnOnForeground(Service srv,int notifID,NotificationManager mNotificationManager,Notification notif) {
        try {
            Method m = Service.class.getMethod("startForeground", new Class[] {int.class, Notification.class});
            m.invoke(srv, notifID, notif);
        } catch (Exception e) {
//            srv.setForeground(true);
            mNotificationManager.notify(notifID, notif);
        }
    } 

    public static void turnOffForeground(Service srv,int notifID,NotificationManager mNotificationManager) {
        try {
            Method m = Service.class.getMethod("stopForeground", new Class[] {boolean.class});
            m.invoke(srv, true);
        } catch (Exception e) {
//            srv.setForeground(false);
            mNotificationManager.cancel(notifID);
        }
    }
}