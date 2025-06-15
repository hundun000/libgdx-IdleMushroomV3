package hundun.gdxgame.idlemushroom.logic;

import com.badlogic.gdx.Gdx;
import hundun.gdxgame.idleshare.gamelib.framework.model.history.HistoryManager.ConstructionSituation;
import hundun.gdxgame.idleshare.gamelib.framework.model.history.HistoryManager.ProxyGameSituationDTO;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.IdleMushroomGame;
import hundun.gdxgame.idleshare.gamelib.framework.model.history.HistoryManager.ProxyActionType;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理所有自动进行的事：<br>
 * - 开发期间加快游戏时自动升级；（未来可能改为游戏机制玩家可托管？）<br>
 * - 自动保存<br>
 */
public class ProxyManager {

    IdleMushroomGame game;
    private final IProxyManagerCallback proxyManagerCallback;



    ProxyConfig config;
    @Setter
    @Getter
    ProxyState proxyState;
    public enum ProxyState {
        /**
         * 进行自动工作（建筑购买、升级）
         */
        RUNNING,
        /**
         * 不再进行自动工作（建筑购买、升级）。自动保存不受影响。
         */
        PAUSE,
        /**
         * 下次逻辑帧将调用结束进程
         */
        STOP,
    }

    /**
     * 某些方法不预设其实现者
     */
    public interface IProxyManagerCallback {
        /**
         * 在自动停止进程结束前调用，可插入一些收尾工作
         */
        default void onProxyCauseExit(IdleMushroomGame game) {};
    }

    public ProxyManager(IdleMushroomGame game, IProxyManagerCallback proxyManagerCallback) {
        this.game = game;
        this.proxyManagerCallback = proxyManagerCallback;
    }

