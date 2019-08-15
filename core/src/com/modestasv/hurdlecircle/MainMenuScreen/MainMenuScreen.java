package com.mv.desktop.hurdlecircle.MainMenuScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mv.desktop.hurdlecircle.Game;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.ObjektAbstr;
import com.mv.desktop.hurdlecircle.Assets;
import com.modestasv.hurdlecircle.MVInterpolate;

import java.security.Key;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

/**
 * das Hauptmenü
 */

public class MainMenuScreen implements Screen {

    private Game game;
    private Stage stage;

    private ObjektAbstr settingsBtn, playButton, backButton, achievementsBtn, leaderboardBtn, playStoreBtn;

    private Table titleTable = new Table();
    private Table settingsTable = new Table();
    private Label.LabelStyle textStyle = new Label.LabelStyle();
    private Label highScore, deaths, distance, questionMarksCollected, title;

    private Button proxSensorBtn, vibrBtn, soundBtn;
    private Label proxLbl, vibrLbl, soundLbl;

    private int buttonWidth = 140;

    public static Color bgColor = new Color( 30/255f, 30/255f, 30/255f, 1);
    private Color achievementsColor = new Color(Color.PURPLE.r + 50/255f, Color.PURPLE.g + 50/255f, Color.PURPLE.b + 50/255f, 1);
    private Color leaderboardsColor = new Color(Color.RED.r - 35/255f, Color.RED.g - 35/255f, Color.RED.b - 350/255f, 1);
    private Color playstoreColor = new Color(255/255f, 255/255f, 255/255f, 1);
    private Color playColor = Color.YELLOW;
    private Color settingsColor = Color.GREEN;
    private Color backBtnColor = Color.LIGHT_GRAY;

    private boolean startToTransition = false;

    public MVInterpolate bgColorinterpolator = new MVInterpolate(Interpolation.fade, 5f , 0, 1);


    public MainMenuScreen(final Game game) {
        Gdx.input.setCatchBackKey(true);
        this.game = game;
        stage = new Stage(new FitViewport(Assets.getWidth(), Assets.getHeight()));
        Texture fontTexture = new Texture(Gdx.files.internal("dotty/dotty.png"), true);
        fontTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        BitmapFont font = new BitmapFont(Gdx.files.internal("dotty/dotty.fnt"), new TextureRegion(fontTexture), false);
        textStyle.font = font;
        setupSettingsButton();
        setupAchievementsButton();
        setupSettingsTable();
        setupPlayButton();
        setupLeaderboardButton();
        setupBackButton();
        setupTitleTable();
        setupPlayStoreButton();
        achievementsBtn.toFront();
        leaderboardBtn.toFront();
        playStoreBtn.toFront();
        settingsBtn.toFront();
        titleTable.toFront();
        settingsTable.toFront();

        titleTable.addAction(moveTo(0, Assets.getHeight() * -0.4f));
        showSettingsTable(false);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        if(!startToTransition) {
            bgColorinterpolator.update();
        } else {
            bgColorinterpolator.updateBW();
        }

        Gdx.gl.glClearColor(new Color(Color.BLACK).lerp(bgColor, bgColorinterpolator.getValue()).r,
                new Color(Color.BLACK).lerp(bgColor, bgColorinterpolator.getValue()).g,
                new Color(Color.BLACK).lerp(bgColor, bgColorinterpolator.getValue()).b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.actionResolver.showCloseDialog();
        }
    }

    @Override
    public void show() {
        stage.addAction(fadeIn(0.15f));
        bgColorinterpolator.reset();
        startToTransition = false;
        Assets.pauseMainMusic();
        setHighscoreLbl();
        setDeathsLbl();
        setDistanceText();
        setQeustionMarkCollectedText();
        Gdx.input.setInputProcessor(stage);
    }


