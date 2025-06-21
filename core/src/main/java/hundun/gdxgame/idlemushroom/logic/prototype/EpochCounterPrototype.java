package hundun.gdxgame.idlemushroom.logic.prototype;

import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.logic.construction.BaseIdleMushroomConstruction;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.id.ResourceType;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomConstructionsLoader;
import hundun.gdxgame.idlemushroom.util.IdleMushroomJavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.ConstProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.SimpleAutoOutputComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


import java.util.Locale;


public class EpochCounterPrototype extends AbstractConstructionPrototype {

    public EpochCounterPrototype() {
        super(
                IdleMushroomConstructionPrototypeId.EPOCH_COUNTER
        );


    }

    @Override
    public BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();

        BaseIdleMushroomConstruction construction = new BaseIdleMushroomConstruction(prototypeId, id, position, descriptionPackage);

        ConstProficiencyComponent proficiencyComponent = new ConstProficiencyComponent(construction);
        construction.setProficiencyComponent(proficiencyComponent);

        SimpleAutoOutputComponent outputComponent = new SimpleAutoOutputComponent(construction);
        construction.setOutputComponent(outputComponent);

        construction.getUpgradeComponent().setUpgradeCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.DNA_POINT, 0
        )));
        construction.getUpgradeComponent().setCalculateCostFunction((baseValue, level) -> {
            return (1L << (level - 1)) * Math.max(1, (level - 1) * 2) * 50L;
        });

        return construction;
    }
}
