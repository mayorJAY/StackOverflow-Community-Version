package com.josycom.mayorjay.flowoverstack

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.josycom.mayorjay.flowoverstack.ui.activity.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenNavigationTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)
    private var scenario: ActivityScenario<MainActivity>? = null

    @Before
    fun setup() {
        scenario = scenarioRule.scenario
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    @Test
    fun test_clickMenuOption_displayQuestionsFilteredByCreation() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        Espresso.onView(ViewMatchers.withText(R.string.action_filter_by_recency)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.recent_questions)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_clickMenuOption_displayQuestionsFilteredByHot() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        Espresso.onView(ViewMatchers.withText(R.string.action_filter_by_hot)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.hot_questions)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_clickMenuOption_displayQuestionsFilteredByVotes() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        Espresso.onView(ViewMatchers.withText(R.string.action_filter_by_vote)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.voted_questions)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_clickMenuOption_displayPopularTagsFragment() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        Espresso.onView(ViewMatchers.withText(R.string.filter_by_popular_tags)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.select_a_tag_to_view_the_related_questions)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_clickMenuOption_displaySearchTagsFragment() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        Espresso.onView(ViewMatchers.withText(R.string.filter_by_tag_name)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_text_input_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_clickSearchFab_launchSearchActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.search_fab)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.type_to_search)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_text_input_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}