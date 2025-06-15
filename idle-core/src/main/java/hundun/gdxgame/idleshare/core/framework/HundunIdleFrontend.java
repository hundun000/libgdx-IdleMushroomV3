package hundun.gdxgame.idleshare.core.framework;

import hundun.gdxgame.libv3.corelib.base.BaseHundunGame;
import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend;

/**
 * 基于{@link BaseHundunGame}实现{@link IIdleFrontend}的需求；
 */
public abstract class HundunIdleFrontend implements IIdleFrontend {
    BaseHundunGame<?> game;
    public HundunIdleFrontend(BaseHundunGame<?> game) {
        this.game = game;
    }

    @Override
    public int getLogicFramePerSecond() {
        return game.getLogicFrameHelper().secondToFrameNum(1.0f);
    }

    @Override
    public boolean modLogicFrameSecondZero(int mod) {
        return game.getLogicFrameHelper().getClockCount() % game.getLogicFrameHelper().secondToFrameNum(mod) == 0;
    }

    @Override
    public float getSecond() {
        return game.getLogicFrameHelper().getClockCount() * 1.0f / game.getLogicFrameHelper().secondToFrameNum(1);
    }
}
