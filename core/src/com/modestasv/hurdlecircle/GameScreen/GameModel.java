package com.mv.desktop.hurdlecircle.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mv.desktop.hurdlecircle.Game;
import com.mv.desktop.hurdlecircle.GameScreen.Camera.CameraController;
import com.mv.desktop.hurdlecircle.GameScreen.Camera.CameraState;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.ConnectionLine.ConnectionLine;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Hindernis.Hindernis;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Hindernis.HindernisController;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Hindernis.HindernisState;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.ObjektAbstr;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.OrbitCircle.OrbitCircle;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.ScoreLine.Line;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Spieler.Spieler;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Wall.Wall;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Wall.WallManager;
import com.modestasv.hurdlecircle.Assets;
import com.modestasv.hurdlecircle.MVInterpolate;
import com.modestasv.hurdlecircle.MainMenuScreen.MainMenuScreen;

import java.util.ArrayList;

/**
 * hält Referenzen zu allen Spielobjekten und sorgt für die verschiedenen Zustände des Spiels
 */
public class GameModel implements Screen {

    public final static int LEFTWALL = -285;
    public final static int RIGHTWALL = 285;
    public Game game;
    public GameController gameController;
    public GameView gameView;
    public Spieler spieler;
    public HindernisController hindernisController;
    public CameraController cameraController;
    public ConnectionLine connectionLine;
    public WallManager wallManager;
    public ArrayList<Line> lines = new ArrayList<Line>();
    public Line highScoreLine;

    public static Color bgColorGame = new Color(Color.BLACK);

    public OrbitCircle orbitCircle;

    public float leftBound;
    public float rightBound;

    public StateMachine<GameModel> stateMachine = new DefaultStateMachine<GameModel>(this, MainGameState.PLAYING);

    public GameModel(Game game) {
        this.game = game;

        highScoreLine = new Line(20, Assets.getHighscore(), Color.YELLOW);
        lines.add(new Line(5, 25, Color.GREEN));
        lines.add(new Line(5, 50, Color.GREEN));
        lines.add(new Line(5, 100, Color.GREEN));
        lines.add(new Line(5, 200, Color.GREEN));
        lines.add(new Line(5, 500, Color.GREEN));
        lines.add(new Line(5, 1000, Color.GREEN));
        lines.add(new Line(5, 2000, Color.GREEN));
        lines.add(new Line(5, 5000, Color.GREEN));

        this.gameView = new GameView(this);
        this.connectionLine = new ConnectionLine();
        this.cameraController = new CameraController();
        this.orbitCircle = new OrbitCircle(40);
        this.hindernisController = new HindernisController();

        resetMainGameScreen();

        this.leftBound = LEFTWALL + spieler.getObj().getWidth()/2;
        this.rightBound = RIGHTWALL - spieler.getObj().getWidth()/2;
        this.wallManager = new WallManager(new Wall(new Vector2(leftBound,0), 50), new Wall(new Vector2(rightBound,0), 50));
        this.gameController = new GameController(this);
    }

    public void resetMainGameScreen() {

        this.spieler = new Spieler(new Vector2(0,5), new Vector2(0, -1700f), Assets.SPIELER_NORMAL, 50);
        this.hindernisController.reset();
        for (Line value : lines) {
            value.turnOn();
        }
        highScoreLine.setPoints(Assets.getHighscore());
        highScoreLine.turnOn();
        cameraController = new CameraController();
        gameView.reset();
        stateMachine.changeState(MainGameState.PLAYING);
        System.gc();
        pauseGame(false);
    }

    public void pauseGame(boolean pause) {
        if(!pause ) {
            show();
            gameView.setBW(false);
            Assets.setMainMusicVolume(Assets.NORMAL);
            stateMachine.changeState(MainGameState.PLAYING);
        } else {
            gameView.setBW(true);
            Assets.setMainMusicVolume(Assets.LEISE);
            stateMachine.changeState(MainGameState.PAUSE);
        }
    }

    public boolean isPaused() {
        return stateMachine.isInState(MainGameState.PAUSE);
    }

    public void gameOver() {

        if(!stateMachine.isInState(MainGameState.GAMEOVER)) {
            gameView.gameOverEffect(spieler.getPos());

            if(!Assets.getFirstTimeRunBool()) {

                Assets.putFirstTimeRunBool(true);
            }

            Assets.setMainMusicVolume(Assets.LEISE);
            if(Assets.getVibrate()) { Gdx.input.vibrate(15); }
            Assets.addToCoveredDistance(gameView.hudView.highestScore);
            Assets.incrementDeaths();

            if(gameView.hudView.highestScore > Assets.getHighscore()) {
                Assets.putHighscore(gameView.hudView.highestScore);
            }
            gameView.hudView.initGameOver();
            cameraController.stateMachine.changeState(CameraState.GAMEOVER);

            if (game.actionResolver.getSignedInGPGS()) {

                game.actionResolver.submitScoreGPGS(gameView.hudView.highestScore);

            }

            stateMachine.changeState(MainGameState.GAMEOVER);

            if(game.actionResolver.getSignedInGPGS()) {
                if (gameView.hudView.highestScore >= 5000) game.actionResolver.unlockAchievementGPGS(Assets.achievement5000Points);
                if (gameView.hudView.highestScore >= 2000) game.actionResolver.unlockAchievementGPGS(Assets.achievement2000Points);
                if (gameView.hudView.highestScore >= 1000) game.actionResolver.unlockAchievementGPGS(Assets.achievement1000Points);
                if (gameView.hudView.highestScore >= 500)  game.actionResolver.unlockAchievementGPGS(Assets.achievement500Points);
                if (gameView.hudView.highestScore >= 200)  game.actionResolver.unlockAchievementGPGS(Assets.achievement200Points);
                if (gameView.hudView.highestScore >= 100)  game.actionResolver.unlockAchievementGPGS(Assets.achievement100Points);
                if (gameView.hudView.highestScore >= 50)   game.actionResolver.unlockAchievementGPGS(Assets.achievement50Points);
                if (gameView.hudView.highestScore >= 25)   game.actionResolver.unlockAchievementGPGS(Assets.achievement25Points);

                if (Assets.getQuestionMarkCollected() >= 100) {
                    game.actionResolver.unlockAchievementGPGS(Assets.achievement100Qs);
                }

                if (Assets.getCoveredDistance() >= 500) {
                    game.actionResolver.unlockAchievementGPGS(Assets.achievement500Miles);
                }

            }
        }
    }

    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameView.hudView.stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor( bgColorGame.r,
                             bgColorGame.g,
                             bgColorGame.b, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameController.updateInPause(delta, stateMachine.isInState(MainGameState.GAMEOVER));
        gameView.draw(delta);
        stateMachine.update();

    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        cameraController.resize(width,height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}

