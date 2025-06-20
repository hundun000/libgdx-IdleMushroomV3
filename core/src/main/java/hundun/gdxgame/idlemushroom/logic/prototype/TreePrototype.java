package hundun.gdxgame.idlemushroom.logic.prototype;

import hundun.gdxgame.idlemushroom.logic.construction.BaseIdleMushroomConstruction;
import hundun.gdxgame.idlemushroom.util.IdleMushroomJavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.ConstProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.SimpleAutoOutputComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


public class TreePrototype extends AbstractConstructionPrototype {

    public TreePrototype(String prototypeId) {
        super(
                prototypeId
        );


    }

    @Override
    public BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();

        BaseIdleMushroomConstruction thiz = new BaseIdleMushroomConstruction(prototypeId, id, position, descriptionPackage);

        ConstProficiencyComponent proficiencyComponent = new ConstProficiencyComponent(thiz);
        thiz.setProficiencyComponent(proficiencyComponent);

        SimpleAutoOutputComponent outputComponent = new SimpleAutoOutputComponent(thiz);
        outputComponent.setTypeClickOutput(true);
        thiz.setOutputComponent(outputComponent);

        return thiz;
    }
}
