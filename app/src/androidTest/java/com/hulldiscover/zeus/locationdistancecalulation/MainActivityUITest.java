package com.hulldiscover.zeus.locationdistancecalulation;

import android.support.annotation.NonNull;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.hulldiscover.zeus.locationdistancecalulation.Activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Zeus on 24/05/16.
 */
@RunWith(AndroidJUnit4.class) // Test Runner

public class MainActivityUITest {

    /* allows to simply specify the activity your going to test
     * and will create the activity at runtime
     * and not have to extend the TestCase that is built into expresso.
     * Launches an activity when your test starts.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainActivity() {
        onView(withId(R.id.textView))            // withId(R.id.my_view) is a ViewMatcher
                .perform(click())                // click() is a ViewAction
                .check(matches(isDisplayed()));  // matches(isDisplayed()) is a ViewAssertion
    }

    @Test
    public void checkNumberInputTo_mainActivity() {
        onView(withId(R.id.inputXCoordinate))
                .perform(typeText("3"))
                .check(matches(isDisplayed()));
    }

    /*
    * This test accesses an the EditText field,
    * for X coordinates,
    * enters a string of text,
    * closes the virtual keyboard,
    * and then performs a button click.
    *
    * When the button click is performed,
    * an error message should be displayed
    * to the screen.
    */
    @Test
    public void testCheckEditTextFieldsValidationMessage_xCoordinate_mainActivity() {
        onView(withId(R.id.inputXCoordinate))
                .perform(typeText("-20"), closeSoftKeyboard());
        onView(withId(R.id.findButton)).perform(click());

        onView(allOf(withId(R.id.inputXCoordinate),
                withText("enter a valid X coordinate between -10 and 10")));
    }

    /*
     * This test accesses an the EditText field,
     * for Y coordinates,
     * enters a string of text,
     * closes the virtual keyboard,
     * and then performs a button click.
     *
     * When the button click is performed,
     * an error message should be displayed
     * to the screen.
     */
    @Test
    public void testCheckEditTextFieldsValidationMessage_yCoordinate_mainActivity() {
        onView(withId(R.id.inputYCoordinate))
                .perform(typeText("-20"), closeSoftKeyboard());
        onView(withId(R.id.findButton)).perform(click());

        //TODO: Check if this is actually checking for the right result
        onView(allOf(withId(R.id.inputYCoordinate),
                withText("enter a valid Y coordinate between -10 and 10")));
    }

    /*
     * This test accesses an EditText field,
     * enters a string of text,
     * closes the virtual keyboard,
     * and then performs a button click.
     */
    @Test
    public void testCheckInputCoordinates_mainActivity() {
        // find X coordinate input-box
        // type text, number
        onView(withId(R.id.inputXCoordinate))
                .perform(typeText("5"), closeSoftKeyboard());

        // find Y coordinate input-box
        // type text, number
        // click find-button
        onView(withId(R.id.inputYCoordinate))
                .perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.findButton)).perform(click());

        // Check that the inputs was successful, by
        // verifying that we have really submitted correct values
        // by checking the other screens content.
        // This is done by,
        // looking for a property that on the next screen.
        onView(withId(R.id.display_image));
    }


    /*
     * test enters an invalid value in one of the fields,
     * presses "find events button"
     * and checks that the field is actually displaying an error message.
     */
    @Test
    public void testErrorMessage_mainActivity () {
        onView(withId(R.id.inputXCoordinate))
                .perform(typeText("-20"), closeSoftKeyboard());
        onView(withId(R.id.findButton)).perform(click());

        //onView(withId(R.id.inputXCoordinate)).check(matches(withError(mActivityRule.getActivity().getString(R.string.xCoordinate_error_message))));
        //TODO: Fix error "java.lang.String java.lang.CharSequence.toString()' on a null object reference" that is causing this Test not to work
        onView(withId(R.id.inputXCoordinate)).check(ViewAssertions.matches(
                withErrorText(Matchers.containsString(mActivityRule.getActivity().getString(R.string.xCoordinate_error_message)))));

    }

    /*
     * This method mocks an intent,
     * so that you can check if an the mainActivity
     * has issued the correct intent,
     * and reacts correct if it receives the correct intent results.
     */
    @Test
    public void testTriggerIntent() {
        // find X coordinate input-box
        // type text, number
        onView(withId(R.id.inputXCoordinate))
                .perform(typeText("5"), closeSoftKeyboard());

        // find Y coordinate input-box
        // type text, number
        onView(withId(R.id.inputYCoordinate))
                .perform(typeText("5"), closeSoftKeyboard());

        // click find-button
        onView(withId(R.id.findButton)).perform(click());

        // check intended activity is as expected
        // using a canned RecordedIntentMatcher to validate that an intent resolving
        // to the "NearbyEventsList" activity has been sent.
        intended(toPackage("com.hulldiscover.zeus.locationdistancecalulation.Activity.NearbyEventsList"));
    }

    /*
     * Custom ViewMatcher
     * this method helps check error messages displayed to
     * UI are as expected.
     */
    @NonNull
    public static Matcher<View> withErrorText(final Matcher<String> stringMatcher) {

        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("with error text: ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final EditText textView) {
                return stringMatcher.matches(textView.getError().toString());
            }
        };
    }

    /*public static Matcher<View> withError(final String expectedError) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String error = ((EditText) view).getError().toString();
                return expectedError.equals(error);

                //EditText editText = (EditText) view;
                //return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }*/


}
