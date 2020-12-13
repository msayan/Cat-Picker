package com.hololo.catpicker.library.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionUtil private constructor(context: Context) : LiveData<Boolean>() {
    companion object {
        @Volatile
        private var INSTANCE: ConnectionUtil? = null

        fun getInstance(context: Context): ConnectionUtil {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ConnectionUtil(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var latestState: Boolean? = null

    init {
        networkCallback = NetworkCallback(this)
    }

    override fun onActive() {
        super.onActive()

        if (latestState != null)
            postValue(latestState)
        else
            updateConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager?.registerDefaultNetworkCallback(
                networkCallback!!
            )
            else -> {
                val networkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(TRANSPORT_WIFI)
                    .build()
                connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager?.unregisterNetworkCallback(networkCallback!!)
    }

    fun post(isOnline: Boolean) {
        latestState = isOnline
        postValue(isOnline)
    }

    internal inner class NetworkCallback(private val connectionUtil: ConnectionUtil) :
        ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            for (i in 1..4) {
                Thread.sleep(1000)

                val capabilities = connectivityManager?.getNetworkCapabilities(network)
                val isOnline =
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && capabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )

                connectionUtil.post(isOnline)

                if (isOnline)
                    return
            }
        }

        override fun onLost(network: Network) {
            connectionUtil.post(false)
        }
    }

    private fun updateConnection() {
        connectivityManager?.let { connectivityManager ->
            connectivityManager.activeNetwork?.let { network ->
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                post(
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && capabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                )
            }
        }
    }
}
