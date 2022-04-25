package application.games;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Game2048Main extends Application {

    private static final int CELL_SIZE = 64;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage myStage) throws Exception {
        myStage.setTitle("Game 2048");

        FlowPane rootNode = new FlowPane();

        myStage.setResizable(false);
        //myStage.setOnCloseRequest(event -> Platform.exit());

        Game2048 game2048 = new Game2048();
        Scene myScene = new Scene(rootNode, game2048.getWidth(), game2048.getHeight());
        myStage.setScene(myScene);

        myScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    game2048.resetGame();
                }

                if (!game2048.canMove() || (!game2048.win && !game2048.canMove())) {
                    game2048.lose = true;
                }

                if (!game2048.win && !game2048.lose) {
                    switch (event.getCode()) {
                        case LEFT:
                            game2048.left();
                            break;
                        case RIGHT:
                            game2048.right();
                            break;
                        case DOWN:
                            game2048.down();
                            break;
                        case UP:
                            game2048.up();
                            break;
                    }
                }
                game2048.relocate(330, 390);
            }
        });

        rootNode.getChildren().add(game2048);
        myStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = game2048.getGraphicsContext2D();
                gc.setFill(Color.rgb(187, 173, 160, 1.0));
                gc.fillRect(0, 0, game2048.getWidth(), game2048.getHeight());

                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        Game2048Cell game2048Cell = game2048.getCells()[x + y * 4];
                        int value = game2048Cell.number;
                        int xOffset = offsetCoors(x);
                        int yOffset = offsetCoors(y);

                        gc.setFill(game2048Cell.getBackground());
                        gc.fillRoundRect(xOffset, yOffset, CELL_SIZE, CELL_SIZE, 14, 14);
                        gc.setFill(game2048Cell.getForeground());

                        final int size = value < 100 ? 32 : value < 1000 ? 28 : 24;
                        gc.setFont(Font.font("Verdana", FontWeight.BOLD, size));
                        gc.setTextAlign(TextAlignment.CENTER);


                        String s = String.valueOf(value);

                        if (value != 0)
                            gc.fillText(s, xOffset + CELL_SIZE / 2, yOffset + CELL_SIZE / 2 - 2);
                        if (game2048.win || game2048.lose) {
                            gc.setFill(Color.rgb(255, 255, 255));
                            gc.fillRect(0, 0, game2048.getWidth(), game2048.getHeight());
                            gc.setFill(Color.rgb(78, 139, 202));
                            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
                            if (game2048.win) {
                                gc.fillText("You win!", 95, 150);
                            }
                            if (game2048.lose) {
                                gc.fillText("Game over!", 150, 130);
                                gc.fillText("You lose!", 160, 200);
                            }
                            if (game2048.win || game2048.lose) {
                                gc.setFont(Font.font("Verdana", FontWeight.LIGHT, 16));
                                gc.setFill(Color.rgb(128, 128, 128));
                                gc.fillText("Press ESC to play again", 110, 270);
                            }
                        }
                        gc.setFont(Font.font("Verdana", FontWeight.LIGHT, 18));
                        gc.fillText("Score: " + game2048.score, 200, 350);
                    }
                }
            }
        }.start();
    }

    private static int offsetCoors(int arg) {
        return arg * (16 + 64) + 16;
    }
}

