package hundun.gdxgame.idleshare.core.framework;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hundun.gdxgame.libv3.corelib.base.BaseHundunGame;
import hundun.gdxgame.libv3.corelib.base.BaseHundunScreen;
import hundun.gdxgame.idleshare.core.framework.entity.BaseGameEntityFactory;
import hundun.gdxgame.idleshare.core.framework.entity.GameEntity;
import hundun.gdxgame.idleshare.core.framework.manager.GameEntityManager;
import hundun.gdxgame.idleshare.gamelib.framework.listener.IResourceChangeListener;
import hundun.gdxgame.idleshare.gamelib.framework.model.event.EventManager.OneSecondResourceChangeEvent;

import java.util.List;

/**
 * @author hundun
 * Created on 2021/11/16
 * @param <T_GAME>
 */
public class GameImageDrawer<T_GAME extends BaseHundunGame<T_SAVE>, T_SAVE> implements IResourceChangeListener {

    SpriteBatch spriteBatch;
    BaseHundunScreen<T_GAME, T_SAVE> parent;
    IGameImageDrawerHolder holder;
    BaseGameEntityFactory gameEntityFactory;
    GameEntityManager manager;


    /**
     * 需要管理者（常为Screen）提供的一些方法。但又不应加到BaseHundunScreen，因为其他Screen用不上。
     */
    public interface IGameImageDrawerHolder {
        String getScreenId();

        long getResourceNumOrZero(String resourceId);

        int getConstructionWorkingLevelNumOrZero(String prototypeId);

        Sprite getResourceEntity(String resourceId);

        Sprite getConstructionEntity(String constructionId);
    }


    public GameImageDrawer(BaseHundunScreen<T_GAME, T_SAVE> parent, IGameImageDrawerHolder holder) {
        this.parent = parent;
        this.holder = holder;
        this.spriteBatch = new SpriteBatch();
    }


    /**
     * Entities移动一步并绘制新位置。
     */
    public void allEntitiesMoveForFrameAndDraw() {
        this.spriteBatch.begin();

        String gameArea = holder.getScreenId();
        List<String> needDrawConstructionIds = manager.getAreaEntityEffectConfigMap().get(gameArea).getOwnAmountConstructionPrototypeIds();
                manager.destroyNoNeedDrawConstructionIds(needDrawConstructionIds);
        manager.allEntityMoveForFrame();

        if (needDrawConstructionIds != null) {
            for (String id : needDrawConstructionIds) {
                List<GameEntity> queue = manager.getGameEntitiesOfConstructionPrototypeIds().get(id);
                if (queue == null) {
                    continue;
                }
                queue.forEach(entity -> {
                    this.spriteBatch.draw(entity.getTexture(), entity.getX(), entity.getY(), (entity.isTextureFlipX() ? -1 : 1) * entity.getDrawWidth(), entity.getDrawHeight());
                });
            }
        }

        List<String> needDrawByOwnAmountResourceIds = manager.getAreaEntityEffectConfigMap().get(gameArea).getOwnAmountResourceIds();
        if (needDrawByOwnAmountResourceIds != null) {
            for (String id : needDrawByOwnAmountResourceIds) {
                List<GameEntity> queue = manager.getGameEntitiesOfResourceIds().get(id);
                if (queue == null) {
                    continue;
                }
                queue.forEach(entity -> {
                    this.spriteBatch.draw(entity.getTexture(), entity.getX(), entity.getY(), (entity.isTextureFlipX() ? -1 : 1) * entity.getDrawWidth(), entity.getDrawHeight());
                });
            }
        }

        List<String> needDrawByChangeAmountResourceIds = manager.getAreaEntityEffectConfigMap().get(gameArea).getChangeAmountResourceIds();
        if (needDrawByChangeAmountResourceIds != null) {
            for (String id : needDrawByChangeAmountResourceIds) {
                List<GameEntity> queue = manager.getGameEntitiesOfResourceIds().get(id);
                if (queue == null) {
                    continue;
                }
                queue.forEach(entity -> {
                    this.spriteBatch.draw(entity.getTexture(), entity.getX(), entity.getY(), (entity.isTextureFlipX() ? -1 : 1) * entity.getDrawWidth(), entity.getDrawHeight());
                });
            }
        }

        this.spriteBatch.end();
    }


    /**
     * 可能因为资源数量变动，绘制内容变动
     */
    @Override
    public void onResourceChange(OneSecondResourceChangeEvent event) {
        String gameArea = holder.getScreenId();

        manager.areaEntityCheckByOwnAmount(gameArea, gameEntityFactory);
        manager.areaEntityCheckByChangeAmount(gameArea, gameEntityFactory, event.getAllTagData().getSecondChangeMap());
    }


    public void lazyInit(BaseGameEntityFactory gameEntityFactory, GameEntityManager manager) {
        this.gameEntityFactory = gameEntityFactory;
        this.manager = manager;
    }


}
