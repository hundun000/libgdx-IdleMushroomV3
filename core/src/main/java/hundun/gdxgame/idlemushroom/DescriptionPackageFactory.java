package hundun.gdxgame.idlemushroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Json;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomAchievementLoader;
import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend.IDescriptionPackageFactory;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.AchievementDescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage.LevelDescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage.ProficiencyDescriptionPackage;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;

import java.util.*;

public class DescriptionPackageFactory implements IDescriptionPackageFactory {
    static final String I18N_COPY_KEY = ".COPY";
    static final String I18N_VALUE_LIST_SPLIT = ";;";

    static DescriptionPackage DEFAULT_DescriptionPackage = DescriptionPackage.builder()
        .name("TODO.蘑菇地块")
        .wikiText("TODO.介绍")
        .upgradeButtonText("TODO.升级")
        .outputCostDescriptionStart("TODO.消耗")
        .outputGainDescriptionStart("TODO.产出")
        .upgradeCostDescriptionStart("TODO.升级费用")
        .upgradeMaxLevelDescription("(TODO.已达到最大等级)")
        .extraTexts(JavaFeatureForGwt.listOf("TODO", "TODO", "TODO", "TODO", "TODO"))
        .levelDescriptionProvider(LevelDescriptionPackage.builder()
            .levelPart("TODO.等级{0}")
            .reachedMaxLevelPart("(TODO.最大)")
            .activeLevelPart("TODO.启用: {0}")
            .build())
        .proficiencyDescriptionProvider(ProficiencyDescriptionPackage.builder()
            .proficiencyPart("TODO.效率: {0)")
            .formatPercentage(true)
            .proficiencyPart("TODO.Growth: {0}%")
            .build())
        .build();

    static AchievementDescriptionPackage DEFAULT_AchievementDescriptionPackage = AchievementDescriptionPackage.builder()
        .name("TODO.NO.1")
            .wikiText("TODO.拥有2个等级1的{PrototypeName}")
            .congratulationText("TODO.你完成了任务NO.1。")
            .descriptionReplaceConstructionPrototypeId(IdleMushroomConstructionPrototypeId.EPOCH_1_MUSHROOM_AUTO_PROVIDER)
            .build();


    IdleMushroomGame idleMushroomGame;



    Map<String, DescriptionPackage> descriptionPackageCache = new HashMap<>();
    Map<String, AchievementDescriptionPackage> achievementDescriptionPackageCache = new HashMap<>();
    public DescriptionPackageFactory(IdleMushroomGame idleMushroomGame) {
        this.idleMushroomGame = idleMushroomGame;
    }

    private String bundleSafeGet(I18NBundle bundle, String key) {
        if (!bundle.keys().contains(key)) {
            //idleMushroomGame.getFrontend().log(this.getClass().getSimpleName(), "bundle miss key = %s", key);
            return "[miss bundle]";
        }
        return bundle.get(key);
    }
    private static final Json json = new Json();


    public DescriptionPackage clone(DescriptionPackage descriptionPackage) {
        String text = json.toJson(descriptionPackage);
        return json.fromJson(DescriptionPackage.class, text);
    }

    public AchievementDescriptionPackage clone(AchievementDescriptionPackage descriptionPackage) {
        String text = json.toJson(descriptionPackage);
        return json.fromJson(AchievementDescriptionPackage.class, text);
    }

