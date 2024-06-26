package testFiles.model.quiz;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.digitalcampus.mobile.quiz.Quiz;
import org.digitalcampus.mobile.quiz.model.QuizQuestion;
import org.digitalcampus.mobile.quiz.model.questiontypes.MultiSelect;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import testFiles.utils.BaseTest;
import testFiles.utils.UnitTestsFileUtils;

public class MultiSelectQuestionTest {

    private static final String MULTISELECT_NOFEEDBACK_JSON = BaseTest.PATH_QUIZZES + "/multiselect_no_feedback.json";
    private static final String MULTISELECT_WITHFEEDBACK_JSON = BaseTest.PATH_QUIZZES + "/multiselect_with_feedback.json";
    private static final String DEFAULT_LANG = "en";
    private Quiz quizNoFeedback;
    private Quiz quizWithFeedback;

    @Before
    public void setup() throws Exception {
        // Setting up before every test
        String quizNoFeedbackContent = UnitTestsFileUtils.readFileFromTestResources(MULTISELECT_NOFEEDBACK_JSON);
        quizNoFeedback = new Quiz();
        quizNoFeedback.load(quizNoFeedbackContent, DEFAULT_LANG);

        String quizWithFeedbackContent = UnitTestsFileUtils.readFileFromTestResources(MULTISELECT_WITHFEEDBACK_JSON);
        quizWithFeedback = new Quiz();
        quizWithFeedback.load(quizWithFeedbackContent, DEFAULT_LANG);
    }

    /*
    No Feedback
     */
    // fully correct
    @Test
    public void test_fullycorrectNoFeedback()throws Exception {
        QuizQuestion quizQuestion = quizNoFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        // check before response added
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19770, responseJson.get("question_id"));
        assertEquals(0.0, responseJson.get("score"));
        assertEquals("", responseJson.get("text"));


        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Red");
        userResponses.add("Yellow");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 1, quizQuestion.getUserscore());
        assertEquals(100, quizQuestion.getScoreAsPercent());

        // check json response object
        responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19770, responseJson.get("question_id"));
        assertEquals(1.0, responseJson.get("score"));
        assertEquals("Red||Yellow||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizNoFeedback.getUserscore());
        quizNoFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 1, quizNoFeedback.getUserscore());
    }
    // one correct (partial)
    @Test
    public void test_oneCorrectNoFeedback()throws Exception {
        QuizQuestion quizQuestion = quizNoFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Yellow");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.5, quizQuestion.getUserscore());
        assertEquals(50, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19770, responseJson.get("question_id"));
        assertEquals(0.5, responseJson.get("score"));
        assertEquals("Yellow||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizNoFeedback.getUserscore());
        quizNoFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.5, quizNoFeedback.getUserscore());
    }

    // one correct one incorrect
    @Test
    public void test_partialCorrectNoFeedback()throws Exception {
        QuizQuestion quizQuestion = quizNoFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Yellow");
        userResponses.add("Black");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.0, quizQuestion.getUserscore());
        assertEquals(0, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19770, responseJson.get("question_id"));
        assertEquals(0.0, responseJson.get("score"));
        assertEquals("Yellow||Black||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizNoFeedback.getUserscore());
        quizNoFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.0, quizNoFeedback.getUserscore());
    }

    // none correct
    @Test
    public void test_noneCorrectNoFeedback()throws Exception {
        QuizQuestion quizQuestion = quizNoFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("White");
        userResponses.add("Black");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.0, quizQuestion.getUserscore());
        assertEquals(0, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19770, responseJson.get("question_id"));
        assertEquals(0.0, responseJson.get("score"));
        assertEquals("White||Black||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizNoFeedback.getUserscore());
        quizNoFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.0, quizNoFeedback.getUserscore());
    }

    /*
    With feedback
     */
    // fully correct
    @Test
    public void test_fullycorrectWithFeedback()throws Exception {
        QuizQuestion quizQuestion = quizWithFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Red");
        userResponses.add("Orange");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("<strong>Red:</strong> correct<br><strong>Orange:</strong> correct<br>", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 1, quizQuestion.getUserscore());
        assertEquals(100, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19771, responseJson.get("question_id"));
        assertEquals(1.0, responseJson.get("score"));
        assertEquals("Red||Orange||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizWithFeedback.getUserscore());
        quizWithFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 1, quizWithFeedback.getUserscore());
    }

    // one correct (partial)
    @Test
    public void test_oneCorrectWithFeedback()throws Exception {
        QuizQuestion quizQuestion = quizWithFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Orange");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("<strong>Orange:</strong> correct<br>", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.5, quizQuestion.getUserscore());
        assertEquals(50, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19771, responseJson.get("question_id"));
        assertEquals(0.5, responseJson.get("score"));
        assertEquals("Orange||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizWithFeedback.getUserscore());
        quizWithFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.5, quizWithFeedback.getUserscore());
    }

    // one correct one incorrect
    @Test
    public void test_partialCorrectWithFeedback()throws Exception {
        QuizQuestion quizQuestion = quizWithFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Red");
        userResponses.add("Black");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("<strong>Red:</strong> correct<br><strong>Black:</strong> wrong<br>", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.0, quizQuestion.getUserscore());
        assertEquals(0, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19771, responseJson.get("question_id"));
        assertEquals(0.0, responseJson.get("score"));
        assertEquals("Red||Black||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizWithFeedback.getUserscore());
        quizWithFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.0, quizWithFeedback.getUserscore());
    }

    // none correct
    @Test
    public void test_noneCorrectWithFeedback()throws Exception {
        QuizQuestion quizQuestion = quizWithFeedback.getCurrentQuestion();
        assertTrue(quizQuestion instanceof MultiSelect);

        ArrayList<String> userResponses = new ArrayList<>();
        userResponses.add("Grey");
        userResponses.add("Black");
        quizQuestion.setUserResponses(userResponses);
        assertEquals("<strong>Black:</strong> wrong<br><strong>Grey:</strong> wrong<br>", quizQuestion.getFeedback(DEFAULT_LANG));
        assertEquals((float) 0.0, quizQuestion.getUserscore());
        assertEquals(0, quizQuestion.getScoreAsPercent());

        // check json response object
        JSONObject responseJson = quizQuestion.responsesToJSON();
        assertTrue(responseJson.has("question_id"));
        assertTrue(responseJson.has("score"));
        assertTrue(responseJson.has("text"));

        assertEquals(19771, responseJson.get("question_id"));
        assertEquals(0.0, responseJson.get("score"));
        assertEquals("Grey||Black||", responseJson.get("text"));

        // check for whole quiz
        assertEquals((float) 0, quizWithFeedback.getUserscore());
        quizWithFeedback.mark(DEFAULT_LANG);
        assertEquals((float) 0.0, quizWithFeedback.getUserscore());
    }
    /*
    Multilang, no feedback
     */


    /*
    Multilang, with feedback
     */
}
