package hundun.gdxgame.idleshare.core.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hundun.gdxgame.idleshare.gamelib.framework.callback.IPopupBoardCallback;
import lombok.Setter;

import java.util.function.Supplier;

public class PopupBoardCallerClickListener<T> extends ClickListener {
    Supplier<T> modelGetter;
    IPopupBoardCallback<T> callback;

    @Setter
    boolean onlyClick;

    public PopupBoardCallerClickListener(Supplier<T> modelGetter, IPopupBoardCallback<T> callback) {
        this.modelGetter = modelGetter;
        this.callback = callback;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        callback.showAndUpdatePopupBoard(modelGetter.get());
        Gdx.app.log(PopupBoardCallerClickListener.class.getSimpleName(), "this clicked event");
        super.clicked(event, x, y);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (!onlyClick && pointer == -1) {
            callback.showAndUpdatePopupBoard(modelGetter.get());
        }
        super.enter(event, x, y, pointer, fromActor);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (!onlyClick && pointer == -1) {
            callback.hideAndCleanPopupBoard();
        }
        super.exit(event, x, y, pointer, toActor);
    }
}
