package hundun.gdxgame.idlemushroom.ui.shared;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hundun.gdxgame.idleshare.core.framework.PopupBoardCallerClickListener;
import hundun.gdxgame.libv3.gamelib.starter.listerner.IGameAreaChangeListener;

public class ExtraControlBoard extends Table {
    BaseIdleMushroomScreen parent;

    public ExtraControlBoard(BaseIdleMushroomScreen parent) {
        super();
        this.parent = parent;
        this.setBackground(parent.getGame().getTextureManager().getDefaultBoardNinePatchDrawable());

        Container<?> button = new Container<>(new Image(parent.getGame().getTextureManager().getSystemSettingButtonTexture()));
        button.setBackground(parent.getGame().getTextureManager().getQuestionMarkTableDrawable());
        button.setTouchable(Touchable.enabled);
        button.setSize(
            parent.getGame().getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize,
            parent.getGame().getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize
        );
        var listener = new PopupBoardCallerClickListener<>(() -> null, parent.getSystemSettingPopupBoardCallback());
        listener.setOnlyClick(true);
        button.addListener(listener);
        this.add(button);
    }

}
