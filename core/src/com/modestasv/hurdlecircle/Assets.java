package com.mv.desktop.hurdlecircle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.mv.desktop.hurdlecircle.GameScreen.MVSound;

import java.util.Locale;


/**
 * Assets kümmert sich um
 *      - den Speicherort aller Ressourcen
 *      - einen AssetManager für den sparsamen Zugriff auf Texturen.
 *        der AssetManager managt die Texturen und verhindert doppelte Instanziierungen der Texturen.
 *      - die Speicherung des Highscores und diversen anderen Einstellungen.
 *      - die Spielsounds.
 */
public class Assets extends AssetManager {

    public static final String TITLE_NAME = "Hurdle Circle";

    private static Assets instance;

    public static final String ORBIT_NORMAL       = "data/orbitCircleNorm.png";
    public static final String SPIELER_NORMAL     = "data/player.png";
    public static final String HINDERNISWALL      = "data/hintergrundtest.png";
    public static final String HINDERNIS_NORMAL1  = "data/hindernis1.png";
    public static final String HINDERNIS_NORMAL2  = "data/hindernis2.png";
    public static final String HINDERNIS_BLOCK    = "data/hindernisBlock.png";
    public static final String HINDERNIS_PLUS     = "data/hindernisPlus.png";
    public static final String HINDERNIS_QUESTION = "data/hindernisQuestion.png";

    public static final String HOME_ICON          = "data/icon/home.png";
    public static final String GOOGLE_PLAY        = "data/icon/googleplay.png";
    public static final String SETTINGS_ICON      = "data/icon/cog.png";
    public static final String BACK_ICON          = "data/icon/back.png";
    public static final String PLAY_ICON          = "data/icon/play.png";
    public static final String PAUSE_ICON         = "data/icon/pause.png";
    public static final String RESTART_ICON       = "data/icon/restart.png";
    public static final String SHARE_ICON         = "data/icon/shareButton.png";
    public static final String LEADERBOARD        = "data/icon/leaderboard.png";
    public static final String PLAYSTORE        = "data/icon/playstore.png";

    public Assets() {
        initAssets();
    }

    public static Assets get() {
        if(instance == null) {
            instance = new Assets();
            return instance;
        } else {
            return instance;
        }
    }

    public void initAssets() {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        param.magFilter = Texture.TextureFilter.Nearest;
        param.genMipMaps = true;

        load(ORBIT_NORMAL, Texture.class, param);
        load(SPIELER_NORMAL, Texture.class, param);
        load(HINDERNISWALL, Texture.class, param);
        load(HINDERNIS_NORMAL1, Texture.class, param);
        load(HINDERNIS_NORMAL2, Texture.class, param);
        load(HINDERNIS_BLOCK, Texture.class, param);
        load(HINDERNIS_PLUS, Texture.class, param);
        load(HINDERNIS_QUESTION, Texture.class, param);
        load(HOME_ICON, Texture.class, param);
        load(SETTINGS_ICON, Texture.class, param);
        load(BACK_ICON, Texture.class, param);
        load(PLAY_ICON, Texture.class, param);
        load(PAUSE_ICON, Texture.class, param);
        load(RESTART_ICON, Texture.class, param);
        load(SHARE_ICON, Texture.class, param);
        load(GOOGLE_PLAY, Texture.class, param);
        load(LEADERBOARD, Texture.class, param);
        load(PLAYSTORE, Texture.class, param);

        mainMusic.setLooping(true);

    }

    public Texture getTexture(String res){
        if(isLoaded(res)) {
            return get( res, Texture.class);
        } else {
            Gdx.app.error("Error", "ERROR LOADING RES");
            Gdx.app.exit();
            return null;
        }
    }

    private static final String PREF_NAME = "StoragePreferencesHurdleCircle";
    private static final String PREFNAME_PROXIMITY = "ProximityAllow";
    private static final String PREFNAME_HIGHSCORE = "Highscore";
    private static final String PREFNAME_VIBRATEALLOW = "Vibrate";
    private static final String PREFNAME_SOUNDALLOW = "SoundAllow";
    private static final String PREFNAME_DEATHS = "Deaths";
    private static final String PREFNAME_COVEREDDISTANCE = "CoveredDistance";

    private static final String PREFNAME_QUESTIONMARKSCOLLECTED = "QuestionMarksCollected";

    private static final String PREFNAME_FIRSTTIMERUN = "FirstTimeRun";


    private static Preferences prefs = Gdx.app.getPreferences(PREF_NAME);

    public static boolean getProximityBool() {
        return prefs.getBoolean(PREFNAME_PROXIMITY, true);
    }
    public static void putProxBool( boolean bool) {
        prefs.putBoolean(PREFNAME_PROXIMITY, bool);
        prefs.flush();
    }

