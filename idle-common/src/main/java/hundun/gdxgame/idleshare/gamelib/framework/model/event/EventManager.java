package hundun.gdxgame.idleshare.gamelib.framework.model.event;

import hundun.gdxgame.libv3.gamelib.starter.listerner.IGameStartListener;
import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.idleshare.gamelib.framework.callback.IAchievementBoardCallback;
import hundun.gdxgame.idleshare.gamelib.framework.callback.IAchievementStateChangeListener;
import hundun.gdxgame.idleshare.gamelib.framework.callback.IConstructionCollectionListener;
import hundun.gdxgame.idleshare.gamelib.framework.callback.INotificationBoardCallerAndCallback;
import hundun.gdxgame.idleshare.gamelib.framework.listener.IBuffChangeListener;
import hundun.gdxgame.idleshare.gamelib.framework.listener.IResourceChangeListener;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.AbstractAchievementPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.AchievementManager.AchievementState;
import hundun.gdxgame.idleshare.gamelib.framework.model.resource.StorageManager.ModifyResourceTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author hundun
 * Created on 2021/11/12
 */
public class EventManager {
    List<IGameStartListener> gameStartListeners = new ArrayList<>();
    List<IBuffChangeListener> buffChangeListeners = new ArrayList<>();
    List<IAchievementStateChangeListener> achievementStateChangeListeners = new ArrayList<>();
    List<INotificationBoardCallerAndCallback> notificationBoardCallerAndCallbacks = new ArrayList<>();
    List<IResourceChangeListener> oneFrameResourceChangeListeners = new ArrayList<>();
    List<IConstructionCollectionListener> constructionCollectionListeners = new ArrayList<>();
    IdleGameplayContext gameContext;


    public EventManager(IdleGameplayContext gameContext) {
        this.gameContext = gameContext;
    }

    public void registerListener(Object listener) {
        if (listener instanceof IGameStartListener && !gameStartListeners.contains(listener)) {
            gameStartListeners.add((IGameStartListener) listener);
        }
        if (listener instanceof IBuffChangeListener && !buffChangeListeners.contains(listener)) {
            buffChangeListeners.add((IBuffChangeListener) listener);
        }
        if (listener instanceof IAchievementStateChangeListener && !achievementStateChangeListeners.contains(listener)) {
            achievementStateChangeListeners.add((IAchievementStateChangeListener) listener);
        }
        if (listener instanceof INotificationBoardCallerAndCallback && !notificationBoardCallerAndCallbacks.contains(listener))
        {
            notificationBoardCallerAndCallbacks.add((INotificationBoardCallerAndCallback)listener);
        }
        if (listener instanceof IResourceChangeListener && !oneFrameResourceChangeListeners.contains(listener)) {
            oneFrameResourceChangeListeners.add((IResourceChangeListener) listener);
        }
        if (listener instanceof IConstructionCollectionListener && !constructionCollectionListeners.contains(listener))
        {
            constructionCollectionListeners.add((IConstructionCollectionListener)listener);
        }
    }

    public void unregisterListener(Object listener)
    {
        if (listener instanceof IGameStartListener) {
            gameStartListeners.remove((IGameStartListener) listener);
        }
        if (listener instanceof IBuffChangeListener)
        {
            buffChangeListeners.remove((IBuffChangeListener)listener);
        }
        if (listener instanceof IAchievementBoardCallback)
        {
            achievementStateChangeListeners.remove((IAchievementStateChangeListener)listener);
        }
        if (listener instanceof INotificationBoardCallerAndCallback)
        {
            notificationBoardCallerAndCallbacks.remove((INotificationBoardCallerAndCallback)listener);
        }
        if (listener instanceof IResourceChangeListener)
        {
            oneFrameResourceChangeListeners.remove((IResourceChangeListener)listener);
        }
        if (listener instanceof IConstructionCollectionListener)
        {
            constructionCollectionListeners.remove((IConstructionCollectionListener)listener);
        }
    }

    public void notifyGameStart() {
        gameContext.getFrontend().log(this.getClass().getSimpleName(), "notifyGameStart");
        for (IGameStartListener listener : gameStartListeners) {
            listener.onGameStart();
        }
    }

//    public void notifyResourceAmountChange(boolean fromLoad) {
//        Gdx.app.log(this.getClass().getSimpleName(), "notifyResourceAmountChange");
//        for (IAmountChangeEventListener listener : amountChangeEventListeners) {
//            listener.onResourceChange(fromLoad);
//        }
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OneSecondResourceChangeEvent {
        OneSecondResourceChangeEventOneTagData allTagData;
        Map<ModifyResourceTag, OneSecondResourceChangeEventOneTagData> tagDataMap;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OneSecondResourceChangeEventOneTagData {
        Map<String, Long> secondChangeMap;
    }

    public void notifyOneSecondResourceChange(OneSecondResourceChangeEvent event) {
        //gameContext.getFrontend().log(this.getClass().getSimpleName(), "notifyOneSecondResourceChange");
        for (IResourceChangeListener listener : oneFrameResourceChangeListeners) {
            listener.onResourceChange(event);
        }
    }

    public void notifyAchievementComplete(AbstractAchievementPrototype achievement, AchievementState state) {
        gameContext.getFrontend().log(this.getClass().getSimpleName(), "notifyAchievementComplete");
        for (IAchievementStateChangeListener listener : achievementStateChangeListeners) {
            listener.onAchievementStateChange(achievement, state);
        }
    }

    public void notifyConstructionCollectionChange()
    {
        gameContext.getFrontend().log(this.getClass().getSimpleName(), "notifyConstructionCollectionChange");
        for (IConstructionCollectionListener listener : constructionCollectionListeners)
        {
            listener.onConstructionCollectionChange();
        }
    }

    public void notifyBuffChange(Map<String, Integer> map) {
        gameContext.getFrontend().log(this.getClass().getSimpleName(), "notifyBuffChange");
        for (IBuffChangeListener listener : buffChangeListeners) {
            listener.onBuffChange(map);
        }
    }
}
