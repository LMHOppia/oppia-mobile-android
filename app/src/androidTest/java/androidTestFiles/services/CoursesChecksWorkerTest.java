package androidTestFiles.services;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;
import androidx.work.Configuration;
import androidx.work.ListenableWorker;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestListenableWorkerBuilder;
import androidx.work.testing.WorkManagerTestInitHelper;

import org.digitalcampus.mobile.learning.BuildConfig;
import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.model.Course;
import org.digitalcampus.oppia.model.CoursesRepository;
import org.digitalcampus.oppia.model.User;
import org.digitalcampus.oppia.service.CoursesChecksWorkerManager;
import org.digitalcampus.oppia.service.CoursesChecksWorker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidTestFiles.utils.CourseUtils;
import androidTestFiles.utils.parent.NotificationsUiTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CoursesChecksWorkerTest extends NotificationsUiTest {

    @Mock
    CoursesRepository coursesRepository;
    @Mock
    User user;

    private Context context;
    private CoursesChecksWorkerManager coursesChecksWorkerManager;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        coursesChecksWorkerManager = new CoursesChecksWorkerManager(context);
        Configuration config = new Configuration.Builder()
                // Set log level to Log.DEBUG to
                // make it easier to see why tests failed
                .setMinimumLoggingLevel(Log.DEBUG)
                // Use a SynchronousExecutor to make it easier to write tests
                .setExecutor(new SynchronousExecutor())
                .build();

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
                context, config);

    }

    @Test
    public void testTrackerWorkerFinishSuccessfuly() throws ExecutionException, InterruptedException {


        // FOR LISTENABLE WORKERS: (https://stackoverflow.com/a/56200464/1365440)
        ListenableWorker testNoCourseDownloadedWorker = TestListenableWorkerBuilder.from(context, CoursesChecksWorker.class).build();
        ListenableWorker.Result result = testNoCourseDownloadedWorker.startWork().get();
        assertThat(result, is(ListenableWorker.Result.success()));

    }


    private void givenThereAreSomeCourses(int numberOfCourses) {

        ArrayList<Course> courses = new ArrayList<>();

        for (int i = 0; i < numberOfCourses; i++) {
            courses.add(CourseUtils.createMockCourse());
        }

        when(coursesRepository.getCourses(any())).thenReturn(courses);

    }

    @Test
    public void doesNotshowNotificationIfNoUserIsLoggedIn() throws Exception {

        when(user.getUsername()).thenReturn(null);

        givenThereAreSomeCourses(BuildConfig.DOWNLOAD_COURSES_DISPLAY - 1);

        coursesChecksWorkerManager.checkNoCoursesInstalled();

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(context.getString(R.string.notification_course_download_title))), 1000);

        Assert.assertNull(device.findObject(By.text(context.getString(R.string.notification_course_download_title))));

    }

    @Test
    public void showsNotificationIfCoursesLessThanConfiguredParameter() throws Exception {

        when(user.getUsername()).thenReturn("test_user");

        givenThereAreSomeCourses(BuildConfig.DOWNLOAD_COURSES_DISPLAY - 1);

        coursesChecksWorkerManager.checkNoCoursesInstalled();

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(context.getString(R.string.notification_course_download_title))), 1000);
        Assert.assertNotNull(device.findObject(By.text(context.getString(R.string.notification_course_download_title))));

    }


    @Test
    public void doesNotshowNotificationIfCoursesEqualThanConfiguredParameter() throws Exception {

        when(user.getUsername()).thenReturn("test_user");

        givenThereAreSomeCourses(BuildConfig.DOWNLOAD_COURSES_DISPLAY);

        coursesChecksWorkerManager.checkNoCoursesInstalled();

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(context.getString(R.string.notification_course_download_title))), 1000);
        Assert.assertNull(device.findObject(By.text(context.getString(R.string.notification_course_download_title))));

    }

}
