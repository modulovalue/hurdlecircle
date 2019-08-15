package com.mv.desktop.hurdlecircle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.modestasv.hurdlecircle.GameScreen.GameModel;
import com.modestasv.hurdlecircle.Interfaces.ActionResolver;
import com.modestasv.hurdlecircle.Interfaces.IProxable;
import com.modestasv.hurdlecircle.MainMenuScreen.MainMenuScreen;
import com.modestasv.hurdlecircle.SplashScreen.SplashScreen;

/**
 * Wird von den Jeweiligen Platformen (Android, PC, ... ) erstellt und startet damit das Spiel
 */
public class Game extends com.badlogic.gdx.Game {

    public IProxable proxable;
    public MainMenuScreen mainMenuScreen;
    public GameModel gameModel;
    public SplashScreen splashScreen;
    public ActionResolver actionResolver;


    public Game(IProxable proxable, ActionResolver actionResolver) {
        this.proxable = proxable;
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);
    }

    public void setup() {
        gameModel = new GameModel(this);
        mainMenuScreen = new MainMenuScreen(this);
    }

    public void showGame() {
        Assets.playMainMusic();
        setScreen(gameModel);
    }

    public float getProxiSensorValue() {
        if(proxable.getProx() == -1) {
            return -1;
        } else if(Assets.getProximityBool()) {
            return proxable.getProx();
        } return 0;
    }

}
