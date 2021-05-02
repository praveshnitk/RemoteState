package com.ratnasagar.remotestate.viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratnasagar.remotestate.helper.DataBaseHelper
import com.ratnasagar.remotestate.model.TrackingData
import com.ratnasagar.remotestate.repository.MainRepository

class MainViewModel(mainRepository: MainRepository?, context: Context) : ViewModel() {

    var topicsMutableLiveData: MutableLiveData<ArrayList<TrackingData>> = MutableLiveData<ArrayList<TrackingData>>()

    fun getTrackData(context: Context, dbHelper: DataBaseHelper) {
        topicsMutableLiveData.value = dbHelper.trackAll
    }

}