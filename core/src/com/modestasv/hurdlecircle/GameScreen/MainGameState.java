package com.mv.desktop.hurdlecircle.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mv.desktop.hurdlecircle.GameScreen.Camera.CameraState;
import com.modestasv.hurdlecircle.Assets;
import com.mv.desktop.hurdlecircle.GameScreen.ScreenObjects.Spieler.SpielerState;

/**
 *  PLAYING:
 *      Alles wird Aktualisiert
 *  PAUSE:
 *      nur die Kamera wird Aktualisiert
 *  RESET:
 *      das Spiel wird zurückgesetzt
 *  GAMEOVER:
 *      der Spieler hat Verloren.
 *      Objekte werden informiert.
 */

public enum MainGameState implements State<GameModel> {

    PLAYING {
        @Override
        public void update(GameModel en) {
            en.gameController.update(Gdx.graphics.getDeltaTime(), false);
        }
    },

    PAUSE {
        @Override
        public void update(GameModel en) {
        }
    },

    RESET {
        @Override
        public void update(GameModel en) {
            en.resetMainGameScreen();

        }
    },

    GAMEOVER() {
        @Override
        public void enter(GameModel en) {

        }

        @Override
        public void update(GameModel en) {
            en.spieler.stateMachine.changeState(SpielerState.GAMEOVER);
            en.gameController.update(Gdx.graphics.getDeltaTime(), true);
        }
    };

    @Override
    public void enter(GameModel en) {
    }
    @Override
    public void update(GameModel entity) {

    }
    @Override
    public void exit(GameModel entity) {
    }
    @Override
    public boolean onMessage(GameModel entity, Telegram telegram) {
        return false;
    }
}
