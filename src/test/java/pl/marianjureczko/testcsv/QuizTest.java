package pl.marianjureczko.testcsv;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.ocadotechnology.gembus.test.Arranger.someObjects;
import static org.assertj.core.api.Assertions.assertThat;

class QuizTest {

    List<Question> questions = someObjects(Question.class, 7).toList();
    Quiz quiz = new Quiz(questions);

    @Test
    void shouldReturnTaskWithDifferentOptions() {
        //when
        Task actual = quiz.nextTask();

        //then
        Set<String> allOptions = getAllOptions(actual);
        assertThat(allOptions).hasSize(5);
    }

    @Test
    void shouldReturnCorrectAnswerAmongTheAnswers() {
        //when
        Task actual = quiz.nextTask();

        //then
        Set<String> allOptions = getAllOptions(actual);
        String expected = actual.question().answer();
        assertThat(allOptions).contains(expected);
    }

    @Test
    void shouldNotReturnTasksWithCorrectlyAnsweredQuestions() {
        //given
        questions.stream()
                .skip(1)
                .forEach(q -> quiz.answeredCorrectly(q));

        //when
        Task actual = quiz.nextTask();

        //then
        assertThat(actual.question()).isEqualTo(questions.get(0));
    }

    @NotNull
    private static Set<String> getAllOptions(Task actual) {
        return Set.of(actual.option1(), actual.option2(), actual.option3(), actual.option4(), actual.option5());
    }
}