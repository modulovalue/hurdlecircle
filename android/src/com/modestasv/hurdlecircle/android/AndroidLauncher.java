package com.modestasv.hurdlecircle.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.modestasv.hurdlecircle.Assets;
import com.modestasv.hurdlecircle.Game;
import com.modestasv.hurdlecircle.Interfaces.ActionResolver;
import com.modestasv.hurdlecircle.Interfaces.IProxable;



public class AndroidLauncher extends AndroidApplication implements IProxable, SensorEventListener, ActionResolver, GameHelper.GameHelperListener {


    private SensorManager sensorManager;
    private Sensor sensor;

    private float proxValue = 0;

    private boolean hasProximitySensor;
    private GameHelper gameHelper;
    Handler uiThread;
    AndroidApplication appContext;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiThread = new Handler();
        this.appContext = this;

        pDialog = new SweetAlertDialog(appContext, SweetAlertDialog.WARNING_TYPE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        hasProximitySensor = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useWakelock = true;
        initialize(new Game(this, this), cfg);

       if (gameHelper == null) {
           gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
           gameHelper.enableDebugLog(false);
       }
       gameHelper.setup(this);

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==0){
            proxValue = 1;
        }
        else{
            proxValue = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(this);
        gameHelper.onStop();
        super.onStop();
    }

    @Override
    public void onStart(){
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        gameHelper.onActivityResult(request, response, data);
    }

    @Override
    public float getProx() {
        if(hasProximitySensor) {
            return proxValue;
        } else {
            return -1;
        }
    }

   @Override
   public boolean getSignedInGPGS() {
       return gameHelper.isSignedIn();
   }

   @Override
   public void loginGPGS() {
       try {
           runOnUiThread(new Runnable() {
               public void run() {
                   gameHelper.beginUserInitiatedSignIn();
               }
           });
       } catch (final Exception ex) {
       }
   }

   @Override
   public void submitScoreGPGS(int score) {
       Games.Leaderboards.submitScore(gameHelper.getApiClient(),
               Assets.leaderboardHighscore, score);

   }

   @Override
   public void unlockAchievementGPGS(String achievementId) {
       Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);

   }

   @Override
   public void getLeaderboardGPGS() {
       if (gameHelper.isSignedIn()) {
           startActivityForResult(
                   Games.Leaderboards.getLeaderboardIntent(
                           gameHelper.getApiClient(), Assets.leaderboardHighscore),
                   100);
       } else if (!gameHelper.isConnecting()) {
           loginGPGS();
       }

   }

   @Override
   public void getAchievementsGPGS() {
       if (gameHelper.isSignedIn()) {
           startActivityForResult(
                   Games.Achievements.getAchievementsIntent(gameHelper
                           .getApiClient()), 101);
       } else if (!gameHelper.isConnecting()) {
           loginGPGS();
       }

   }

   @Override
   public void onSignInFailed() {
   }

   @Override
   public void onSignInSucceeded() {
   }

    @Override
    public void showShortToast(final CharSequence toastMessage) {
        uiThread.post(new Runnable() {
            public void run() {
                Toast.makeText(appContext, toastMessage, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void showLongToast(final CharSequence toastMessage) {
        uiThread.post(new Runnable() {
            public void run() {
                Toast.makeText(appContext, toastMessage, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void showAlertBox(final String alertBoxTitle,
                             final String alertBoxMessage, final String alertBoxButtonText) {
        uiThread.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(appContext)
                        .setTitle(alertBoxTitle)
                        .setMessage(alertBoxMessage)
                        .setNeutralButton(alertBoxButtonText,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                }).create().show();
            }
        });
    }

    @Override
    public void showCloseDialog() {
        if(pDialog != null && !pDialog.isShowing()) {
            uiThread.post(new Runnable() {
                public void run() {
                    if(pDialog.isShowing()) {
                        pDialog.cancel();
                    }
                    pDialog = new SweetAlertDialog(appContext, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Close?")
                            .setCancelText("No")
                            .setConfirmText("Close")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Gdx.app.exit();
                                }
                            })
                            .show();
                }
            });
        }

    }

    @Override
    public void shareHighscore(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
