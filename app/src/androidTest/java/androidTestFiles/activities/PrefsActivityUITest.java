package androidTestFiles.activities;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static androidTestFiles.utils.UITestActionsUtils.waitForView;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.digitalcampus.mobile.learning.BuildConfig;
import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.activity.MainActivity;
import org.digitalcampus.oppia.activity.PrefsActivity;
import org.digitalcampus.oppia.activity.WelcomeActivity;
import org.digitalcampus.oppia.model.Course;
import org.digitalcampus.oppia.model.CoursesRepository;
import org.digitalcampus.oppia.model.Lang;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.UUID;

import androidTestFiles.utils.CourseUtils;
import androidTestFiles.utils.TestUtils;
import androidTestFiles.utils.parent.DaggerInjectMockUITest;

@RunWith(AndroidJUnit4.class)
public class PrefsActivityUITest extends DaggerInjectMockUITest {

    @Mock
    CoursesRepository coursesRepository;
    @Mock
    SharedPreferences prefs;
    @Mock
    SharedPreferences.Editor editor;


    @Before
    public void setUp() throws Exception {
        initMockEditor();
        when(prefs.edit()).thenReturn(editor);
    }

    private void initMockEditor() {
        when(editor.remove(anyString())).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);
    }

    private void givenThereAreSomeCourses(int numberOfCourses) {

        ArrayList<Course> courses = new ArrayList<>();

        for (int i = 0; i < numberOfCourses; i++) {
            courses.add(CourseUtils.createMockCourse());
        }

        when(coursesRepository.getCourses((Context) any())).thenReturn(courses);

    }

    @Test
    public void showsChangeLanguageOptionIfThereAreCoursesWithManyLanguages() throws Exception {

        givenThereAreSomeCourses(1);

        coursesRepository.getCourses((Context) any()).get(0).setLangs(new ArrayList<Lang>() {{
            add(new Lang("en", "English"));
            add(new Lang("es", "Spanish"));
        }});

        try (ActivityScenario<PrefsActivity> scenario = ActivityScenario.launch(PrefsActivity.class)) {

            waitForView(withId(androidx.preference.R.id.recycler_view))
                    .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(R.string.prefDisplay_title)),
                            click()));

            waitForView(withText(R.string.prefContentLanguage)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void hideChangeLanguageOptionIfThereAreCoursesWithOnlyOneLanguage() throws Exception {

        givenThereAreSomeCourses(1);

        coursesRepository.getCourses((Context) any()).get(0).setLangs(new ArrayList<Lang>() {{
            add(new Lang("en", "English"));
        }});

        try (ActivityScenario<PrefsActivity> scenario = ActivityScenario.launch(PrefsActivity.class)) {

            waitForView(withText(R.string.prefContentLanguage)).check(doesNotExist());
        }
    }


    @Test
    public void goToMainActivityIfUserDontModifyServerUrl() throws InterruptedException {

        if (!BuildConfig.MENU_ALLOW_SETTINGS) {
            return;
        }

        when(prefs.getString(eq(PrefsActivity.PREF_USER_NAME), anyString())).thenReturn("test_user");
        when(prefs.getBoolean(eq(PrefsActivity.PREF_ADMIN_PROTECTION), anyBoolean())).thenReturn(false);
        when(prefs.getString(eq(PrefsActivity.PREF_TEST_ACTION_PROTECTED), anyString())).thenReturn("false");
        when(prefs.getString(eq(PrefsActivity.PREF_SERVER), anyString())).thenReturn("https://mock-server.com");

        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext())
                .edit().putString(PrefsActivity.PREF_SERVER, "https://mock-server.com").apply();

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            openDrawer();

            waitForView(withText(R.string.menu_settings)).perform(click());

            waitForView(withId(androidx.preference.R.id.recycler_view))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(R.string.prefAdvanced_title)), click()));

            waitForView(withId(androidx.preference.R.id.recycler_view))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(R.string.prefServer)), click()));

            closeSoftKeyboard();

            waitForView(withText(android.R.string.ok))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());

            pressBackUnconditionally();
            pressBackUnconditionally();

            assertEquals(MainActivity.class, TestUtils.getCurrentActivity().getClass());

        }

    }


    @Test
    public void goToWelcomeActivityIfUserModifyServerUrl() throws InterruptedException {

        if (!BuildConfig.MENU_ALLOW_SETTINGS) {
            return;
        }

        when(prefs.getString(eq(PrefsActivity.PREF_USER_NAME), anyString())).thenReturn("test_user");
        when(prefs.getBoolean(eq(PrefsActivity.PREF_ADMIN_PROTECTION), anyBoolean())).thenReturn(false);
        when(prefs.getString(eq(PrefsActivity.PREF_TEST_ACTION_PROTECTED), anyString())).thenReturn("false");
        when(prefs.edit()).thenReturn(editor);

        when(prefs.getString(eq(PrefsActivity.PREF_SERVER), anyString())).thenReturn("https://mock-server.com");

        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext())
                .edit().putString(PrefsActivity.PREF_SERVER, "https://mock-server.com").apply();

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            openDrawer();

            waitForView(withText(R.string.menu_settings)).perform(click());

            waitForView(withId(androidx.preference.R.id.recycler_view))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(R.string.prefAdvanced_title)), click()));

            waitForView(withId(androidx.preference.R.id.recycler_view))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(R.string.prefServer)), click()));

            waitForView(allOf(instanceOf(EditText.class)))
                    .inRoot(isDialog())
                    .perform(clearText(), typeText(String.format("https://some-url-%s.com", getRandomString())),
                            ViewActions.closeSoftKeyboard());

            closeSoftKeyboard();

            waitForView(withText(android.R.string.ok))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());

            waitForView(withText(R.string.accept))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());

            pressBackUnconditionally();
            pressBackUnconditionally();

            assertEquals(WelcomeActivity.class, TestUtils.getCurrentActivity().getClass());

        }

    }

    private Object getRandomString() {
        return UUID.randomUUID().toString();
    }

}
