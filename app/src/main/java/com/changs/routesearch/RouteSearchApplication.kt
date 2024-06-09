package com.changs.routesearch

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class RouteSearchApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIENT_ID)
    }
}