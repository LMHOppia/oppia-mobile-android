package androidTestFiles.widgets.quiz;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static androidTestFiles.utils.UITestActionsUtils.waitForView;
import static androidTestFiles.utils.matchers.EspressoTestsMatchers.withDrawable;
import static androidTestFiles.utils.matchers.RecyclerViewMatcher.withRecyclerView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.widgets.QuizWidget;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidTestFiles.utils.TestUtils;
import androidTestFiles.utils.parent.BaseTest;

@RunWith(AndroidJUnit4.class)
public class MultiselectWithFeedbackTest extends BaseQuizTest {

    private static final String MULTISELECT_WITHFEEDBACK_JSON =
            BaseTest.PATH_QUIZZES + "/multiselect_with_feedback.json";
    private static final String FIRST_QUESTION_TITLE =
            "Which of the following are colours of the rainbow?";
    private static final String CORRECT_ANSWER_1 = "Red";
    private static final String CORRECT_ANSWER_2 = "Orange";
    private static final String INCORRECT_ANSWER_1 = "Black";
    private static final String CORRECT_ANSWER_01_FEEDBACK = CORRECT_ANSWER_1 + ": correct";
    private static final String CORRECT_ANSWER_02_FEEDBACK = CORRECT_ANSWER_2 + ": correct";
    private static final String INCORRECT_ANSWER_FEEDBACK = INCORRECT_ANSWER_1 + ": wrong";

    @Override
    protected String getQuizContentFile() {
        return MULTISELECT_WITHFEEDBACK_JSON;
    }

    @Test
    public void correctAnswer() {
        launchInContainer(QuizWidget.class, args, R.style.Oppia_ToolbarTheme);
        waitForView(withId(R.id.take_quiz_btn)).perform(click());

        waitForView(withId(R.id.question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));

        waitForView(withText(CORRECT_ANSWER_1)).perform(click());
        waitForView(withText(CORRECT_ANSWER_2)).perform(click());
        waitForView(withId(R.id.mquiz_next_btn)).perform(click());

        String actual = TestUtils.getCurrentActivity().getString(R.string.widget_quiz_results_score, (float) 100);
        waitForView(withId(R.id.quiz_results_score))
                .check(matches(withText(actual)));

        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_1))));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_2))));
        // check feedback text
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_01_FEEDBACK))));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_02_FEEDBACK))));

        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_feedback_image))
                .check(matches(withDrawable(R.drawable.quiz_tick)));

    }

    @Test
    public void incorrectAnswer() {
        launchInContainer(QuizWidget.class, args, R.style.Oppia_ToolbarTheme);
        waitForView(withId(R.id.take_quiz_btn)).perform(click());

        waitForView(withId(R.id.question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));

        waitForView(withText(INCORRECT_ANSWER_1)).perform(click());
        waitForView(withId(R.id.mquiz_next_btn)).perform(click());

        String actual = TestUtils.getCurrentActivity().getString(R.string.widget_quiz_results_score, (float) 0);
        waitForView(withId(R.id.quiz_results_score))
                .check(matches(withText(actual)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(INCORRECT_ANSWER_1)));
        // check feedback text
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(INCORRECT_ANSWER_FEEDBACK))));

        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_feedback_image))
                .check(matches(withDrawable(R.drawable.quiz_cross)));
    }

    @Test
    public void oneCorrectAnswer() {
        launchInContainer(QuizWidget.class, args, R.style.Oppia_ToolbarTheme);
        waitForView(withId(R.id.take_quiz_btn)).perform(click());

        waitForView(withId(R.id.question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));

        waitForView(withText(CORRECT_ANSWER_1)).perform(click());
        waitForView(withId(R.id.mquiz_next_btn)).perform(click());

        String actual = TestUtils.getCurrentActivity().getString(R.string.widget_quiz_results_score, (float) 50);
        waitForView(withId(R.id.quiz_results_score))
                .check(matches(withText(actual)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(CORRECT_ANSWER_1)));
        // check feedback text
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_01_FEEDBACK))));

        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_feedback_image))
                .check(matches(withDrawable(R.drawable.quiz_partially_correct)));
    }

    @Test
    public void oneCorrectOneIncorrectAnswer() {
        launchInContainer(QuizWidget.class, args, R.style.Oppia_ToolbarTheme);
        waitForView(withId(R.id.take_quiz_btn)).perform(click());

        waitForView(withId(R.id.question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));

        waitForView(withText(CORRECT_ANSWER_1)).perform(click());
        waitForView(withText(INCORRECT_ANSWER_1)).perform(click());
        waitForView(withId(R.id.mquiz_next_btn)).perform(click());

        String actual = TestUtils.getCurrentActivity().getString(R.string.widget_quiz_results_score, (float) 0);
        waitForView(withId(R.id.quiz_results_score))
                .check(matches(withText(actual)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_text))
                .check(matches(withText(FIRST_QUESTION_TITLE)));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_1))));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_response_text))
                .check(matches(withText(containsString(INCORRECT_ANSWER_1))));

        // check feedback text
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(CORRECT_ANSWER_01_FEEDBACK))));
        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_user_feedback_text))
                .check(matches(withText(containsString(INCORRECT_ANSWER_FEEDBACK))));

        waitForView(withRecyclerView(R.id.recycler_quiz_results_feedback)
                .atPositionOnView(0, R.id.quiz_question_feedback_image))
                .check(matches(withDrawable(R.drawable.quiz_cross)));
    }

}
