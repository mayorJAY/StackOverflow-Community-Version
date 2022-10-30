package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuestionActivityViewModel(private val preferenceRepository: PreferenceRepository) :
    ViewModel() {

    var appOpenCountLiveData: LiveData<Int?>? = null
    var appOpenCount = 0

    fun getAppOpenCountPref(key: String): Flow<Int?> {
        return preferenceRepository.getIntPreferenceFlow(key).apply {
            appOpenCountLiveData = this.asLiveData()
        }
    }

    fun saveAppOpenCounts(key: String, value: Int) {
        viewModelScope.launch {
            preferenceRepository.setIntPreference(key, value)
        }
    }

    fun deletePreferences() {
        viewModelScope.launch {
            preferenceRepository.deleteAllPreferences()
        }
    }
}

class ViewModelProviderFactory(private val preferenceRepository: PreferenceRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(QuestionActivityViewModel::class.java) -> return QuestionActivityViewModel(
                preferenceRepository
            ) as T
            else -> throw IllegalArgumentException("Wrong Class Provided $modelClass")
        }
    }
}