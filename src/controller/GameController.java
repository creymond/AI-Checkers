package controller;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import observerPattern.ObservableGame;
import view.GameView;

public class GameController extends Application {

    private ObservableGame game;

    private GameView gameView;
    private AnimationTimer timer;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        game = new ObservableGame();
        gameView = new GameView(600,600, primaryStage, game.getBoard());
        game.addObserver(gameView);
        
        timer = new AnimationTimer() {
            private long prevTime = 0;
            private long timeToWait = 100_000_000;//500_000_000;
            
            @Override
            public void handle(long now) {
                // calculate elapsed time
                if((now-prevTime) < timeToWait) {
                    return;
                }
                step();
                prevTime = now;
            }
        };
        timer.start();
    }

    private void step() {
        if (game.isGameOver()) {
            stopTimer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                
            }
            Platform.exit();
            return;
        } else {
            game.move();
        }     
    }

    /**
     * Starts the controller's timer.
     */
    public void startTimer() {
        // Launch the timer
        timer.start();
    }

    /**
     * Ends the race.
     */
    public void stopTimer() {
        timer.stop();
    }


    public Scene getScene() {
        return gameView.getScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