    @Override
    public DescriptionPackage getConstructionDescriptionPackage(String prototypeId) {
        if (descriptionPackageCache.containsKey(prototypeId)) {
            return clone(descriptionPackageCache.get(prototypeId));
        }

        FileHandle fileHandle = Gdx.files.internal("i18n/ConstructionPrototype");
        I18NBundle bundle = I18NBundle.createBundle(fileHandle, idleMushroomGame.getIdleGameplayExport().getLocale());
        if (bundle != null) {
            // 若存在COPY标记的key，则其value作为模板
            String usingClassKey = Optional.of(prototypeId + I18N_COPY_KEY)
                .filter(it -> bundle.keys().contains(it))
                .map(it -> bundle.get(it))
                .orElse(prototypeId);
            // 若存在COPY标记的key，则其value作为模板
            String usingLevelDescriptionPackageClassKey = Optional.of(usingClassKey + ".levelDescriptionProvider" + I18N_COPY_KEY)
                .filter(it -> bundle.keys().contains(it))
                .map(it -> bundle.get(it))
                .orElse(usingClassKey + ".levelDescriptionProvider");
            // 若存在COPY标记的key，则其value作为模板
            String usingProficiencyDescriptionPackageClassKey = Optional.of(usingClassKey + ".proficiencyDescriptionProvider" + I18N_COPY_KEY)
                .filter(it -> bundle.keys().contains(it))
                .map(it -> bundle.get(it))
                .orElse(usingClassKey + ".proficiencyDescriptionProvider");

            DescriptionPackage result = DescriptionPackage.builder()
                .name(bundleSafeGet(bundle, usingClassKey + ".name"))
                .wikiText(bundleSafeGet(bundle, usingClassKey + ".wikiText"))
                .upgradeButtonText(bundleSafeGet(bundle, usingClassKey + ".upgradeButtonText"))
                .outputCostDescriptionStart(bundleSafeGet(bundle, usingClassKey + ".outputCostDescriptionStart"))
                .outputGainDescriptionStart(bundleSafeGet(bundle, usingClassKey + ".outputGainDescriptionStart"))
                .upgradeCostDescriptionStart(bundleSafeGet(bundle, usingClassKey + ".upgradeCostDescriptionStart"))
                .upgradeMaxLevelDescription(bundleSafeGet(bundle, usingClassKey + ".upgradeMaxLevelDescription"))
                .transformButtonText(bundleSafeGet(bundle, usingClassKey + ".transformButtonText"))
                .transformCostDescriptionStart(bundleSafeGet(bundle, usingClassKey + ".transformCostDescriptionStart"))
                .extraTexts(new ArrayList<>(Arrays.asList(
                    bundleSafeGet(bundle, usingClassKey + ".extraTexts").split(I18N_VALUE_LIST_SPLIT)
                )))
                .levelDescriptionProvider(LevelDescriptionPackage.builder()
                    .levelPart(bundleSafeGet(bundle, usingLevelDescriptionPackageClassKey + ".levelPart"))
                    .reachedMaxLevelPart(bundleSafeGet(bundle, usingLevelDescriptionPackageClassKey + ".reachedMaxLevelPart"))
                    .activeLevelPart(bundleSafeGet(bundle, usingLevelDescriptionPackageClassKey + ".activeLevelPart"))
                    .build())
                .proficiencyDescriptionProvider(ProficiencyDescriptionPackage.builder()
                    .formatPercentage(bundleSafeGet(bundle, usingProficiencyDescriptionPackageClassKey + ".levelPart").equals(Boolean.TRUE.toString()))
                    .proficiencyPart(bundleSafeGet(bundle, usingProficiencyDescriptionPackageClassKey + ".proficiencyPart"))
                    .reachedMaxProficiencyPart(bundleSafeGet(bundle, usingProficiencyDescriptionPackageClassKey + ".reachedMaxProficiencyPart"))
                    .build())
                .build();
            descriptionPackageCache.put(prototypeId, result);
            return clone(result);
        }

        // TODO
        return DEFAULT_DescriptionPackage;
    }

    @Override
    public AchievementDescriptionPackage getAchievementDescriptionPackage(String prototypeId) {
        if (achievementDescriptionPackageCache.containsKey(prototypeId)) {
            return clone(achievementDescriptionPackageCache.get(prototypeId));
        }
        FileHandle fileHandle = Gdx.files.internal("i18n/AchievementDescriptionPackage");
        I18NBundle bundle = I18NBundle.createBundle(fileHandle, idleMushroomGame.getIdleGameplayExport().getLocale());
        if (bundle != null) {
            // 若存在COPY标记的key，则其value作为模板
            String usingClassKey = Optional.of(prototypeId + I18N_COPY_KEY)
                .filter(it -> bundle.keys().contains(it))
                .map(it -> bundle.get(it))
                .orElse(prototypeId);

            AchievementDescriptionPackage result = AchievementDescriptionPackage.builder()
                .name(bundleSafeGet(bundle, usingClassKey + ".name"))
                .wikiText(bundleSafeGet(bundle, usingClassKey + ".wikiText"))
                .congratulationText(bundleSafeGet(bundle, usingClassKey + ".congratulationText"))
                .descriptionReplaceConstructionPrototypeId(bundleSafeGet(bundle, usingClassKey + ".descriptionReplaceConstructionPrototypeId"))
                .build();
            achievementDescriptionPackageCache.put(prototypeId, result);
            return clone(result);
        }
        return DEFAULT_AchievementDescriptionPackage;
    }

    @Override
    public Map<String, Integer> getAchievementExtraArgMap() {
        return IdleMushroomAchievementLoader.achievementExtraArgMap;
    }
}
