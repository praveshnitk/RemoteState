package com.ratnasagar.remotestate.factory

import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ratnasagar.remotestate.repository.MainRepository
import com.ratnasagar.remotestate.viewmodel.MainViewModel

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory{

    @NonNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
        {
            return MainViewModel(MainRepository.instance,context) as T
                }
        else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }


}
