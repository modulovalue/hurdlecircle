package com.mv.desktop.hurdlecircle.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Hindernis.Hindernis;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Hindernis.HindernisState;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Spieler.Spieler;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Spieler.SpielerState;

import java.util.ArrayList;


/**
 *
 */
public class GameController {

    private GameModel gameModel;
    public Hindernis nearestHindernis;
    public boolean trueTouchingDown = false;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
        nearestHindernis = gameModel.hindernisController.hindernisse.get(1);
    }

    public void update(float delta, boolean gameOver) {

        /* Wenn der Spieler im NOTHANG Zustand ist, wird ein Hinderniss gewählt, und alle Hinderniseffekte zurückgesetzt */
        if(gameModel.spieler.stateMachine.isInState(SpielerState.NOTHANG)) {
            setNearestHindernis(gameModel.hindernisController.hindernisse);
            gameModel.cameraController.zoomNormal();
            gameModel.spieler.drehGeschwindigkeit = Spieler.DREH_GESCHWINDIGKEIT;

        } else {

            /* Hinderniseffekte werden hier umgesetzt */
            if (gameModel.spieler.getNearestHindernis().stateMachine.isInState(HindernisState.GREEN)) {
                gameModel.cameraController.zoomOut();

            } else if (gameModel.spieler.getNearestHindernis().stateMachine.isInState(HindernisState.RED)) {
                gameModel.spieler.drehGeschwindigkeit = Spieler.DREH_GESCHWINDIGKEIT - 0.5f;

            } else if(gameModel.spieler.getNearestHindernis().stateMachine.isInState(HindernisState.QUESTIONMARK)) {

                if(gameModel.spieler.getNearestHindernis().randomNumber <= 1f) {
                    gameModel.spieler.drehGeschwindigkeit = Spieler.DREH_GESCHWINDIGKEIT + 1.5f;

                } else if(gameModel.spieler.getNearestHindernis().randomNumber <= 2f ) {
                    gameModel.spieler.drehGeschwindigkeit = Spieler.DREH_GESCHWINDIGKEIT - MathUtils.random(0.3f, 0.9f);
                    gameModel.cameraController.zoomOut();

                } else if(gameModel.spieler.getNearestHindernis().randomNumber <= 3f) {
                    gameModel.spieler.drehGeschwindigkeit = Spieler.DREH_GESCHWINDIGKEIT;
                    gameModel.cameraController.zoomOut();

                }
            }
        }

        /* Solange das Spiel läuft, wird der Input verarbeitet*/
        if(!gameOver) {
            handleInput();
        }

        /* Objekte werden geupdatet */
        gameModel.gameView.update(trueTouchingDown);
        gameModel.spieler.update(delta, trueTouchingDown, gameOver);
        gameModel.hindernisController.update(delta, gameModel.spieler.getY(), gameModel.gameView.hudView.highestScore);
        gameModel.orbitCircle.update(gameModel.spieler.startedHanging, gameModel.spieler.getNearestHindernis().getPos(),gameModel.spieler.proj.dst(gameModel.spieler.getNearestHindernis().getPos()) * 2, gameModel.spieler.getNearestHindernis().stateMachine.getCurrentState(), gameModel.spieler.startedHanging   );

        gameModel.wallManager.update(trueTouchingDown);

        /* es wird geprüft ob die Bedingungen für ein GameOver erfüllt sind */
        checkGameOver();
    }

    public void updateInPause(float delta, boolean gameOver) {
        gameModel.cameraController.update(delta, gameModel.spieler.getX(), gameModel.spieler.getY(), gameOver);
    }


    /* überprüft ob die Bedingungen für eine Niederlage gegeben sind und falls ja, initialisiert Gameover */
    public void checkGameOver() {

        /*überlappen des Spielers mit den Hindernissen wird überprüft */
        for (int i = 0; i < gameModel.hindernisController.hindernisse.size(); i++) {
            if(gameModel.hindernisController.hindernisse.get(i).getBoundingCircle().overlaps(gameModel.spieler.getBoundingCircle())) {
                gameModel.gameOver();
            }
        }

        /* nachlässigere Variable für ein weniger frustrierendes gameplay */
        Vector2 vec = gameModel.spieler.getPos().cpy().add(gameModel.spieler.getVel().cpy().scl(60));

        /* Grenzenkollision */
        if(gameModel.spieler.stateMachine.isInState(SpielerState.NOTHANG)) {
            if ( gameModel.spieler.getPos().x <= gameModel.leftBound || gameModel.spieler.getPos().x >= gameModel.rightBound ) {
                if(vec.x <= gameModel.leftBound || vec.x >= gameModel.rightBound ) {
                    gameModel.gameOver();
                }
            }
        }

        if(gameModel.spieler.getPos().y <= -2000) {
            gameModel.gameOver();
        }
    }

    public void handleInput() {

        if(Gdx.input.isTouched() ||
                gameModel.game.getProxiSensorValue() == 1f ||
                Gdx.input.isKeyPressed(Input.Keys.SPACE)  ) {
            trueTouchingDown = true;
        } else {
            trueTouchingDown = false;
        }
    }

    /* Bestimmt das nächste Hindernis */
    public void setNearestHindernis(ArrayList<Hindernis> hindernisse) {

        Vector2 extraVector = gameModel.spieler.getPos().cpy().add(gameModel.spieler.getVel().cpy().scl(35));

        for (Hindernis aHindernisse : hindernisse) {
            if (nearestHindernis.getPos().dst(extraVector) > aHindernisse.getPos().dst(extraVector)) {
                nearestHindernis = aHindernisse;
            }
        }

        float projectionsAbstandZumHindernis = gameModel.spieler.getProj(nearestHindernis).dst(nearestHindernis.getPos());
        float hindernisRadiusUndSpielerRadius = nearestHindernis.getBoundingCircle().radius + gameModel.spieler.getBoundingCircle().radius;

        if (projectionsAbstandZumHindernis > hindernisRadiusUndSpielerRadius) {
            System.out.println("NEEEEAREST HINDERNIS GESETZT NORMAL");

        } else {
            System.out.println("MEH" + gameModel.spieler.richtung());
            if(gameModel.spieler.richtung()) {
                nearestHindernis = hindernisse.get(hindernisse.indexOf(nearestHindernis) - 1);
            } else {
                nearestHindernis = hindernisse.get(hindernisse.indexOf(nearestHindernis) + 1);
            }
        }
        gameModel.spieler.setNearestHindernis(nearestHindernis);
    }

}
