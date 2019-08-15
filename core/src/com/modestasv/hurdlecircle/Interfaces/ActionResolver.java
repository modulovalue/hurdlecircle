package com.mv.desktop.hurdlecircle.Interfaces;

/**
 * Created by Modestas Valauskas on 20.03.2015.
 */
public interface ActionResolver {
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();
    public void openPlayStore();

    public void showShortToast(final CharSequence toastMessage);
    public void showLongToast(final CharSequence toastMessage);
    public void showAlertBox(final String alertBoxTitle, final String alertBoxMessage, final String alertBoxButtonText);
    public void shareHighscore(final String message);
    public void showCloseDialog();

}