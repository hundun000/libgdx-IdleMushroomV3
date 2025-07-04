package hundun.gdxgame.idleshare.gamelib.export;

import hundun.gdxgame.libv3.gamelib.base.IFrontend;
import hundun.gdxgame.libv3.gamelib.starter.listerner.ILogicFrameListener;
import hundun.gdxgame.libv3.gamelib.starter.save.PairChildrenSaveHandler.ISubGameplaySaveHandler;
import hundun.gdxgame.libv3.gamelib.starter.save.PairChildrenSaveHandler.ISubSystemSettingSaveHandler;
import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.idleshare.gamelib.framework.data.ChildGameConfig;
import hundun.gdxgame.idleshare.gamelib.framework.data.GameplaySaveData;
import hundun.gdxgame.idleshare.gamelib.framework.data.SystemSettingSaveData;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.IAchievementPrototypeLoader;
import hundun.gdxgame.idleshare.gamelib.framework.model.buff.IBuffPrototypeLoader;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.IBuiltinConstructionsLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author hundun
 * Created on 2023/02/21
 */
public class IdleGameplayExport implements ILogicFrameListener,
        ISubGameplaySaveHandler<GameplaySaveData>,
        ISubSystemSettingSaveHandler<SystemSettingSaveData>  {

    @Getter
    private final IdleGameplayContext gameplayContext;
    private final IBuiltinConstructionsLoader builtinConstructionsLoader;
    @Getter
    private final IAchievementPrototypeLoader builtinAchievementsLoader;
    private final ChildGameConfig childGameConfig;
    private final IBuffPrototypeLoader buffPrototypeLoader;
    @Setter
    @Getter
    private Locale locale;

    public String stageId;

    public IdleGameplayExport(
            IFrontend frontend,
            IIdleFrontend idleFrontend,
            IBuiltinConstructionsLoader builtinConstructionsLoader,
            IAchievementPrototypeLoader builtinAchievementsLoader,
            IBuffPrototypeLoader buffPrototypeLoader,
            ChildGameConfig childGameConfig
    ) {
        this.builtinConstructionsLoader = builtinConstructionsLoader;
        this.builtinAchievementsLoader = builtinAchievementsLoader;
        this.buffPrototypeLoader = buffPrototypeLoader;
        this.childGameConfig = childGameConfig;
        this.gameplayContext = new IdleGameplayContext(frontend, idleFrontend);
    }

    @Override
    public void onLogicFrame() {
        gameplayContext.setCurrentIntSecond((int)gameplayContext.getIdleFrontend().getSecond());
        gameplayContext.getConstructionManager().onSubLogicFrame();
        gameplayContext.getStorageManager().onSubLogicFrame();
        gameplayContext.getHistoryManager().onLogicFrame();
    }

    @Override
    public void applyGameplaySaveData(GameplaySaveData gameplaySaveData) {
        this.stageId = gameplaySaveData.getStageId();

        gameplaySaveData.getConstructionSaveDataMap().values().forEach(it -> {
            gameplayContext.getConstructionManager().loadInstance(it);
        });

        gameplayContext.getStorageManager().setUnlockedResourceTypes(gameplaySaveData.getUnlockedResourceTypes());
        gameplayContext.getStorageManager().setOwnResources(gameplaySaveData.getOwnResources());
        gameplayContext.getAchievementManager().subApplyGameplaySaveData(
                builtinAchievementsLoader.getProviderMap(gameplayContext.getIdleFrontend().getDescriptionPackageFactory()),
                gameplaySaveData.getAchievementSaveDataMap()
        );
        gameplayContext.getBuffManager().subApplyGameplaySaveData(
                buffPrototypeLoader.getProviderMap(locale),
                gameplaySaveData.getBuffSaveDataMap()
        );
    }

    @Override
    public void currentSituationToGameplaySaveData(GameplaySaveData gameplaySaveData) {
        gameplaySaveData.setStageId(this.stageId);
        List<BaseConstruction> constructions = gameplayContext.getConstructionManager().getAllConstructionInstances();
        gameplaySaveData.setConstructionSaveDataMap(constructions.stream()
                .collect(Collectors.toMap(
                        it -> it.getId(),
                        it -> it.getSaveData()
                        ))
                );
        gameplaySaveData.setUnlockedResourceTypes(gameplayContext.getStorageManager().getUnlockedResourceTypes());
        gameplaySaveData.setOwnResources(gameplayContext.getStorageManager().getOwnResources());
        gameplaySaveData.setAchievementSaveDataMap(gameplayContext.getAchievementManager().getAchievementSaveDataMap());
        gameplaySaveData.setBuffSaveDataMap(gameplayContext.getBuffManager().getBuffSaveDataMap());
    }

    @Override
    public void applySystemSetting(SystemSettingSaveData systemSettingSave) {
        Locale locale = Locale.forLanguageTag(systemSettingSave.getLocaleLanguageTag());
        this.locale = locale;
        gameplayContext.allLazyInit(
            locale,
            childGameConfig,
            builtinConstructionsLoader.getProviderMap(),
            builtinAchievementsLoader.getProviderMap(gameplayContext.getIdleFrontend().getDescriptionPackageFactory())
            );
        gameplayContext.getFrontend().log(this.getClass().getSimpleName(), "applySystemSetting done");
    }

    @Override
    public void currentSituationToSystemSetting(SystemSettingSaveData systemSettingSave) {
        systemSettingSave.setLocaleLanguageTag(this.getLocale().toLanguageTag());
    }

    public void postConstructionCreate(BaseConstruction construction) {}
}
