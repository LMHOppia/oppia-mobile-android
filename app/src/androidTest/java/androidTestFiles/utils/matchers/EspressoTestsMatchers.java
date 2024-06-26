package androidTestFiles.utils.matchers;

import android.graphics.Bitmap;
import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EspressoTestsMatchers {

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(DrawableMatcher.EMPTY);
    }

    public static Matcher<View> hasDrawable() {
        return new DrawableMatcher(DrawableMatcher.ANY);
    }

    public static Matcher<View> withBitmap(final Bitmap bitmap) {
        return new BitmapMatcher(bitmap);
    }

    public static Matcher<View> withCustomHint(final Matcher<String> stringMatcher) {
        return new BaseMatcher<View>() {

            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                try {
                    Method method = item.getClass().getMethod("getHint");
                    return stringMatcher.matches(method.invoke(item));
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                } catch (IllegalAccessException e) {
                }
                return false;
            }
        };
    }

    public static Matcher<View> withCustomError(final Matcher<String> stringMatcher) {
        return new BaseMatcher<View>() {

            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                try {
                    Method method = item.getClass().getMethod("getError");
                    return stringMatcher.matches(method.invoke(item));
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                } catch (IllegalAccessException e) {
                }
                return false;
            }
        };
    }
}
