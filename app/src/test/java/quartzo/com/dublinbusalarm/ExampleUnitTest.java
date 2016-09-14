package quartzo.com.dublinbusalarm;

import org.joda.time.DateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Ignore;
import org.junit.Test;

import dalvik.annotation.TestTargetClass;
import entities.DaysOfWeek;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Ignore
    @Test
    public void dayOfTheWeekTest(){
        assertEquals(LocalDateTime.now().dayOfWeek().get(), DaysOfWeek.values()[LocalDateTime.now().dayOfWeek().get()].ordinal());
    }
}