package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.flowoverstack.testdata.FakePreferenceRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionActivityViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var sut: QuestionActivityViewModel
    private val repository = FakePreferenceRepository()

    override fun setUp() {
        Dispatchers.setMain(dispatcher)
        sut = QuestionActivityViewModel(repository)
    }

    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }

    fun `test getAppOpenCountPref __ pass_any_key_not_previously_saved __ null should be returned`() =
        runBlocking {
            val result = sut.getAppOpenCountPref("int").first()
            assertNull(result)
        }

    fun `test saveAppOpenCounts __ pass_any_key __ data source should not be empty`() =
        runBlocking {
            sut.saveAppOpenCounts("key", 20)
            assertFalse(repository.isEmpty())
        }

    fun `test saveAppOpenCounts __ data source should contain the key saved`() = runBlocking {
        sut.saveAppOpenCounts("key", 20)
        assertTrue(repository.contains("key"))
    }

    fun `test saveAppOpenCounts when retrieved the value should be the correct value saved`() =
        runBlocking {
            sut.saveAppOpenCounts("key", 20)
            val result = sut.getAppOpenCountPref("key").first()
            assertEquals(20, result)
        }

    fun `test getAppOpenCountPref when retrieved the value should be the correct value saved`() =
        runBlocking {
            sut.saveAppOpenCounts("abc", 5)
            val result = sut.getAppOpenCountPref("abc").first()
            assertEquals(5, result)
        }

    fun `test deletePreferences the data store should be empty`() = runBlocking {
        sut.saveAppOpenCounts("xyz", 1)
        sut.saveAppOpenCounts("tea", 2)
        sut.deletePreferences()
        assertTrue(repository.isEmpty())
    }
}