    public void lazyInit(ProxyConfig config) {
        this.config = config;
        this.proxyState = config.starterProxyState;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProxyConfig {
        /**
         * 满足该秒数后，运行状态自动切换到STOP
         */
        Integer stopConditionSecondCount;
        /**
         * 满足该建筑等级后，运行状态自动切换到STOP
         */
        Map<String, Integer> stopConditionConstructionLevelMap;
        /**
         * 自动保存间隔
         */
        Integer autoSaveDeltaSecond;
        /**
         * 游戏开始时的状态
         */
        ProxyState starterProxyState;

        public static ProxyConfig devInstance() {
            return ProxyConfig.builder()
                    .stopConditionSecondCount(null)
                    .stopConditionConstructionLevelMap(JavaFeatureForGwt.mapOf(
                            IdleMushroomConstructionPrototypeId.EPOCH_COUNTER,
                            5
                    ))
                    .autoSaveDeltaSecond(null)
                    .starterProxyState(ProxyState.RUNNING)
                    .build();
        }

        public static ProxyConfig releaseInstance() {
            return ProxyConfig.builder()
                    .autoSaveDeltaSecond(10)
                    .starterProxyState(ProxyState.PAUSE)
                    .build();
        }
    }


    /**
     * （每个游戏帧检查一次）满足条件则自动做事；
     */
    public void tryAutoAction() {
        // 并不需要每个游戏帧检查一次，此处设为每整数秒执行一次；
        if (!game.getIdleGameplayExport().getGameplayContext().getIdleFrontend().modLogicFrameSecondZero(1))
        {
            return;
        }
        // try buyInstanceOfPrototype
        game.getIdleGameplayExport().getGameplayContext().getConstructionManager().getWorldConstructionInstances().stream()
                .filter(model -> model.getExistenceComponent().getBuyCandidateConfigs() != null)
                .forEach(model -> {
                    model.getExistenceComponent().getBuyCandidateConfigs().stream()
                            .filter(constructionBuyCandidateConfig -> {
                                boolean canBuyInstanceOfPrototype = game.getIdleGameplayExport().getGameplayContext()
                                        .getConstructionManager()
                                        .canBuyInstanceOfPrototype(constructionBuyCandidateConfig, model.getPosition());
                                return canBuyInstanceOfPrototype;
                            })
                            .findFirst()
                            .ifPresent(constructionBuyCandidateConfig -> {
                                game.getIdleGameplayExport().getGameplayContext().getHistoryManager().addProxyRunRecord(
                                        ProxyActionType.buyInstanceOfPrototype,
                                        constructionBuyCandidateConfig.getPrototypeId(),
                                        model.getPosition()
                                );
                                game.getIdleGameplayExport().getGameplayContext()
                                        .getConstructionManager()
                                        .buyInstanceOfPrototype(constructionBuyCandidateConfig, model.getPosition());
                            });
                });
        // try doUpgrade World
        game.getIdleGameplayExport().getGameplayContext().getConstructionManager().getWorldConstructionInstances().stream()
                .filter(model -> model.getUpgradeComponent().canUpgrade())
                .forEach(model -> {
                    proxyDoUpgrade(model);
                });
        // ------- try autoSeller ------
        final BaseConstruction autoSellerConstruction = game.getIdleMushroomExtraGameplayExport().getAutoSellerConstruction();
        if (autoSellerConstruction.getUpgradeComponent().canUpgrade()) {
            proxyDoUpgrade(autoSellerConstruction);
        }
        proxyTryWorkingLevel(autoSellerConstruction);
        // ------- try epoch ------
        final BaseConstruction epochCounterConstruction = game.getIdleMushroomExtraGameplayExport().getEpochCounterConstruction();
        if (epochCounterConstruction.getUpgradeComponent().canUpgrade()) {
            proxyDoUpgrade(epochCounterConstruction);
        }
    }

    private void proxyTryWorkingLevel(BaseConstruction model) {
        int beforeLevel = model.getSaveData().getWorkingLevel();
        // firstly, set max workingLevel
        while (model.getLevelComponent().canChangeWorkingLevel(1)) {
            model.getLevelComponent().changeWorkingLevel(1);
        }
        // secondly, minus until can output
        while (!model.getOutputComponent().canOutput() && model.getLevelComponent().canChangeWorkingLevel(-1)) {
            model.getLevelComponent().changeWorkingLevel(-1);
        }
        int afterLevel = model.getSaveData().getWorkingLevel();
        if (beforeLevel != afterLevel) {
            game.getIdleGameplayExport().getGameplayContext().getHistoryManager().addProxyRunRecord(
                    ProxyActionType.changeWorkingLevel,
                    model.getPrototypeId(),
                    "beforeLevel=" + beforeLevel,
                    "afterLevel=" + afterLevel
            );
        }
    }

    private void proxyDoUpgrade(BaseConstruction model) {

        int beforeLevel = model.getSaveData().getLevel();
        model.getUpgradeComponent().doUpgrade();
        if (model.getPrototypeId().equals(IdleMushroomConstructionPrototypeId.EPOCH_COUNTER)) {
            game.getIdleMushroomExtraGameplayExport().doChangeEpoch(model.getSaveData().getLevel());
        }
        int afterLevel = model.getSaveData().getLevel();
        game.getIdleGameplayExport().getGameplayContext().getHistoryManager().addProxyRunRecord(
                ProxyActionType.doUpgrade,
                model.getPrototypeId(),
                model.getPosition(),
                "beforeLevel=" + beforeLevel,
                "afterLevel=" + afterLevel
        );
    }

    private boolean checkStopCondition() {
        if (config.stopConditionSecondCount != null && game.getLogicFrameHelper().getClockCount() > game.getLogicFrameHelper().secondToFrameNum(config.getStopConditionSecondCount())) {
            return true;
        }
        if (config.stopConditionConstructionLevelMap != null) {
            for (String targetPrototypeId : config.stopConditionConstructionLevelMap.keySet()) {
                final int targetLevel = config.stopConditionConstructionLevelMap.get(targetPrototypeId);
                List<BaseConstruction> constructions = game.getIdleGameplayExport().getGameplayContext().getConstructionManager().getAllConstructionInstances().stream()
                        .filter(it -> it.getPrototypeId().equals(targetPrototypeId))
                        .collect(Collectors.toList());
                for (BaseConstruction construction : constructions) {
                    if (construction.getSaveData().getLevel() >= targetLevel) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ProxyGameSituationDTO rootSaveDataToSituation(RootSaveData rootSaveData) {
        if (rootSaveData == null) {
            return null;
        }
        List<ConstructionSituation> constructionSituations = rootSaveData.getGameplaySave().getConstructionSaveDataMap().values().stream()
            .filter(it -> it.getLevel() > 0)
            .map(it -> ConstructionSituation.builder()
                .prototypeId(it.prototypeId)
                .level(it.getLevel())
                .count(1)
                .build()
            )
            .collect(Collectors.toList());
        List<ConstructionSituation> countedConstructionSituations = new ArrayList<>();
        constructionSituations.forEach(it -> {
            ConstructionSituation counted = countedConstructionSituations.stream()
                .filter(itt -> itt.getPrototypeId().equals(it.getPrototypeId()) && itt.getLevel() == it.getLevel())
                .findAny()
                .orElse(null);
            if (counted == null) {
                countedConstructionSituations.add(it);
            } else {
                counted.setCount(counted.getCount() + it.getCount());
            }
        });

        return ProxyGameSituationDTO.builder()
            .ownResources(rootSaveData.getGameplaySave().getOwnResources())
            .constructionSituations(countedConstructionSituations)
            .build();
    }

    public void onLogicFrame() {

        if (config.getAutoSaveDeltaSecond() != null) {
            if (game.getLogicFrameHelper().getClockCount() % game.getLogicFrameHelper().secondToFrameNum(config.getAutoSaveDeltaSecond()) == 0)
            {
                RootSaveData lastSaveCurrentResult = game.getSaveHandler().gameSaveCurrent();
                ProxyGameSituationDTO gameSituationDTO = rootSaveDataToSituation(lastSaveCurrentResult);
                game.getIdleGameplayExport().getGameplayContext().getHistoryManager().addProxyRunRecordTypeLogSaveCurrentResult(gameSituationDTO);
            }
        }
        if (checkStopCondition()) {
            proxyState = ProxyState.STOP;
        }

        if (proxyState == ProxyState.RUNNING) {
            tryAutoAction();
        } else if (proxyState == ProxyState.STOP) {
            game.getIdleGameplayExport().getGameplayContext().getHistoryManager().addProxyRunRecord(
                    ProxyActionType.proxyCauseExit
            );
            proxyManagerCallback.onProxyCauseExit(game);
            Gdx.app.exit();
        }

    }
}
