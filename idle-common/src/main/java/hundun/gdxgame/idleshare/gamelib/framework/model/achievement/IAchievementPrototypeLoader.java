package hundun.gdxgame.idleshare.gamelib.framework.model.achievement;



import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend.IDescriptionPackageFactory;

import java.util.Locale;
import java.util.Map;

public interface IAchievementPrototypeLoader {
    Map<String, AbstractAchievementPrototype> getProviderMap(IDescriptionPackageFactory descriptionPackageFactory);
}
