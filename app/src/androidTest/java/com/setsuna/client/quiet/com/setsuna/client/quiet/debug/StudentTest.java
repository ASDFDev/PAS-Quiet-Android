package com.setsuna.client.quiet.com.setsuna.client.quiet.debug;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.setsuna.client.quiet.R;
import com.setsuna.client.quiet.signInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class StudentTest {

    private static final String student_username = "s10001";
    private static final String student_password = "student";

    @Rule
    public ActivityTestRule<signInActivity> signInActivityActivityTestRule = new ActivityTestRule<>(signInActivity.class);

    @Test
    public void fillEditText() {
        onView(withId(R.id.textEdit_userID)).perform(typeText(student_username),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.textEdit_password)).perform(typeText(student_password),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
    }
}
