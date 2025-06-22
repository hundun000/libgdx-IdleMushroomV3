package hundun.gdxgame.idleshare.gamelib.framework.callback;

import org.jetbrains.annotations.Nullable;

/**
 * @author hundun
 * Created on 2023/02/17
 */
public interface IPopupBoardCallback<T> {
    void showAndUpdatePopupBoard(@Nullable T model);
    void hideAndCleanPopupBoard();
}
