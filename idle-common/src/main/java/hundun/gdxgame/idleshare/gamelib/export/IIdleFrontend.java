package hundun.gdxgame.idleshare.gamelib.export;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;

/**
 * {@link IdleGameplayExport}持有此interface。<br>
 * - 需求游戏引擎实现的一些方法。<br>
 * - 会通过此interface向外部发出某些通知。<br>
 */
public interface IIdleFrontend {
    /**
     * 获取每秒逻辑帧数
     */
    int getLogicFramePerSecond();

    /**
     * 判断当前逻辑帧是否正好是某个整数秒数倍数（用于每N秒触发一次的事件检查）
     */
    boolean modLogicFrameSecondZero(int secondUnit);

    /**
     * 获取当前游戏时长秒数
     */
    float getSecond();

    /**
     * 来自{@link IdleGameplayExport}的通知
     */
    void postConstructionCreate(BaseConstruction construction);
}
