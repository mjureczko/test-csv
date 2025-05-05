package pl.marianjureczko.testcsv;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

record Question(
        String question,
        String answer
) {
    boolean isAnswerCorrect(String answerCandidate) {
        return answer.equalsIgnoreCase(answerCandidate);
    }
}

class DataRepository {

    private final TestFileFinder fileFinder = new TestFileFinder();
    private final CsvParser csvParser = new CsvParser();

    List<Question> loadQuestions() {
        File directory = new File(".");
        Optional<String> fileWithTest = fileFinder.findTestFile(directory);
        if (fileWithTest.isPresent()) {
            return loadFromCsv(fileWithTest.get());
        } else {
            return List.of();
        }
    }

    private List<Question> loadFromCsv(String csvFileName) {
        try {
            List<String> lines = Files.readAllLines(new File(csvFileName).toPath());
            List<Question> questions = csvParser.parseCsvContent(lines);
            return questions;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}

class TestFileFinder {
    Optional<String> findTestFile(File directory) {
        Optional<String> fileWithTest = Arrays.stream(directory.list())
                .filter(it -> it.matches(".*csv"))
                .findFirst();
        return fileWithTest;
    }
}

class CsvParser {

    private final char SEPARATOR = '-';

    List<Question> parseCsvContent(List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            int sepIndex = line.indexOf(SEPARATOR);
            if (sepIndex != -1) {
                sepIndex = correctionForSecondDash(line, sepIndex);
                String answer = line.substring(0, sepIndex).trim();
                String question = line.substring(sepIndex + 1).trim();
                questions.add(new Question(question, answer));
            }
        }
        return questions;
    }

    private int correctionForSecondDash(String line, int firstSepIndex) {
        int secondSepIndex = line.indexOf(SEPARATOR, firstSepIndex + 1);
        if (secondSepIndex != -1) {
            String betweenSeparators = line.substring(firstSepIndex + 1, secondSepIndex).trim();
            if (betweenSeparators.chars().allMatch(Character::isDigit)) {
                return secondSepIndex;
            }
        }
        return firstSepIndex;
    }
}