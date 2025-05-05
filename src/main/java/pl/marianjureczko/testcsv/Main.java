package pl.marianjureczko.testcsv;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.io.PrintWriter;

enum State {
    WAITING_FOR_RESPONSE,
    SUCCESS,
    MISTAKE,
    SUMMARY
}

public class Main extends Application {

    private static Quiz quiz;
    private static Task currentTask;
    private static State state = State.WAITING_FOR_RESPONSE;

    private Label largeText = new Label();
    private Button answer1 = new Button();
    private Button answer2 = new Button();
    private Button answer3 = new Button();
    private Button answer4 = new Button();
    private Button answer5 = new Button();
    private Label response = new Label("");
    private Button nextButton = new Button("Następne pytanie");

    @Override
    public void start(Stage primaryStage) {
        // Large text
        largeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        nextTask();

        HBox buttonRow = new HBox(10, answer1, answer2, answer3, answer4, answer5);
        buttonRow.setAlignment(Pos.CENTER);

        nextButton.setOnAction(event -> {
            if (quiz.hasMoreTasks()) {
                nextTask();
            } else {
                summary();
            }
        });

        HBox textButtonRow = new HBox(10, response, nextButton);
        textButtonRow.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(20, largeText, buttonRow, textButtonRow);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 300);
        scene.getStylesheets().add(".root { -fx-font-family: \"Arial\"; }");

        primaryStage.setTitle("Testy historyczne");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void summary() {
        nextButton.setVisible(false);
        String motivation = "";
        double summary = quiz.summary();
        if (quiz.summary() < 0.4) {
            motivation = "Postaraj się bardziej.";
        }
        if (quiz.summary() > 0.9) {
            motivation = "Gratulacje, świetny wynik!";
        }
        String result = String.valueOf(summary * 100.0);
        if (result.length() > 6) {
            result = result.substring(0, 5);
        }
        response.setText("Twój wynik to " + result + "%." + " " + motivation);
    }

    private void nextTask() {
        currentTask = quiz.nextTask();
        largeText.setText(currentTask.question().question());
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
        answer4.setDisable(false);
        answer5.setDisable(false);
        answer1.setText(currentTask.option1());
        answer2.setText(currentTask.option2());
        answer3.setText(currentTask.option3());
        answer4.setText(currentTask.option4());
        answer5.setText(currentTask.option5());
        answer1.setOnAction(getAnswerHandler(currentTask.option1()));
        answer2.setOnAction(getAnswerHandler(currentTask.option2()));
        answer3.setOnAction(getAnswerHandler(currentTask.option3()));
        answer4.setOnAction(getAnswerHandler(currentTask.option4()));
        answer5.setOnAction(getAnswerHandler(currentTask.option5()));
        response.setVisible(false);
        nextButton.setVisible(false);
    }

    private EventHandler<ActionEvent> getAnswerHandler(String answer) {
        return event -> {
            if (currentTask.question().answer().equalsIgnoreCase(answer)) {
                response.setText("Brawo! Poprawna odpowiedź.");
                quiz.answeredCorrectly(currentTask.question());
            } else {
                response.setText("Poprawna odpowiedź to " + currentTask.question().answer());
                quiz.misteake();
            }
            answer1.setDisable(true);
            answer2.setDisable(true);
            answer3.setDisable(true);
            answer4.setDisable(true);
            answer5.setDisable(true);
            response.setVisible(true);
            nextButton.setVisible(true);
        };
    }

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("log.txt")) {
            System.setProperty("user.language", "pl");
            System.setProperty("user.region", "PL");
            writer.println("start");
            try {
                quiz = new Quiz(new DataRepository().loadQuestions());
                launch(args);
            } catch(Exception e) {
                writer.println(e.getMessage());
                e.printStackTrace(writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}