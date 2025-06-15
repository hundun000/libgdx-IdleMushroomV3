package hundun.gdxgame.idleshare.gamelib.framework.model.achievement;



import java.util.Locale;
import java.util.Map;

public interface IAchievementPrototypeLoader {
    Map<String, AbstractAchievementPrototype> getProviderMap(Locale language);
}
