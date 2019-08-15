package com.modestasv.hurdlecircle;

import com.badlogic.gdx.Gdx;
import com.modestasv.hurdlecircle.Interfaces.ActionResolver;

/**
 * Created by Modestas Valauskas on 20.03.2015.
 */
public class DesktopActionResolver implements ActionResolver {


    boolean signedInStateGPGS = true;

    @Override
    public boolean getSignedInGPGS() {
        return signedInStateGPGS;
    }

    @Override
    public void loginGPGS() {
        System.out.println("loginGPGS");
        signedInStateGPGS = true;
    }

    @Override
    public void submitScoreGPGS(int score) {
        System.out.println("submitScoreGPGS " + score);
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {
        System.out.println("unlockAchievement " + achievementId);
    }

    @Override
    public void getLeaderboardGPGS() {
        System.out.println("getLeaderboardGPGS");
    }

    @Override
    public void getAchievementsGPGS() {
        System.out.println("getAchievementsGPGS");
    }

    @Override
    public void showShortToast(CharSequence toastMessage) {
    }

    @Override
    public void showLongToast(CharSequence toastMessage) {
    }

    @Override
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText) {
    }

    @Override
    public void shareHighscore(String message) {
    }

    @Override
    public void showCloseDialog() {
        Gdx.app.exit();
    }
}
