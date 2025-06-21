package hundun.gdxgame.idlemushroom.logic.loader;

import hundun.gdxgame.idlemushroom.logic.IdleMushroomGameDictionary.LanguageCode;
import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend.IDescriptionPackageFactory;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.IdleMushroomExtraGameplayManager;
import hundun.gdxgame.idlemushroom.logic.IdleMushroomGameDictionary;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomAchievementId;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.id.ResourceType;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.AbstractAchievementPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.IAchievementPrototypeLoader;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.OwnConstructionAchievementPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.resource.ResourcePair;


import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdleMushroomAchievementLoader implements IAchievementPrototypeLoader {

    public static Map<String, Integer> achievementSortWeightMap = new HashMap<>();
    public static Map<String, Integer> achievementExtraArgMap = new HashMap<>();

    public static final String EXTRA_ARG_0 = "ExtraArg0";
    public static final String EXTRA_ARG_1 = "ExtraArg1";
    public static final String EXTRA_ARG_2 = "ExtraArg2";
    public static final String EXTRA_ARG_3 = "ExtraArg3";
    static {
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_1, 1);
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_2, 2);
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_3, 3);
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_4, 4);
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_5, 5);
        achievementSortWeightMap.put(IdleMushroomAchievementId.STEP_6, 6);

        achievementExtraArgMap.put(EXTRA_ARG_0, 2);
        achievementExtraArgMap.put(EXTRA_ARG_1, 4);
        achievementExtraArgMap.put(EXTRA_ARG_2, 8);
        achievementExtraArgMap.put(EXTRA_ARG_3, IdleMushroomExtraGameplayManager.EPOCH_COUNTER_MAX_LEVEL);
    }




    IdleMushroomGameDictionary idleMushroomGameDictionary;
    public IdleMushroomAchievementLoader(IdleMushroomGameDictionary idleMushroomGameDictionary) {
        this.idleMushroomGameDictionary = idleMushroomGameDictionary;
    }

    @Override
    public Map<String, AbstractAchievementPrototype> getProviderMap(IDescriptionPackageFactory descriptionPackageFactory) {
        Map<String, List<String>> textMap = new HashMap<>();

        Map<String, AbstractAchievementPrototype> map = new HashMap<>();
        final String EPOCH_ANY_MUSHROOM_AUTO_PROVIDER = IdleMushroomConstructionPrototypeId.EPOCH_1_MUSHROOM_AUTO_PROVIDER
                + "|" + IdleMushroomConstructionPrototypeId.EPOCH_2_MUSHROOM_AUTO_PROVIDER
                + "|" + IdleMushroomConstructionPrototypeId.EPOCH_3_MUSHROOM_AUTO_PROVIDER ;;
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
                map,
                IdleMushroomAchievementId.STEP_1,
                descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_1),
                JavaFeatureForGwt.mapOf(
                        EPOCH_ANY_MUSHROOM_AUTO_PROVIDER, new SimpleEntry<>(2, 1)
                ),
                JavaFeatureForGwt.listOf(IdleMushroomAchievementId.STEP_2),
                new ResourcePair(ResourceType.MUSHROOM, 50L)
        );
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
            map,
            IdleMushroomAchievementId.STEP_2,
            descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_2),
            JavaFeatureForGwt.mapOf(
                    IdleMushroomConstructionPrototypeId.EPOCH_COUNTER, new SimpleEntry<>(1, achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_0))
            ),
            JavaFeatureForGwt.listOf(IdleMushroomAchievementId.STEP_3),
            new ResourcePair(ResourceType.MUSHROOM, 200L)
        );
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
            map,
            IdleMushroomAchievementId.STEP_3,
            descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_3),
            JavaFeatureForGwt.mapOf(
                    EPOCH_ANY_MUSHROOM_AUTO_PROVIDER, new SimpleEntry<>(2, 2)
            ),
            JavaFeatureForGwt.listOf(IdleMushroomAchievementId.STEP_4),
            new ResourcePair(ResourceType.MUSHROOM, 400L)
        );
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
            map,
            IdleMushroomAchievementId.STEP_4,
            descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_4),
            JavaFeatureForGwt.mapOf(
                    IdleMushroomConstructionPrototypeId.EPOCH_COUNTER, new SimpleEntry<>(1, achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_1))
            ),
            JavaFeatureForGwt.listOf(IdleMushroomAchievementId.STEP_5),
            new ResourcePair(ResourceType.MUSHROOM, 1600L)
        );
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
            map,
            IdleMushroomAchievementId.STEP_5,
            descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_5),
            JavaFeatureForGwt.mapOf(
                    IdleMushroomConstructionPrototypeId.EPOCH_COUNTER, new SimpleEntry<>(1, achievementExtraArgMap.get(IdleMushroomAchievementLoader.EXTRA_ARG_2))
            ),
            JavaFeatureForGwt.listOf(IdleMushroomAchievementId.STEP_6),
            new ResourcePair(ResourceType.MUSHROOM, 6400L)
        );
        OwnConstructionAchievementPrototype.Companion.quickAddOwnConstructionAchievement(
            map,
            IdleMushroomAchievementId.STEP_6,
            descriptionPackageFactory.getAchievementDescriptionPackage(IdleMushroomAchievementId.STEP_6),
            JavaFeatureForGwt.mapOf(
                    IdleMushroomConstructionPrototypeId.EPOCH_COUNTER, new SimpleEntry<>(1, IdleMushroomExtraGameplayManager.EPOCH_COUNTER_MAX_LEVEL)
            ),
            null,
            new ResourcePair(ResourceType.MUSHROOM, 1000000L)
        );
        return map;
    }
}
