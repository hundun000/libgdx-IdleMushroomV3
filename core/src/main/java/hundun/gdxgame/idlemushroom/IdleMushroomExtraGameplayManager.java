package hundun.gdxgame.idlemushroom;

import com.badlogic.gdx.utils.Null;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomAchievementLoader;
import hundun.gdxgame.idleshare.gamelib.export.IdleGameplayExport;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.libv3.gamelib.starter.listerner.IGameStartListener;
import hundun.gdxgame.idlemushroom.IdleMushroomGame.BuffEpochConfig;
import hundun.gdxgame.idlemushroom.IdleMushroomGame.ConstructionEpochConfig;
import hundun.gdxgame.idlemushroom.IdleMushroomGame.RootEpochConfig;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomBuffId;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idleshare.core.framework.HundunIdleFrontend;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一方面，拓展一些Gameplay机制，封装好，提供给{@link IdleMushroomGame}；
 * 另一方面，作为{@link HundunIdleFrontend}的实现，被{@link IdleGameplayExport}使用;
 */
public class IdleMushroomExtraGameplayManager extends HundunIdleFrontend implements IGameStartListener {
    IdleMushroomGame idleMushroomGame;
    private final Map<Integer, RootEpochConfig> epochConfigMap;
    public static final int EPOCH_COUNTER_MAX_LEVEL = 20;
    private final Map<String, Integer> specialEpochConfigMaxLevel = JavaFeatureForGwt.mapOf(
            IdleMushroomConstructionPrototypeId.MAIN_MUSHROOM, Integer.MAX_VALUE,
            IdleMushroomConstructionPrototypeId.MUSHROOM_AUTO_SELLER, 999,
            IdleMushroomConstructionPrototypeId.EPOCH_COUNTER, EPOCH_COUNTER_MAX_LEVEL
    );
    DescriptionPackageFactory descriptionPackageFactory;

