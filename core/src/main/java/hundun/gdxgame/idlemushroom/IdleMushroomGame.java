package hundun.gdxgame.idlemushroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.stripe.FreeTypeSkin;

import hundun.gdxgame.idlemushroom.ui.shared.BaseIdleMushroomScreen;
import hundun.gdxgame.libv3.gamelib.base.save.ISaveTool;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.logic.*;
import hundun.gdxgame.idlemushroom.logic.ProxyManager.IProxyManagerCallback;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomScreenId;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomAchievementLoader;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomBuffPrototypeLoader;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomConstructionsLoader;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomScreenContext;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomScreenContext.IdleMushroomPlayScreenLayoutConst;
import hundun.gdxgame.idleshare.gamelib.export.IdleGameplayExport;
import hundun.gdxgame.idleshare.gamelib.framework.data.ChildGameConfig;
import hundun.gdxgame.idleshare.gamelib.framework.util.text.TextFormatTool;
import hundun.gdxgame.libv3.corelib.base.BaseHundunGame;
import hundun.gdxgame.libv3.gamelib.starter.listerner.ILogicFrameListener;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class IdleMushroomGame extends BaseHundunGame<RootSaveData> {
    private static final float DEV_SCALE = 1f;
    private static final int LOGIC_FRAME_PER_SECOND = 100;
    @Getter
    protected IdleMushroomAudioPlayManager audioPlayManager;

    @Getter
    protected TextFormatTool textFormatTool;

    @Getter
    protected Viewport sharedViewport;

    @Getter
    protected IdleGameplayExport idleGameplayExport;
    @Getter
    protected ChildGameConfig childGameConfig;
    @Setter
    @Getter
    protected String lastScreenId;
    @Getter
    protected
    List<Supplier<BaseIdleMushroomScreen>> controlBoardScreenIds;

    @Getter
    IdleMushroomPlayScreenLayoutConst idleMushroomPlayScreenLayoutConst;
    @Getter
    private final IdleMushroomTextureManager textureManager;
    @Getter
    private final IdleMushroomScreenContext screenContext;
    @Getter
    private final IdleMushroomGameDictionary idleMushroomGameDictionary;
    @Getter
    private final IdleMushroomExtraGameplayExport idleMushroomExtraGameplayExport;
    @Getter
    private final ProxyManager proxyManager;
    @Getter
    private final HistoryManager historyManager;

    public IdleMushroomGame(ISaveTool<RootSaveData> saveTool, IProxyManagerCallback proxyManagerCallback) {
        super(GameArg.builder()
            .viewportWidth(960)
            .viewportHeight(640)
            .logicFramePerSecond(LOGIC_FRAME_PER_SECOND)
            .mainSkinFilePath("skins/IdleMushroom/IdleMushroom.json")
            .freeTypeSkin(true)
            .build());
        this.debugMode = false;
        this.getLogicFrameHelper().setScale(DEV_SCALE);

        this.sharedViewport = new ScreenViewport();
        this.textFormatTool = new TextFormatTool();
        this.saveHandler = new IdleMushroomSaveHandler(frontend, saveTool);
        this.textureManager = new IdleMushroomTextureManager();
        this.screenContext = new IdleMushroomScreenContext(this);
        this.audioPlayManager = new IdleMushroomAudioPlayManager(this);
        this.childGameConfig = new IdleMushroomChildGameConfig();
        this.idleMushroomGameDictionary = new IdleMushroomGameDictionary();
        this.controlBoardScreenIds = JavaFeatureForGwt.listOf(
            () -> screenContext.getMainPlayScreen(),
            () -> screenContext.getWorldPlayScreen(),
            () -> screenContext.getAchievementScreen()
        );
        this.idleMushroomExtraGameplayExport = new IdleMushroomExtraGameplayExport(this);
        this.proxyManager = new ProxyManager(this,
                proxyManagerCallback
                );
        this.historyManager = new HistoryManager(this);
    }


    @Data
    @AllArgsConstructor
    @Builder
    public static class RootEpochConfig {
        int enlargementLevel;
        int maxLevel;
        Map<String, BuffEpochConfig> buffEpochConfigMap;
        Map<String, ConstructionEpochConfig> constructionEpochConfigMap;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ConstructionEpochConfig {

        String transformToPrototypeId;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class BuffEpochConfig {
        int buffLevel;
    }

    @Override
    protected void createAfterGdxStatic() {
        super.createAfterGdxStatic();
        this.mainSkin = new FreeTypeSkin(Gdx.files.internal("skins/IdleMushroom/IdleMushroom.json"));
        this.idleMushroomPlayScreenLayoutConst = new IdleMushroomPlayScreenLayoutConst(this.getMainViewportWidth(), this.getMainViewportHeight());
        this.idleGameplayExport = new IdleGameplayExport(
            frontend,
            idleMushroomExtraGameplayExport,
            new IdleMushroomConstructionsLoader(),
            new IdleMushroomAchievementLoader(idleMushroomGameDictionary),
            new IdleMushroomBuffPrototypeLoader(),
            childGameConfig
        );
        this.getSaveHandler().registerSubHandler(idleGameplayExport);
        saveHandler.systemSettingLoadOrStarter();
    }

    @Override
    protected void createBody() {
        textureManager.lazyInitOnGameCreateStage2();

        idleMushroomExtraGameplayExport.lazyInitStage2();
        this.getIdleGameplayExport().getGameplayContext().getEventManager().registerListener(historyManager);
        screenContext.lazyInit();
    }

    @Override
    protected void createFinally() {
        audioPlayManager.lazyInitOnGameCreate(childGameConfig.getScreenIdToFilePathMap());

        screenManager.pushScreen(screenContext.getMenuScreen(), null);
        getAudioPlayManager().intoScreen(IdleMushroomScreenId.SCREEN_MENU);
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    protected void onLogicFrame(ILogicFrameListener source) {
        source.onLogicFrame();
        idleGameplayExport.onLogicFrame();
        proxyManager.onLogicFrame();
        historyManager.onLogicFrame();
    }
}
