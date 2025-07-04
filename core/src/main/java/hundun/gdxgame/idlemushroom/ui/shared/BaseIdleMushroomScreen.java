package hundun.gdxgame.idlemushroom.ui.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hundun.gdxgame.libv3.corelib.base.BaseHundunScreen;
import hundun.gdxgame.libv3.gamelib.starter.listerner.IGameAreaChangeListener;
import hundun.gdxgame.idlemushroom.IdleMushroomGame;
import hundun.gdxgame.idlemushroom.logic.RootSaveData;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomScreenContext.IdleMushroomPlayScreenLayoutConst;
import hundun.gdxgame.idlemushroom.ui.shared.wiki.SharedWikiPopupBoard;
import hundun.gdxgame.idleshare.gamelib.framework.callback.IPopupBoardCallback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseIdleMushroomScreen extends BaseHundunScreen<IdleMushroomGame, RootSaveData>
{
    public class WikiPopupBoardCallback implements IPopupBoardCallback<Object> {
        @Override
        public void showAndUpdatePopupBoard(Object model) {
            popupRootTable.clear();
            var wikiPopupBoard = new SharedWikiPopupBoard(BaseIdleMushroomScreen.this.getGame());
            popupRootTable.add(wikiPopupBoard).center().expand();
            wikiPopupBoard.update(model);
        }
        @Override
        public void hideAndCleanPopupBoard() {
            popupRootTable.clear();
        }
    }

    public class SystemSettingPopupBoardCallback implements IPopupBoardCallback<Void> {
        @Override
        public void showAndUpdatePopupBoard(Void model) {
            popupRootTable.clear();
            var systemSettingPopupBoard = new SystemSettingPopupBoard(BaseIdleMushroomScreen.this, this);
            popupRootTable.add(systemSettingPopupBoard).center().expand();
            Gdx.input.setInputProcessor(popupUiStage);
        }
        @Override
        public void hideAndCleanPopupBoard() {
            popupRootTable.clear();
            Gdx.input.setInputProcessor(provideDefaultInputProcessor());
        }
    }

    @Getter
    WikiPopupBoardCallback wikiPopupBoardCallback = new WikiPopupBoardCallback();
    @Getter
    SystemSettingPopupBoardCallback systemSettingPopupBoardCallback = new SystemSettingPopupBoardCallback();

    @Getter
    protected final IdleMushroomPlayScreenLayoutConst layoutConst;

    protected boolean hidden;
    protected List<IGameAreaChangeListener> gameAreaChangeListeners;

    @Getter
    protected final String screenId;
    protected boolean enableLogicFrameOnShow = true;
    protected StorageInfoBoard storageInfoTable;
    protected BuffInfoBoard buffInfoBoard;
    protected BackgroundImageBox backgroundImageBox;
    protected IdleMushroomGameAreaControlBoard gameAreaControlBoard;
    protected ExtraControlBoard extraControlBoard;

    protected Table leftSideGroup;
    protected Table middleGroup;
    protected Table uiRootTable;
    protected Table popupRootTable;
    protected static int UI_ROOT_TABLE_COLSPAN_SIZE = 3;

    public static class BackgroundImageBox extends Container<Image> implements IGameAreaChangeListener{
        BaseIdleMushroomScreen parent;

        public BackgroundImageBox(BaseIdleMushroomScreen parent) {
            this.parent = parent;
            this.setFillParent(true);
            //this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        }

        @Override
        public void onGameAreaChange(String last, String current) {
            Drawable image = new TextureRegionDrawable(parent.getGame().getTextureManager().getBackgroundTexture(current));
            this.setBackground(image);
        }

    }

    public BaseIdleMushroomScreen(IdleMushroomGame game, String screenId, IdleMushroomPlayScreenLayoutConst layoutConst) {
        super(game);
        this.screenId = screenId;
        this.layoutConst = layoutConst;
        this.gameAreaChangeListeners = new ArrayList<>();
    }

    @Override
    protected void baseInit(ScreenArg arg) {
        super.baseInit(arg);

        uiRootTable = new Table();
        uiRootTable.setFillParent(true);
        uiStage.addActor(uiRootTable);

        popupRootTable = new Table();
        popupRootTable.setFillParent(true);
        popupUiStage.addActor(popupRootTable);
    }

    @Override
    protected void create() {

        lazyInitBackUiAndPopupUiContent();

        lazyInitUiRootContext();

        lazyInitLogicContext();

        if (game.debugMode) {
            uiRootTable.debugAll();
            popupRootTable.debugCell();
        }
    }


    protected void lazyInitLogicContext() {

        gameAreaChangeListeners.add(backgroundImageBox);
        gameAreaChangeListeners.add(gameAreaControlBoard);

        gameAreaControlBoard.lazyInit(game.getControlBoardScreenGetters());

        this.getGame().getIdleGameplayExport().getGameplayContext().getEventManager().registerListener(this);
        this.getGame().getIdleGameplayExport().getGameplayContext().getEventManager().registerListener(storageInfoTable);
        this.getGame().getIdleGameplayExport().getGameplayContext().getEventManager().registerListener(buffInfoBoard);
    }

    /**
     * BaseIdleMushroomScreen.lazyInitUiRootContext()结束时，uiRootTable基础布局已完成：<br>
     * 1. 第一第二行（上部）是定高的storageInfoTable和buffInfoBoard；<br>
     * 2. 第三行分为左中右，右部是定宽定高的gameAreaControlBoard，中部middleGroup应用grow()，则左侧leftSideGroup默认宽度为0。<br>
     * 子类可拓展：<br>
     * - 向leftSideGroup添加（定宽的）board。<br>
     * - 向middleGroup添加游戏画面。<br>
     * - 继续向uiRootTable添加第四行（下部）（定高的）board。<br>
     */
    protected void lazyInitUiRootContext() {
        storageInfoTable = new StorageInfoBoard(this);
        uiRootTable.add(storageInfoTable)
                .height(layoutConst.STORAGE_BOARD_BORDER_HEIGHT / 2.0f)
                .fill()
                .colspan(UI_ROOT_TABLE_COLSPAN_SIZE)
                .row();
        buffInfoBoard = new BuffInfoBoard(this);
        uiRootTable.add(buffInfoBoard)
                .height(layoutConst.STORAGE_BOARD_BORDER_HEIGHT / 2.0f)
                .fill()
                .colspan(UI_ROOT_TABLE_COLSPAN_SIZE)
                .row();


        leftSideGroup = new Table();
        uiRootTable.add(leftSideGroup)
                .growY();

        middleGroup = new Table();
        uiRootTable.add(middleGroup).grow();

        Table verticalGroup = new Table();
        gameAreaControlBoard = new IdleMushroomGameAreaControlBoard(this);
        extraControlBoard = new ExtraControlBoard(this);

        verticalGroup.add(gameAreaControlBoard)
            .width(this.getLayoutConst().AREA_BOARD_BORDER_WIDTH)
            .row();
        verticalGroup.add(extraControlBoard)
            .width(this.getLayoutConst().AREA_BOARD_BORDER_WIDTH);
        uiRootTable.add(verticalGroup)
            .top()
            .row();
    }

    protected void lazyInitBackUiAndPopupUiContent() {



        this.backgroundImageBox = new BackgroundImageBox(this);
        backUiStage.addActor(backgroundImageBox);

    }

    @Override
    public void dispose() {
    }


    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(provideDefaultInputProcessor());

        updateUIForShow();

        for (IGameAreaChangeListener gameAreaChangeListener : gameAreaChangeListeners) {
            gameAreaChangeListener.onGameAreaChange(game.getLastScreenId(), this.getScreenId());
        }
        game.getLogicFrameHelper().setLogicFramePause(!enableLogicFrameOnShow);

        this.hidden = false;
        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }

    @Override
    public void hide() {
        super.hide();

        this.hidden = true;
    }

    protected void updateUIForShow() {
        buffInfoBoard.updateViewData(new HashMap<>(0));
    };

    /**
     * 子类决定好当前Screen的InputProcessor（常为mixed）后，在此方法提供给基类。基类管理切换。
     */
    protected abstract InputProcessor provideDefaultInputProcessor();
}
