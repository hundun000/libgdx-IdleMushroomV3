package hundun.gdxgame.idleshare.gamelib.framework.model.construction;

import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend;
import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


import java.util.Locale;
import java.util.Map;

/**
 * @author hundun
 * Created on 2021/11/16
 */
public class BaseConstructionFactory {

    IdleGameplayContext gameContext;
    Locale locale;
    Map<String, AbstractConstructionPrototype> providerMap;
    IIdleFrontend idleFrontend;

    public void lazyInit(
            IdleGameplayContext gameContext,
            Locale locale,
            Map<String, AbstractConstructionPrototype> providerMap,
            IIdleFrontend idleFrontend
    ) {
        this.locale = locale;
        this.providerMap = providerMap;
        this.gameContext = gameContext;
        this.idleFrontend = idleFrontend;
    }

    public AbstractConstructionPrototype getPrototype(String prototypeId)
    {
        AbstractConstructionPrototype prototype = providerMap.get(prototypeId);
        return prototype;
    }


    public BaseConstruction getInstanceOfPrototype(String prototypeId, GridPosition position)
    {
        AbstractConstructionPrototype prototype = providerMap.get(prototypeId);
        DescriptionPackage descriptionPackage = idleFrontend.getDescriptionPackageFactory().getConstructionDescriptionPackage(prototypeId);
        BaseConstruction construction = prototype.getInstance(position, descriptionPackage);
        construction.lazyInitDescription(gameContext, locale);
        gameContext.getEventManager().registerListener(construction);
        return construction;
    }


}