    @Getter
    RootEpochConfig currentRootEpochConfig;
    @Getter
    @Null
    RootEpochConfig nextRootEpochConfig;
    // for quick ref
    @Getter
    BaseConstruction epochCounterConstruction;
    // for quick ref
    @Getter
    BaseConstruction mainClickerConstruction;
    // for quick ref
    @Getter
    BaseConstruction autoSellerConstruction;
    IdleMushroomExtraGameplayManager(IdleMushroomGame idleMushroomGame) {
        super(idleMushroomGame);
        this.idleMushroomGame = idleMushroomGame;
        this.epochConfigMap = new HashMap<>();
        this.descriptionPackageFactory = new DescriptionPackageFactory(idleMushroomGame);
        int EPOCH_COUNTER_SPECIAL_LEVEL_0 = IdleMushroomAchievementLoader.achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_0);
        int EPOCH_COUNTER_SPECIAL_LEVEL_1 = IdleMushroomAchievementLoader.achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_1);
        int EPOCH_COUNTER_SPECIAL_LEVEL_2 = IdleMushroomAchievementLoader.achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_2);
        for (int i = 1; i < EPOCH_COUNTER_SPECIAL_LEVEL_1; i++) {
            epochConfigMap.put(
                    i, RootEpochConfig.builder()
                            .enlargementLevel(1)
                            .build()
            );
        }
        epochConfigMap.put(
                EPOCH_COUNTER_SPECIAL_LEVEL_1, RootEpochConfig.builder()
                        .enlargementLevel(2)
                        .constructionEpochConfigMap(JavaFeatureForGwt.mapOf(
                                IdleMushroomConstructionPrototypeId.EPOCH_1_MUSHROOM_AUTO_PROVIDER,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_2_MUSHROOM_AUTO_PROVIDER)
                                        .build(),
                                IdleMushroomConstructionPrototypeId.EPOCH_1_EMPTY_CELL,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_2_EMPTY_CELL)
                                        .build(),
                                IdleMushroomConstructionPrototypeId.EPOCH_1_TREE,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_2_TREE)
                                        .build()
                        ))
                        .build()
        );
        for (int i = EPOCH_COUNTER_SPECIAL_LEVEL_1 + 1; i < EPOCH_COUNTER_SPECIAL_LEVEL_2; i++) {
            epochConfigMap.put(
                    i, RootEpochConfig.builder()
                            .enlargementLevel(2)
                            .build()
            );
        }
        epochConfigMap.put(
                EPOCH_COUNTER_SPECIAL_LEVEL_2, RootEpochConfig.builder()
                        .enlargementLevel(3)
                        .constructionEpochConfigMap(JavaFeatureForGwt.mapOf(
                                IdleMushroomConstructionPrototypeId.EPOCH_2_MUSHROOM_AUTO_PROVIDER,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_3_MUSHROOM_AUTO_PROVIDER)
                                        .build(),
                                IdleMushroomConstructionPrototypeId.EPOCH_2_EMPTY_CELL,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_3_EMPTY_CELL)
                                        .build(),
                                IdleMushroomConstructionPrototypeId.EPOCH_2_TREE,
                                ConstructionEpochConfig.builder()
                                        .transformToPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_3_TREE)
                                        .build()
                        ))
                        .build()
        );
        for (int i = EPOCH_COUNTER_SPECIAL_LEVEL_2 + 1; i <= EPOCH_COUNTER_MAX_LEVEL; i++) {
            epochConfigMap.put(
                    i, RootEpochConfig.builder()
                            .enlargementLevel(3)
                            .build()
            );
        }
        epochConfigMap.forEach((k, v) -> {
            v.setMaxLevel(Math.max(1, 2 * (k - 1)));
            v.setBuffEpochConfigMap(JavaFeatureForGwt.mapOf(
                    IdleMushroomBuffId.BUFF_MUSHROOM_OUTPUT_SCALE,
                    BuffEpochConfig.builder()
                            .buffLevel(k - 1)
                            .build()
            ));
        });
    }


    public void doChangeEpoch(int currentEpochLevel) {
        @Null RootEpochConfig lastRootEpochConfig = epochConfigMap.get(currentEpochLevel - 1);
        this.currentRootEpochConfig = epochConfigMap.get(currentEpochLevel);
        this.nextRootEpochConfig = epochConfigMap.get(currentEpochLevel + 1);

        idleMushroomGame.getScreenContext().getMainPlayScreen().setMainClickerWithScale();
        // ------ UpdateBuff ------
        Map<String, Integer> deltaMap = currentRootEpochConfig.buffEpochConfigMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                it -> it.getKey(),
                                it -> {
                                    if (lastRootEpochConfig == null) {
                                        return it.getValue().getBuffLevel();
                                    } else {
                                        BuffEpochConfig lastConfig = lastRootEpochConfig.getBuffEpochConfigMap().get(it.getKey());
                                        if (lastConfig == null) {
                                            return it.getValue().getBuffLevel();
                                        } else {
                                            return it.getValue().getBuffLevel() - lastConfig.getBuffLevel();
                                        }
                                    }
                                }));
        deltaMap.entrySet().removeIf(it -> it.getValue() == 0);
        if (!deltaMap.isEmpty()) {
            idleMushroomGame.getIdleGameplayExport().getGameplayContext().getBuffManager().modifyBuffLevel(deltaMap);
        }
        // ------ UpdateMaxLevel ------
        boolean needUpdateMaxLevel = lastRootEpochConfig == null || lastRootEpochConfig.getMaxLevel() != currentRootEpochConfig.getMaxLevel();
        if (needUpdateMaxLevel) {
            idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager().getWorldConstructionInstances().stream()
                    .forEach(it -> {
                        handleEpochConfigMaxLevel(it);
                    });
        }
        // ------ UpdateEveryConstruction ------
        boolean needUpdateEveryConstruction =
                (lastRootEpochConfig == null
                        || lastRootEpochConfig.getEnlargementLevel() != currentRootEpochConfig.getEnlargementLevel())
                && currentRootEpochConfig.getConstructionEpochConfigMap() != null;
        if (needUpdateEveryConstruction) {
            idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager().getWorldConstructionInstances().stream()
                    .forEach(it -> {
                        ConstructionEpochConfig constructionEpochConfig = currentRootEpochConfig.getConstructionEpochConfigMap()
                                .get(it.getPrototypeId());
                        if (constructionEpochConfig != null) {
                            idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager().addToRemoveQueue(it);
                            if (constructionEpochConfig.getTransformToPrototypeId() != null) {
                                idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager().addToCreateQueue(
                                        constructionEpochConfig.getTransformToPrototypeId(),
                                        it.getPosition(),
                                        it.getSaveData()
                                );
                            }
                        }
                    });
        }
        // ------ UpdateSingleton ------
        while (mainClickerConstruction.getSaveData().getLevel() < currentRootEpochConfig.getEnlargementLevel()) {
            mainClickerConstruction.getUpgradeComponent().doUpgrade();
        }

    }

    public void lazyInitOnGameCreateBody() {
        idleMushroomGame.getIdleGameplayExport().getGameplayContext().getEventManager().registerListener(this);

    }

    @Override
    public void onGameStart() {
        this.epochCounterConstruction = idleMushroomGame.getIdleGameplayExport().getGameplayContext()
                .getConstructionManager()
                .getSingletonConstructionInstancesOrEmpty()
                .stream()
                .filter(it -> it.getPrototypeId().equals(IdleMushroomConstructionPrototypeId.EPOCH_COUNTER))
                .findAny()
                .orElseThrow(() -> new RuntimeException("bad onGameStart"));
        this.mainClickerConstruction = idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager()
                .getSingletonConstructionInstancesOrEmpty()
                .stream()
                .filter(it -> it.getPrototypeId().equals(IdleMushroomConstructionPrototypeId.MAIN_MUSHROOM))
                .findAny()
                .orElseThrow(() -> new RuntimeException("bad onGameStart"));
        this.autoSellerConstruction = idleMushroomGame.getIdleGameplayExport().getGameplayContext().getConstructionManager()
                .getSingletonConstructionInstancesOrEmpty()
                .stream()
                .filter(it -> it.getPrototypeId().equals(IdleMushroomConstructionPrototypeId.MUSHROOM_AUTO_SELLER))
                .findAny()
                .orElseThrow(() -> new RuntimeException("bad onGameStart"));
        doChangeEpoch(epochCounterConstruction.getSaveData().getLevel());
    }

    @Override
    public void postConstructionCreate(BaseConstruction construction) {
        handleEpochConfigMaxLevel(construction);
        construction.updateModifiedValues();
    }

    @Override
    public IDescriptionPackageFactory getDescriptionPackageFactory() {
        return descriptionPackageFactory;
    }


    private void handleEpochConfigMaxLevel(BaseConstruction construction) {
        if (specialEpochConfigMaxLevel.containsKey(construction.getPrototypeId())) {
            construction.getLevelComponent().maxLevel = specialEpochConfigMaxLevel.get(construction.getPrototypeId());
        } else {
            construction.getLevelComponent().maxLevel = currentRootEpochConfig.getMaxLevel();
        }
        construction.updateModifiedValues();
    }
}