    private void setupSettingsTable() {

        int paddingBottom = 27;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));


        settingsTable.padTop(Assets.getHeight()*0.2f);
        settingsTable.align(Align.top);
        settingsTable.setFillParent(true);
        settingsTable.setVisible(false);


        proxSensorBtn = new Button(skin);
        proxSensorBtn.padLeft(20).padRight(20);
        proxLbl = new Label("",textStyle);
        proxLbl.setFontScale(0.5f);
        changeProximityBtnText(proxLbl, proxSensorBtn, Assets.getProximityBool());
        proxSensorBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Assets.clickSound.play(true);
                changeProximityBtnText(proxLbl, proxSensorBtn, !Assets.getProximityBool());
            }
        });
        proxSensorBtn.add(proxLbl);
        settingsTable.add(proxSensorBtn).padBottom(paddingBottom).row();


        vibrBtn = new Button(skin);
        vibrBtn.padLeft(20).padRight(20);
        vibrLbl = new Label("",textStyle);
        vibrLbl.setFontScale(0.5f);
        changeVibrBtnText(vibrLbl, vibrBtn, Assets.getVibrate());
        vibrBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Assets.clickSound.play(true);
                changeVibrBtnText(vibrLbl, vibrBtn, !Assets.getVibrate());
            }
        });
        vibrBtn.add(vibrLbl);
        settingsTable.add(vibrBtn).padBottom(paddingBottom).row();


        soundBtn = new Button(skin);
        soundBtn.padLeft(20).padRight(20);
        soundLbl = new Label("",textStyle);
        soundLbl.setFontScale(0.5f);
        changeSoundBtnText(soundLbl, soundBtn, Assets.getSoundAllow());
        soundBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                changeSoundBtnText(soundLbl, soundBtn, !Assets.getSoundAllow());
            }
        });
        soundBtn.add(soundLbl);
        settingsTable.add(soundBtn).padBottom(0).row();


        highScore = new Label("",textStyle);
        highScore.setFontScale(0.6f);
        settingsTable.add(new Container(highScore)).padBottom(-30).size(Assets.getWidth() * 0.8f, Assets.getHeight() * 0.1f).row();

        distance = new Label("",textStyle);
        distance.setFontScale(0.6f);
        settingsTable.add(new Container(distance)).padBottom(-30).size(Assets.getWidth() * 0.8f, Assets.getHeight() * 0.1f).row();


        deaths = new Label("",textStyle);
        deaths.setFontScale(0.6f);
        settingsTable.add(new Container(deaths)).padBottom(-30).size(Assets.getWidth() * 0.8f, Assets.getHeight() * 0.1f).row();




        questionMarksCollected = new Label("",textStyle);
        questionMarksCollected.setFontScale(0.6f);
        settingsTable.add(new Container(questionMarksCollected)).padBottom(-30).size(Assets.getWidth() * 0.8f, Assets.getHeight() * 0.1f).row();


        stage.addActor(settingsTable);
    }

    private void changeProximityBtnText(Label label, Button button, boolean bool) {
        if(game.getProxiSensorValue() == -1f) {
            button.setColor(Color.RED);
            label.setText("Proximity Sensor: N/A");
            Assets.putProxBool(false);
        } else {
            Assets.putProxBool(bool);
            if(Assets.getProximityBool()) {
                label.setText("Proximity Sensor: On");
            } else {
                label.setText("Proximity Sensor: Off");
            }
        }
    }

    private void changeVibrBtnText(Label label, Button button, boolean bool) {
        if(!Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
            button.setColor(Color.RED);
            label.setText("Vibration: N/A");
            Assets.putVibrate(false);
        } else {
            Assets.putVibrate(bool);
            if(Assets.getVibrate()) {
                label.setText("Vibration: On");
            } else {
                label.setText("Vibration: Off");
            }
        }
    }

    private void changeSoundBtnText(Label label, Button button, boolean bool) {
        if(bool) {
            Assets.putSoundAllow(bool);
            label.setText("Sound: On");
        } else {
            Assets.putSoundAllow(bool);
            label.setText("Sound: Off");
        }
    }

    private void setupTitleTable() {
        title = new Label(Assets.TITLE_NAME,textStyle);
        title.setAlignment(Align.center);
        Container titleContainer = new Container(title);
        title.setAlignment(Align.center);
        titleTable.align(Align.top);
        titleTable.add(titleContainer).padBottom(10).size(Assets.getWidth() * 0.8f, Assets.getHeight() * 0.1f).row();
        titleTable.setFillParent(true);
        stage.addActor(titleTable);
    }

    private void setupPlayButton() {
        playButton = new ObjektAbstr(new Vector2(0,0), new Vector2(Assets.getWidth()- buttonWidth * 0.75f, -buttonWidth * 0.25f), Assets.PLAY_ICON, buttonWidth ) {};
        playButton.setColor(playColor);
        playButton.setTouchable(Touchable.enabled);
        playButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.clickSound.play(true);
                playButton.getActions().clear();
                playButton.addAction(parallel(scaleTo(2f, 2f, 0.5f, swingOut), moveTo(Assets.getWidth() - 200, 60, 0.5f, swingOut)));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startToTransition = true;
                playButton.getActions().clear();
                stage.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        playButton.addAction(sequence(parallel(scaleTo(1f, 1f, 0.2f, swing), moveTo(Assets.getWidth()- buttonWidth * 0.75f, -buttonWidth * 0.25f, 0.2f, swing))));
                    }
                }), fadeOut(0.15f), run(new Runnable() {
                    @Override
                    public void run() {
                        game.showGame();
                        showSettings(false);
                        game.mainMenuScreen.dispose();
                    }
                })));
            }
        });
        stage.addActor(playButton);
    }

    private void setupLeaderboardButton() {
        leaderboardBtn = new ObjektAbstr(new Vector2(0,0), new Vector2(Assets.getWidth()- buttonWidth * 0.75f,Assets.getHeight() -buttonWidth * 0.75f), Assets.LEADERBOARD, buttonWidth ) {};
        leaderboardBtn.setColor(leaderboardsColor);
        leaderboardBtn.setTouchable(Touchable.enabled);
        leaderboardBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.clickSound.play(true);
                leaderboardBtn.getActions().clear();
                leaderboardBtn.addAction(parallel(scaleTo(2f, 2f, 0.5f, swingOut), moveTo(Assets.getWidth()-200, Assets.getHeight() - 200, 0.5f, swingOut)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leaderboardBtn.getActions().clear();
                leaderboardBtn.addAction(sequence(parallel(scaleTo(1f, 1f, 0.2f, swing), moveTo(Assets.getWidth()- buttonWidth * 0.75f,Assets.getHeight() -buttonWidth * 0.75f, 0.2f, swing)), run(new Runnable() {
                    @Override
                    public void run() {
                        game.actionResolver.getLeaderboardGPGS();
                    }

                })));
            }
        });
        stage.addActor(leaderboardBtn);
    }


    private void setupPlayStoreButton() {
        final int padding = 20;
        playStoreBtn = new ObjektAbstr(new Vector2(0,0), new Vector2(Assets.getWidth()/2 - buttonWidth/2, Assets.getHeight() -buttonWidth * 0.5f), Assets.PLAYSTORE, buttonWidth ) {};
        playStoreBtn.setColor(playstoreColor);
        playStoreBtn.setTouchable(Touchable.enabled);
        playStoreBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.clickSound.play(true);
                playStoreBtn.getActions().clear();
                playStoreBtn.addAction(parallel(scaleTo(2f, 2f, 0.5f, swingOut), moveTo(Assets.getWidth()/2 - buttonWidth/2,  Assets.getHeight() - 200, 0.5f, swingOut)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playStoreBtn.getActions().clear();
                playStoreBtn.addAction(sequence(parallel(scaleTo(1f, 1f, 0.2f, swing), moveTo(Assets.getWidth()/2 - buttonWidth/2, Assets.getHeight() -buttonWidth * 0.5f, 0.2f, swing)), run(new Runnable() {
                    @Override
                    public void run() {
                        game.actionResolver.openPlayStore();
                    }

                })));
            }
        });
        stage.addActor(playStoreBtn);
    }

    private void setupAchievementsButton() {
        achievementsBtn = new ObjektAbstr(new Vector2(0,0), new Vector2(-buttonWidth * 0.25f, Assets.getHeight() - buttonWidth * 0.75f), Assets.GOOGLE_PLAY, buttonWidth ) {};
        achievementsBtn.setColor(achievementsColor);
        achievementsBtn.setTouchable(Touchable.enabled);
        achievementsBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.clickSound.play(true);
                achievementsBtn.getActions().clear();
                achievementsBtn.addAction(parallel(scaleTo(2f, 2f, 0.5f, swingOut), moveTo(60, Assets.getHeight() - 200, 0.5f, swingOut)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                achievementsBtn.getActions().clear();
                achievementsBtn.addAction(sequence(parallel(scaleTo(1f, 1f, 0.2f, swing), moveTo(-buttonWidth * 0.25f, Assets.getHeight() - buttonWidth * 0.75f, 0.2f, swing)), run(new Runnable() {
                    @Override
                    public void run() {
                        game.actionResolver.getAchievementsGPGS();
                    }

                })));
            }
        });
        stage.addActor(achievementsBtn);
    }

    public void setupSettingsButton() {
        settingsBtn = new ObjektAbstr(new Vector2(0,0), new Vector2(-buttonWidth* 0.25f, -buttonWidth* 0.25f), Assets.SETTINGS_ICON, buttonWidth ) {};
        settingsBtn.setColor(settingsColor);
        settingsBtn.addAction(forever(rotateBy(2)));
        settingsBtn.setTouchable(Touchable.enabled);
        settingsBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.clickSound.play(true);
                settingsBtn.addAction(parallel(scaleTo(2f, 2f, 0.5f, swingOut), moveTo(60, 60, 0.5f, swingOut)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                settingsBtn.getActions().clear();
                settingsBtn.addAction(parallel(scaleTo(1f, 1f, 0.2f, swing), moveTo(-buttonWidth* 0.25f, -buttonWidth* 0.25f, 0.2f, swing), run(new Runnable() {
                    @Override
                    public void run() {
                        showSettings(true);
                    }

                })));
                settingsBtn.addAction(forever(rotateBy(2)));
            }
        });
        stage.addActor(settingsBtn);

    }

    public void showTitleTable(boolean show) {
        if(!show) {
            titleTable.addAction(moveTo(0, Assets.getHeight() * -0.07f, 0.4f, Interpolation.swingOut));
        } else {
            titleTable.addAction(moveTo(0, Assets.getHeight() * -0.4f, 0.5f, Interpolation.swingOut));
        }
    }

    public void showSettingsTable(boolean show) {
        if(show) {
            settingsTable.clearActions();
            settingsTable.setVisible(true);
            settingsTable.addAction(parallel(Actions.alpha(0), fadeIn(0.7f)));
            settingsTable.setPosition(0, 0);
            backButton.setVisible(true);
        } else {
            settingsTable.clearActions();
            settingsTable.addAction(parallel(Actions.alpha(1), sequence( parallel( fadeOut(0.3f), moveTo(0, -Assets.getHeight()*2, 0.5f)), run(new Runnable() {
                @Override
                public void run() {
                    settingsTable.setVisible(false);
                }
            }))));
            backButton.setVisible(false);
        }
    }

    private void setupBackButton() {
        backButton = new ObjektAbstr(new Vector2(0,0), new Vector2(Assets.getWidth()/2-70, -200), Assets.BACK_ICON, buttonWidth ) {
            public Vector2 origPos = new Vector2(getX(),getY());
            @Override
            public void setVisible(boolean visible) {
                if(visible) {
                    addAction(moveTo(origPos.x, Assets.getHeight() * -0.01f, 0.5f, swingOut));
                } else {
                    addAction(moveTo(origPos.x, origPos.y, 0.5f, swingIn));
                }
            }
        };

        backButton.setColor(backBtnColor);
        backButton.setTouchable(Touchable.enabled);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                Assets.clickSound.play(true);
                backButton.getActions().clear();
                backButton.addAction(parallel(scaleTo(1.5f, 1.5f, 0.5f, swingOut)));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backButton.getActions().clear();
                backButton.addAction(sequence(parallel(scaleTo(1f, 1f, 0.2f, swing), run(new Runnable() {
                    @Override
                    public void run() {
                        backButton.setVisible(false);
                        showSettings(false);
                    }
                }))));
            }
        });
        stage.addActor(backButton);
    }

    public void showSettings(boolean settings) {
        if(settings) {
            showTitleTable(false);
            showSettingsTable(true);
        } else {
            showTitleTable(true);
            showSettingsTable(false);
        }
    }


    private void setHighscoreLbl() {
        highScore.setText("Highscore: " + String.valueOf(Assets.getHighscore()));
    }

    private void setDeathsLbl() {
        deaths.setText("Deaths: " + String.valueOf(Assets.getDeaths()));
    }

    private void setDistanceText() {
        distance.setText("Total: " + String.valueOf(Assets.getCoveredDistance()));
    }

    private void setQeustionMarkCollectedText() {
        questionMarksCollected.setText("Q's:  " + String.valueOf(Assets.getQuestionMarkCollected()));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
    }
}


