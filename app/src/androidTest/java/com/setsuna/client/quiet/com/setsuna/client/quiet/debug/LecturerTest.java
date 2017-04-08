package com.setsuna.client.quiet.com.setsuna.client.quiet.debug;


import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import com.setsuna.client.quiet.R;
import com.setsuna.client.quiet.signInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class LecturerTest {

    private static final String staff_username = "s10001";
    private static final String staff_password = "staff";
    private static final String code = "123456";

  @Rule
    public ActivityTestRule<signInActivity> signInActivityActivityTestRule = new ActivityTestRule<>(signInActivity.class);


    @Test
    public void fillEditText(){
        onView(withId(R.id.textEdit_userID)).perform(typeText(staff_username),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.textEdit_password)).perform(typeText(staff_password),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.textCode)).perform(typeText(code),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button_send)).perform(click());
    }

}
