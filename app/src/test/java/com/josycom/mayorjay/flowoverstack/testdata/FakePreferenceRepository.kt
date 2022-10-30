package com.josycom.mayorjay.flowoverstack.testdata

import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferenceRepository : PreferenceRepository {

    private val fakeDataStore: MutableMap<String, Any> = mutableMapOf()

    override fun getIntPreferenceFlow(key: String): Flow<Int?> {
        return flow {
            emit(fakeDataStore[key] as Int?)
        }
    }

    override suspend fun setIntPreference(key: String, value: Int) {
        fakeDataStore[key] = value
    }

    override suspend fun deleteAllPreferences() {
        fakeDataStore.clear()
    }

    override suspend fun contains(key: String) = fakeDataStore.contains(key)

    override suspend fun isEmpty() = fakeDataStore.isEmpty()
}