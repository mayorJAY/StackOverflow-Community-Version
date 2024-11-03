package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionActivityViewModel @Inject constructor(private val preferenceRepository: PreferenceRepository) :
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