    public static boolean getFirstTimeRunBool() {
        return prefs.getBoolean(PREFNAME_FIRSTTIMERUN, false);
    }
    public static void putFirstTimeRunBool( boolean bool) {
        prefs.putBoolean(PREFNAME_FIRSTTIMERUN, bool);
        prefs.flush();
    }

    public static int getHighscore() {
        return prefs.getInteger(PREFNAME_HIGHSCORE, 0);
    }
    public static void putHighscore(int highscore) {
        prefs.putInteger(PREFNAME_HIGHSCORE, highscore);
        prefs.flush();
    }

    public static boolean getVibrate() {
        return prefs.getBoolean(PREFNAME_VIBRATEALLOW, true);
    }
    public static void putVibrate(boolean vibrate) {
        prefs.putBoolean(PREFNAME_VIBRATEALLOW, vibrate);
        prefs.flush();
    }

    public static boolean getSoundAllow() { return prefs.getBoolean(PREFNAME_SOUNDALLOW, false); }
    public static void putSoundAllow(boolean sound) {
        prefs.putBoolean(PREFNAME_SOUNDALLOW, sound);
        prefs.flush();
    }

    public static int getDeaths() { return prefs.getInteger(PREFNAME_DEATHS, 0); }
    public static void incrementDeaths() {
        prefs.putInteger(PREFNAME_DEATHS, getDeaths()+1);
        prefs.flush();
    }

    public static int getCoveredDistance() { return prefs.getInteger(PREFNAME_COVEREDDISTANCE, 0); }
    public static void addToCoveredDistance(int addToCoveredDistance) {
        prefs.putInteger(PREFNAME_COVEREDDISTANCE, getCoveredDistance() + addToCoveredDistance);
        prefs.flush();
    }


    public static int getQuestionMarkCollected() { return prefs.getInteger(PREFNAME_QUESTIONMARKSCOLLECTED, 0); }
    public static void putQuestionsMarkCollected() {
        prefs.putInteger(PREFNAME_QUESTIONMARKSCOLLECTED, getQuestionMarkCollected() + 1);
        prefs.flush();
    }


    private static final String MUSIC_SOUND = "sound/ostttl.ogg";
    private static Music mainMusic = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_SOUND));

    public static void playMainMusic() {
        if(getSoundAllow()) {
            mainMusic.play();
        }
    }

    public static void pauseMainMusic() {
        mainMusic.pause();
    }

    public static float LEISE = 0.4f;
    public static float NORMAL= 0.8f;

    public static void setMainMusicVolume(float volume) {
       mainMusic.setVolume(volume);
    }

    private static final String SUCCESS_SOUND = "sound/success.ogg";
    public static final MVSound successSound = new MVSound(SUCCESS_SOUND);

    private static final String CLICK_SOUND = "sound/click.ogg";
    public static final MVSound clickSound = new MVSound(CLICK_SOUND);


    public static final float GAME_WORLD_HEIGHT = 800;

    public static float getHeight() {
        return  GAME_WORLD_HEIGHT;
    }

    public static float getWidth() {
        return getHeight() *  (float) Gdx.graphics.getWidth() / (float)  Gdx.graphics.getHeight();
    }


    public static String achievement25Points = "CgkI7K24_IUCEAIQAQ";
    public static String achievement50Points = "CgkI7K24_IUCEAIQAg";
    public static String achievement100Points = "CgkI7K24_IUCEAIQAw";
    public static String achievement200Points = "CgkI7K24_IUCEAIQBA";
    public static String achievement500Points = "CgkI7K24_IUCEAIQBQ";
    public static String achievement1000Points = "CgkI7K24_IUCEAIQBg";
    public static String achievement2000Points = "CgkI7K24_IUCEAIQBw";
    public static String achievement5000Points ="CgkI7K24_IUCEAIQCA";
    public static String achievement100Qs = "CgkI7K24_IUCEAIQCw";
    public static String achievement500Miles = "CgkI7K24_IUCEAIQDA";
    public static String leaderboardHighscore = "CgkI7K24_IUCEAIQAA";

    public static String getHighscoreMessage(int points) {

        if(Locale.getDefault().getLanguage().equals("de")) {
            return "Ich habe " + points + " Punkte erreicht. Kannst du mich schlagen? Spiele Hurdle Circle kostenlos: https://play.google.com/store/apps/details?id=com.modestasv.hurdlecircle.android";
        } else {
            return "My score: " + points + " can you beat me at Hurdle Circle? Play for free: https://play.google.com/store/apps/details?id=com.modestasv.hurdlecircle.android";
        }

    }

}
