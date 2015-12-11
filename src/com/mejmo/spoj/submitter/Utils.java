package com.mejmo.spoj.submitter;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Created by MFO on 11.12.2015.
 */
public class Utils {

    public static void showError(String mesg) {
        Notifications.Bus.notify(new Notification("SPOJ Submitter", "SPOJ Submitter", mesg, NotificationType.ERROR));
    }

    public static void showWarning(String mesg) {
        Notifications.Bus.notify(new Notification("SPOJ Submitter", "SPOJ Submitter", mesg, NotificationType.WARNING));
    }

    public static void showInformation(String mesg) {
        Notifications.Bus.notify(new Notification("SPOJ Submitter", "SPOJ Submitter", mesg, NotificationType.INFORMATION));
    }


}
