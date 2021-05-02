package com.ratnasagar.remotestate.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ratnasagar.remotestate.model.TrackingData

class MainRepository {
    var topicsMutableLiveData: MutableLiveData<ArrayList<TrackingData>> = MutableLiveData()
    companion object {
        @Volatile
        var instance: MainRepository? = null
            get() {
                if (field == null) {
                    field = MainRepository()
                }
                return field
            }
            private set
    }
}