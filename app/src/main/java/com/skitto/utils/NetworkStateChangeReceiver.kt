package com.skitto.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import java.util.Objects

class NetworkStateChangeReceiver : BroadcastReceiver() {
    private val NETWORK_AVAILABLE_ACTION = "NetworkAvailable"
    private val IS_NETWORK_AVAILABLE = "isNetworkAvailable"
    private val TAG: String = NetworkStateChangeReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        val networkStateIntent = Intent(NETWORK_AVAILABLE_ACTION)
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context))
//        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent)
    }

    private fun isConnectedToInternet(context: Context?): Boolean {
        return try {
            if (context != null) {
                val connectivityManager =
                    (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            }
            false
        } catch (e: Exception) {
            Objects.requireNonNull(e.message).let {
                if (it != null) {
                    Log.e(TAG, it)
                }
            }
            false
        }
    }
}