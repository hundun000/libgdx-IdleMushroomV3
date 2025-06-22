package hundun.gdxgame.idlemushroom.ui.shared;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hundun.gdxgame.idlemushroom.ui.shared.BaseIdleMushroomScreen.SystemSettingPopupBoardCallback;

public class SystemSettingPopupBoard extends Table {

    BaseIdleMushroomScreen screen;
    SystemSettingPopupBoardCallback callback;

    public SystemSettingPopupBoard(BaseIdleMushroomScreen screen, SystemSettingPopupBoardCallback callback) {
        //super("GUIDE_TEXT", parent.game.getButtonSkin());
        this.screen = screen;
        this.callback = callback;
        //this.setBounds(5, GameAreaControlBoard.Y, GameAreaControlBoard.X - 10, 120);
        this.setBackground(screen.getGame().getTextureManager().getTableType1Drawable());


        Container<?> testButton = new Container<>(new Image(screen.getGame().getTextureManager().getCloseButtonTexture()));
        testButton.setBackground(screen.getGame().getTextureManager().getQuestionMarkTableDrawable());
        testButton.setTouchable(Touchable.enabled);
        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callback.hideAndCleanPopupBoard();
            }
        });
        this.add(testButton)
            .width(screen.getGame().getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize)
            .height(screen.getGame().getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize)
            .left();
    }




}
