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
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage.ProficiencyDescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.BaseAutoProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


import java.util.HashMap;


public class AutoProviderPrototype extends AbstractConstructionPrototype {




    public AutoProviderPrototype(String prototypeId) {
        super(
                prototypeId
        );


    }

    @Override
    public BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();
        BaseIdleMushroomConstruction construction = new BaseIdleMushroomConstruction(prototypeId, id, position, descriptionPackage);

        AutoProviderProficiencyComponent proficiencyComponent = new AutoProviderProficiencyComponent(construction);
        construction.setProficiencyComponent(proficiencyComponent);

        DemoSimpleAutoOutputComponent outputComponent = new DemoSimpleAutoOutputComponent(construction);
        construction.setOutputComponent(outputComponent);

        construction.getOutputComponent().setOutputCostPack(IdleMushroomConstructionsLoader.toPack(new HashMap<>()));
        construction.getOutputComponent().setOutputGainPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.MUSHROOM, 1
        )));

        construction.getUpgradeComponent().setUpgradeCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.MUSHROOM, 50
        )));

        return construction;
    }

    public static class AutoProviderProficiencyComponent extends BaseAutoProficiencyComponent {

        public AutoProviderProficiencyComponent(
                BaseConstruction construction
        ) {
            super(construction, 1, 200, 200);
        }

        @Override
        protected void tryProficiencyOnce() {
            long neighborProviderCount = construction.getNeighbors().values().stream()
                    .map(it -> (BaseConstruction)it)
                    .filter(it -> it != null
                            && (
                                    it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_1_MUSHROOM_AUTO_PROVIDER)
                                            || it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_2_MUSHROOM_AUTO_PROVIDER)
                                            || it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_3_MUSHROOM_AUTO_PROVIDER)
                            )
                    )
                    .count();
            long neighborTreeCount = construction.getNeighbors().values().stream()
                    .map(it -> (BaseConstruction)it)
                    .filter(it -> it != null
                                    && (
                                    it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_1_TREE)
                                            || it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_2_TREE)
                                            || it.getSaveData().prototypeId.equals(IdleMushroomConstructionPrototypeId.EPOCH_3_TREE)
                            )
                    )
                    .count();
            int add = (int) Math.max(1, 1 + neighborTreeCount * 2 - neighborProviderCount);
            this.changeProficiency(add);
        }
    }
}
