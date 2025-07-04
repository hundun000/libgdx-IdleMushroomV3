package hundun.gdxgame.idleshare.gamelib.framework.model.construction.base;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;


import java.util.Locale;
import java.util.Map;

/**
 * @author hundun
 * Created on 2023/03/01
 */
public interface IBuiltinConstructionsLoader {
    Map<String, AbstractConstructionPrototype> getProviderMap();
}
