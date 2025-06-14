package hundun.gdxgame.idleshare.gamelib.framework.model.construction;

import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;
import hundun.gdxgame.idleshare.gamelib.framework.util.text.Language;

import java.util.Map;

/**
 * @author hundun
 * Created on 2021/11/16
 */
public class BaseConstructionFactory {

    IdleGameplayContext gameContext;
    Language language;
    Map<String, AbstractConstructionPrototype> providerMap;

    public void lazyInit(
            IdleGameplayContext gameContext,
            Language language,
            Map<String, AbstractConstructionPrototype> providerMap
    ) {
        this.language = language;
        this.providerMap = providerMap;
        this.gameContext = gameContext;
    }

    public AbstractConstructionPrototype getPrototype(String prototypeId)
    {
        AbstractConstructionPrototype prototype = providerMap.get(prototypeId);
        return prototype;
    }


    public BaseConstruction getInstanceOfPrototype(String prototypeId, GridPosition position)
    {
        AbstractConstructionPrototype prototype = providerMap.get(prototypeId);
        BaseConstruction construction = prototype.getInstance(position);
        construction.lazyInitDescription(gameContext, language);
        gameContext.getEventManager().registerListener(construction);
        return construction;
    }


}
