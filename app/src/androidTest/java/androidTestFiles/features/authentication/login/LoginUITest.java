package androidTestFiles.features.authentication.login;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotEquals;
import static androidTestFiles.utils.UITestActionsUtils.waitForView;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.activity.WelcomeActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidTestFiles.utils.TestUtils;
import androidTestFiles.utils.parent.BaseTest;
import androidTestFiles.utils.parent.MockedApiEndpointTest;


@RunWith(AndroidJUnit4.class)
public class LoginUITest extends MockedApiEndpointTest {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    public static final String VALID_LOGIN_RESPONSE = BaseTest.PATH_RESPONSES + "/response_201_login.json";
    private static final String WRONG_CREDENTIALS_RESPONSE = BaseTest.PATH_RESPONSES + "/response_400_login.json";


    @Test
    public void showsErrorMessageWhenThereIsNoUsername() throws Exception{

        try (ActivityScenario<WelcomeActivity> scenario = ActivityScenario.launch(WelcomeActivity.class)) {

            waitForView(withId(R.id.welcome_login))
                    .perform(scrollTo(), click());

            waitForView(withId(R.id.login_btn))
                    .perform(scrollTo(), click());

            waitForView(withText(R.string.error_no_username))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void showsErrorMessageWhenTheUsernameOrPasswordAreWrong() throws Exception{

        startServer(400, WRONG_CREDENTIALS_RESPONSE, 0);
        try (ActivityScenario<WelcomeActivity> scenario = ActivityScenario.launch(WelcomeActivity.class)) {

            waitForView(withId(R.id.welcome_login))
                    .perform(scrollTo(), click());

            waitForView(withId(R.id.login_username_field))
                    .perform(closeSoftKeyboard(), scrollTo(), typeText("WrongUsername"));

            waitForView(withId(R.id.login_password_field))
                    .perform(closeSoftKeyboard(), scrollTo(), typeText("WrongPassword"));

            waitForView(withId(R.id.login_btn))
                    .perform(closeSoftKeyboard(), scrollTo(), click());

            waitForView(withText(R.string.error_login))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void changeActivityWhenTheCredentialsAreCorrect() throws Exception {

        startServer(200, VALID_LOGIN_RESPONSE, 0);
        try (ActivityScenario<WelcomeActivity> scenario = ActivityScenario.launch(WelcomeActivity.class)) {

            waitForView(withId(R.id.welcome_login))
                    .perform(scrollTo(), click());

            waitForView(withId(R.id.login_username_field))
                    .perform(closeSoftKeyboard(), scrollTo(), typeText("valid_username"));

            waitForView(withId(R.id.login_password_field))
                    .perform(closeSoftKeyboard(), scrollTo(), typeText("valid_password"));

            waitForView(withId(R.id.login_btn))
                    .perform(closeSoftKeyboard(), scrollTo(), click());

            assertNotEquals(WelcomeActivity.class, TestUtils.getCurrentActivity().getClass());
        }
    }

}
