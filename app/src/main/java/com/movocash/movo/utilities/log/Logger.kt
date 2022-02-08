package com.movocash.movo.utilities.log

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object Logger {

    private val LOG_TAG = "App Logs"
    private val LOG_TAG_RESPONSE = "RESPONSE"
    private val LOG_TAG_IM = "IM.SN"
    private val LOG_TAG_GIM = "IM.GR"
    val LOGS_FILE_NAME = "HooleyOrganizersLogs.txt"

    //INFO: Setting from Splash
    var showORMLogs = false
    var showLogs = true
    var exportDatabase = true
    var showIMLogs = false
    var showSmackLogs = false
    var showIMGroupLogs = false
    var showFileLogs = false


    fun i(msg: String) {
        if (showLogs)
            Log.i(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)
    }

    fun o(msg: String) {
        if (showORMLogs)
            Log.i(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)
    }

    fun x(msg: String) {
        if (showIMLogs)
            Log.i(LOG_TAG_IM, msg)
        if (showFileLogs)
            writeInFile(msg, true)

    }

    fun xgr(msg: String) {
        if (showIMGroupLogs)
            Log.i(LOG_TAG_GIM, msg)
        if (showFileLogs)
            writeInFile(msg, true)

    }

    fun d(msg: String) {
        if (showLogs)
            Log.d(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)

    }


    fun oe(msg: String) {
        if (showLogs)
            Log.e(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)
    }


    fun e(msg: String) {
        if (showLogs)
            Log.e(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)

    }

    fun ex(msg: String) {
        if (showLogs)
            Log.e(LOG_TAG_RESPONSE, msg)
        if (showFileLogs)
            writeInFile(msg, true)
    }

    fun e(msg: String, t: Throwable) {
        if (showLogs)
            Log.e(LOG_TAG, msg, t)
        if (showFileLogs)
            writeInFile(msg, true)

    }

    fun v(msg: String) {
        if (showLogs)
            Log.v(LOG_TAG, msg)
        if (showFileLogs)
            writeInFile(msg, true)

    }

    /**
     * Testing function to write every log in file for trouble shooting
     *
     * @param message
     * @param append  pass false to reset the file
     */
    fun writeInFile(message: String, append: Boolean) {

        val writeInFileThread = Thread(Runnable {
            //                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            val file = File(Environment.getExternalStorageDirectory(), LOGS_FILE_NAME)

            val fileLength = file.length()
            if (fileLength > 50 * 1024 * 1024)
                file.delete()

            try {
                val fos = FileOutputStream(file, true)

                val buffer = (message + "\r\n").toByteArray()
                fos.write(buffer)
                fos.flush()
                fos.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        writeInFileThread.start()


    }

    fun clearLogFile() {
        val file = File(Environment.getExternalStorageDirectory(), "BingeHireLogs.txt")
        if (file.exists()) {
            file.delete()
        }
    }

}