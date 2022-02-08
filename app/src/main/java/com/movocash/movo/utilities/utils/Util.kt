package com.movocash.movo.utilities.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object Util {
    private val TAG = Util::class.java.name

    /**
     * disable it at release mode
     */
    val LOG_ENABLED = true


    /**
     * Deletes the fileOrDirectory passed and all its children.
     *
     * @param fileOrDirectory
     * @return
     */
    fun deleteRecursive(fileOrDirectory: File): Boolean {
        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles())
                deleteRecursive(child)

        val success = deleteFile(fileOrDirectory)

        if (LOG_ENABLED) {
            Log.v(TAG, fileOrDirectory.path + ".delete=" + success)
        }
        return success
    }


    /**
     * @param file
     * @return success
     */
    fun deleteFile(file: File): Boolean {
        // because the android 'open failed: EBUSY'
        // http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
        val to = File(file.absolutePath + System.currentTimeMillis())
        file.renameTo(to)
        return to.delete()
    }

    fun isValidEmail(emailAddress: String): Boolean {
        val emailRegEx: String
        val pattern: Pattern
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$"
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx)
        val matcher = pattern.matcher(emailAddress)
        return matcher.find()
    }


    /**
     * @param dir
     * @return the complete size of the directory or the file passed in Bytes.
     */
    fun dirSize(dir: File): Long {
        if (dir.exists()) {
            var result: Long = 0
            val fileList = dir.listFiles()
            for (i in fileList.indices) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory) {
                    result += dirSize(fileList[i])
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length()
                }
            }
            return result // return the file size
        }
        return 0
    }


    fun equals(objOne: Any?, objTwo: Any): Boolean {
        if (objOne === objTwo) {
            return true
        }

        return if (objOne == null) false else objOne == objTwo
    }

    /**
     * @param activity
     * @param title
     * @param message
     */
    fun showToastTitleMessage(activity: Activity, title: String, message: String) {
        val numberSpacesToAdd = (message.length - title.length) / 2 + 6
        var blankSpaces = ""
        blankSpaces = blankSpaces.substring(0, numberSpacesToAdd)
        val stringToShow = blankSpaces + title + "\n" + message
        Toast.makeText(activity.applicationContext, stringToShow, Toast.LENGTH_LONG).show()
    }


    /**
     * @param activity
     * @param stringToShow
     */
    fun showToastMessage(activity: Activity, stringToShow: String) {
        Toast.makeText(activity.applicationContext, stringToShow, Toast.LENGTH_LONG).show()
    }


    /**
     * Prints the string to the log verbose, avoiding the logcat maxlength
     * restriction. The string is not truncated.
     *
     * @param tag
     * @param string
     */
    fun logv(tag: String, string: String) {
        if (!LOG_ENABLED) {
            return
        }
        if (TextUtils.isEmpty(string) || string.length <= 4000) {
            Log.v(tag, string)
        } else {
            val chunkCount = string.length / 4000 // integer division
            for (i in 0..chunkCount) {
                val max = 4000 * (i + 1)
                if (max >= string.length) {
                    Log.v(tag, "chunk " + i + " of " + chunkCount + ":" + string.substring(4000 * i))
                } else {
                    Log.v(tag, "chunk " + i + " of " + chunkCount + ":" + string.substring(4000 * i, max))
                }
            }
        }
    }


    /**
     * Move file from one path to another path
     *
     * @param path_source
     * @param path_destination
     * @throws IOException
     */


    /**
     * Goes to the activity indicated with 'clazz', from the activity
     * 'fromActiviy' and erases navigation history.
     *
     * @param fromActivity
     * @param clazz
     */
    fun gotoActivityClearTop(fromActivity: Activity, clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        if (extras != null)
            intent.putExtras(extras)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.setClass(fromActivity, clazz)
        fromActivity.startActivity(intent)
        fromActivity.finish()
    }

    fun removeFragmentFromStack(context: Context, fragment: Fragment?) {
        if (fragment != null) {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val trans = manager.beginTransaction()
            trans.remove(fragment)
            trans.commit()
            manager.popBackStack()
        }
    }

    fun removeAllFragmentFromStack(context: Context) {
        run {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val count = manager.backStackEntryCount
            for (index in 0 until count)
                manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    /**
     * Goes to the activity indicated with 'clazz', from the activity
     * 'fromActiviy' . and puts bundle in intent if not null
     *
     * @param fromActivity
     * @param clazz
     * @param extras
     */
    fun gotoActivity(fromActivity: Activity, clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        if (extras != null)
            intent.putExtras(extras)
        intent.setClass(fromActivity, clazz)
        fromActivity.startActivity(intent)

    }

    /**
     * Restore the saved application state if needed.
     *
     * @param context
     * @param savedInstanceState
     */
    fun restoreSavedApplicationState(context: Context, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // Restore value of members from saved state
        }
    }

    fun calculateTimeDiff(date: String): Long {
        var milliSec: Long = 0
        var checkInTime = Date()
        // SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
        val sdf = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
            Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val msgDate = sdf.parse(date)
            milliSec = msgDate.time
            checkInTime = sdf.parse(sdf.format(checkInTime))
        } catch (e: ParseException) {
            Log.e("DATE Parse Exception", e.message!!)
        }

        val currentMilliSec = checkInTime.time
        val diff = currentMilliSec - milliSec

        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    /**
     * Show loading with shimmer effect like facebook
     * @param shimmerView
     */
    /*  public static void applyShimmerEffectOnView(ShimmerFrameLayout shimmerView){
        shimmerView.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
        shimmerView.setDuration(1000);
        shimmerView.startShimmerAnimation();
    }*/


    /**
     * Encapsulated behavior for dismissing Dialogs, because of several android problems
     * related.
     */
    fun dismissDialog(dialog: Dialog?) {
        if (dialog != null && dialog.isShowing) {
            try {
                dialog.dismiss()
            } catch (e: IllegalArgumentException) // even sometimes happens?: http://stackoverflow.com/questions/12533677/illegalargumentexception-when-dismissing-dialog-in-async-task
            {
                Log.i(TAG, "Error when attempting to dismiss dialog, it is an android problem.", e)
            }

        }
    }

    fun showkeyPad(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * Pass a view to method and it will hide the keyboard if opened.
     *
     * @param view
     */
    fun hidekeyPad(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,
            0)
    }

    fun getCurrentFragment(context: Context): Fragment? {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        var currentFragment: Fragment? = null
        if (fragmentManager.backStackEntryCount > 0) {
            val fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
            currentFragment = fragmentManager
                .findFragmentByTag(fragmentTag)
        }
        return currentFragment
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun getDeviceWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getDeviceHeight(context: Context): Int {
        var _heigthPixels = 0
        try {
            _heigthPixels = context.resources.displayMetrics.heightPixels
        } catch (e: Exception) {

        }

        return _heigthPixels
    }

    fun getKeyHash(context: Context) {
        /* HASH KEY CALCULATOR */

        try {
            val info = context.packageManager.getPackageInfo(
                "com.lizergroup.pricelizer", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sign = Base64
                    .encodeToString(md.digest(), Base64.DEFAULT)
                Log.e("MY KEY HASH:", sign)

            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("MY KEY HASH:", "NAME NOT FOUND")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("MY KEY HASH:", "NO SUCH ALGORITHM")
        }

    }

    fun hideAView(layout: View, afterMillisecs: Int) {
        Handler().postDelayed({
            layout.animate()
                .alpha(0.0f)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        layout.visibility = View.GONE
                    }
                })
        }, 4000)
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    fun hasSoftKeys(activity: Activity): Boolean {
        var d: Display = activity.windowManager.defaultDisplay
        var realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)
        var realHeight: Int = realDisplayMetrics.heightPixels
        var realWidth: Int = realDisplayMetrics.widthPixels
        var displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        var displayHeight = displayMetrics.heightPixels
        var displayWidth = displayMetrics.widthPixels
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0
    }
}