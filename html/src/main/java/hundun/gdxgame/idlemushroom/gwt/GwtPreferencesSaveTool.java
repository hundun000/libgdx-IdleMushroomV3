package hundun.gdxgame.idlemushroom.gwt;

import com.badlogic.gdx.Gdx;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import hundun.gdxgame.idlemushroom.logic.RootSaveData;
import hundun.gdxgame.libv3.corelib.base.save.AbstractLibgdxSaveTool;



/**
 * @author hundun
 * Created on 2021/11/10
 */
public class GwtPreferencesSaveTool extends AbstractLibgdxSaveTool<RootSaveData> {


    private SaveDataMapper objectMapper;

    public static interface SaveDataMapper extends ObjectMapper<RootSaveData> {}


    public GwtPreferencesSaveTool(String preferencesName) {
        super(preferencesName);
        this.objectMapper = GWT.create(SaveDataMapper.class);
    }

    @Override
    protected String serializeRootSaveData(RootSaveData saveData) {
        try {
            return objectMapper.write(saveData);
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "serializeRootSaveData() error", e);
            return null;
        }
    }

    @Override
    protected RootSaveData deserializeRootSaveData(String raw) {
        try {
            return objectMapper.read(raw);
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "deserializeRootSaveData() error", e);
            return null;
        }
    }
}
