package com.example.ads

import android.content.Context
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions

object AdsManager {
    private const val UNITY_GAME_ID = "6010039"
    private const val TEST_MODE = true
    const val REWARDED_AD_UNIT = "Rewarded_Android"
    const val INTERSTITIAL_AD_UNIT = "Interstitial_Android"

    fun initialize(context: Context) {
        UnityAds.initialize(
            context,
            UNITY_GAME_ID,
            TEST_MODE,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.d("AdsManager", "Unity Ads Initialized")
                }

                override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {
                    Log.e("AdsManager", "Unity Ads Failed: $message")
                }
            }
        )
    }

    fun showAd(context: Context, adUnit: String, onComplete: () -> Unit) {
        if (UnityAds.isInitialized()) {
            UnityAds.load(adUnit, object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String) {
                    UnityAds.show(context as android.app.Activity, adUnit, UnityAdsShowOptions(), object : IUnityAdsShowListener {
                        override fun onUnityAdsShowFailure(placementId: String, error: UnityAds.UnityAdsShowError?, message: String?) {
                            Log.e("AdsManager", "Ad show failed: $message")
                            onComplete()
                        }

                        override fun onUnityAdsShowStart(placementId: String) {}
                        override fun onUnityAdsShowClick(placementId: String) {}
                        override fun onUnityAdsShowComplete(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
                            onComplete()
                        }
                    })
                }

                override fun onUnityAdsFailedToLoad(placementId: String, error: UnityAds.UnityAdsLoadError?, message: String?) {
                    Log.e("AdsManager", "Ad load failed: $message")
                    onComplete()
                }
            })
        } else {
            onComplete()
        }
    }
}
