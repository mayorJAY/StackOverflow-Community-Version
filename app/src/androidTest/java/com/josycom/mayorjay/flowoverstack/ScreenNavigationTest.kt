package com.josycom.mayorjay.flowoverstack;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.josycom.mayorjay.flowoverstack.ui.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityLaunchTest {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void launchQuestionByCreationFragment(){
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.action_filter_by_recency)).perform(click());
        onView(withId(R.id.tv_recent_questions)).check(matches(isDisplayed()));
    }

    @Test
    public void launchSearchActivity(){
        onView(withId(R.id.search_fab)).perform(click());
        onView(withId(R.id.type_to_search)).perform(click());
        onView(withId(R.id.search_text_input_layout)).check(matches(isDisplayed()));
    }
}
