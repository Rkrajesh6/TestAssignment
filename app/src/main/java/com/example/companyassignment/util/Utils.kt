package com.example.companyassignment.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.jetbrains.annotations.Contract

object Utils {
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val SEARCH_BASE_URL = "https://api.github.com/"
    const val REQUEST_TIMEOUT = 60L
    const val IN_QUALIFIER = "in:name,description"
    const val GITHUB_STARTING_PAGE_INDEX = 1
    const val LAST_SEARCH_QUERY: String = "last_search_query"
    const val DEFAULT_QUERY = "Android"

    fun isNetworkAvailable(context: Context): Boolean {
        var isInternetConnected = false
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkArray = connectivityManager.allNetworks
            for (network in networkArray) {
                val networkInfo = connectivityManager.getNetworkInfo(network)
                isInternetConnected = isInternetConnected || checkNetworkInfo(networkInfo)
            }
        } catch (ignore: Exception) {
        }
        return isInternetConnected
    }

    @Contract("null -> false")
    private fun checkNetworkInfo(networkInfo: NetworkInfo?): Boolean {
        if (networkInfo != null && networkInfo.isConnected) {
            when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> return true
                ConnectivityManager.TYPE_MOBILE -> return true
                ConnectivityManager.TYPE_ETHERNET -> return true
            }
        }
        return false
    }
}