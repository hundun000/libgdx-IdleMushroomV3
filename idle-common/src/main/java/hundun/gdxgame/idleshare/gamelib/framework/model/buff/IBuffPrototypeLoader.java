package hundun.gdxgame.idleshare.gamelib.framework.model.buff;



import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public interface IBuffPrototypeLoader {
    static IBuffPrototypeLoader emptyImpl() {
        return new IBuffPrototypeLoader() {
            @Override
            public Map<String, AbstractBuffPrototype> getProviderMap(Locale language) {
                return new HashMap<>();
            }
        };
    }

    Map<String, AbstractBuffPrototype> getProviderMap(Locale language);
}
