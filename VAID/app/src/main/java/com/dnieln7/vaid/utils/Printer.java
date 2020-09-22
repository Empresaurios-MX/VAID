package com.dnieln7.vaid.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to print messages to the UI
 *
 * @author dnieln7
 */
public class Printer {

    private Printer() {
    }

    /**
     * Displays a {@link Toast} for short period of time.
     *
     * @param context Activity context.
     * @param message Message to be displayed.
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a {@link Snackbar} for short period of time.
     *
     * @param view    The view in witch to show the message.
     * @param message Message to be displayed.
     */
    public static void snackBar(View view, String message) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public static void logError(String className, Throwable error) {
        Logger.getLogger(className).log(Level.SEVERE, "There was an error", error);
    }
}
