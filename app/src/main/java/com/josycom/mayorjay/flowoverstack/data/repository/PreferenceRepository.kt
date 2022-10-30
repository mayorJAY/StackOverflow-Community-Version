package com.josycom.mayorjay.flowoverstack.data.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    fun getIntPreferenceFlow(key: String): Flow<Int?>
    suspend fun setIntPreference(key: String, value: Int)
    suspend fun deleteAllPreferences()
    suspend fun contains(key: String): Boolean
    suspend fun isEmpty(): Boolean
}