package hundun.gdxgame.idlemushroom.logic.prototype;

import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.logic.construction.BaseIdleMushroomConstruction;
import hundun.gdxgame.idlemushroom.logic.construction.DemoSimpleAutoOutputComponent;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.id.ResourceType;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomConstructionsLoader;
import hundun.gdxgame.idlemushroom.util.IdleMushroomJavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.ConstProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


import java.util.Locale;


public class AutoSellerPrototype extends AbstractConstructionPrototype {

    public AutoSellerPrototype(Locale language) {
        super(
                IdleMushroomConstructionPrototypeId.MUSHROOM_AUTO_SELLER
        );

    }

    @Override
    public BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();
        BaseIdleMushroomConstruction construction = new BaseIdleMushroomConstruction(prototypeId, id, position, descriptionPackage);

        ConstProficiencyComponent proficiencyComponent = new ConstProficiencyComponent(construction);
        construction.setProficiencyComponent(proficiencyComponent);

        DemoSimpleAutoOutputComponent outputComponent = new DemoSimpleAutoOutputComponent(construction);
        construction.setOutputComponent(outputComponent);

        construction.getOutputComponent().setOutputCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.MUSHROOM, 1
        )));
        construction.getOutputComponent().setOutputGainPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.DNA_POINT, 2
        )));

        construction.getUpgradeComponent().setUpgradeCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.MUSHROOM, 0
        )));
        construction.getUpgradeComponent().setCalculateCostFunction((baseValue, level) -> {
            return Math.max(1, (level - 1) * 2) * 50L;
        });
        construction.getLevelComponent().setTypeWorkingLevelChangeable(true);

        return construction;
    }
}
