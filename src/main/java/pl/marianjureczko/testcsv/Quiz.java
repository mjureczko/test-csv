package pl.marianjureczko.testcsv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

record Task(
        Question question,
        String option1,
        String option2,
        String option3,
        String option4,
        String option5
) {
}

class Quiz {
    private List<Question> questions;
    private ArrayList<Question> unanswered;
    private double correct = 0.0;
    private double mistakes = 0.0;

    Quiz(List<Question> questions) {
        if(questions.size() < 5) {
            throw new RuntimeException("Not enough questions!");
        }
        this.questions = questions;
        this.unanswered = new ArrayList<>(questions);
    }

    Task nextTask() {
        Question selected = getRandomItemFrom(unanswered);
        List<String> answers = add4answers(selected.answer());
        Collections.shuffle(answers);
        return new Task(selected, answers.get(0), answers.get(1), answers.get(2), answers.get(3), answers.get(4));
    }

    boolean hasMoreTasks() {
        return !unanswered.isEmpty();
    }

    void answeredCorrectly(Question question) {
        correct += 1;
        unanswered.remove(question);
    }

    void misteake() {
        mistakes += 1;
    }

    double summary() {
        return correct / (correct + mistakes);
    }

    @Override
    public String toString() {
        return "Q:" + questions.toString() + "\nUA" + unanswered.toString();
    }

    private Question getRandomItemFrom(List<Question> collection) {
        return collection.get(ThreadLocalRandom.current().nextInt(collection.size()));
    }

    private List<String> add4answers(String answer) {
        HashSet<String> result = new HashSet<>();
        result.add(answer);
        while (result.size() < 5) {
            result.add(getRandomItemFrom(questions).answer());
        }
        return new ArrayList<>(result);
    }
}

