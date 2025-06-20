package hundun.gdxgame.idlemushroom.logic.prototype;

import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.logic.construction.BaseIdleMushroomConstruction;
import hundun.gdxgame.idlemushroom.logic.id.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.id.ResourceType;
import hundun.gdxgame.idlemushroom.logic.loader.IdleMushroomConstructionsLoader;
import hundun.gdxgame.idlemushroom.util.IdleMushroomJavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.data.ChildGameConfig.ConstructionBuyCandidateConfig;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.ConstProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.SimpleAutoOutputComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


public class DirtPrototype extends AbstractConstructionPrototype {


    public DirtPrototype(String prototypeId) {
        super(
                prototypeId
        );
    }

    @Override
    public BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();

        BaseIdleMushroomConstruction thiz = new BaseIdleMushroomConstruction(prototypeId, id, position, descriptionPackage);

        thiz.setAllowPositionOverwrite(true);

        ConstProficiencyComponent proficiencyComponent = new ConstProficiencyComponent(thiz);
        thiz.setProficiencyComponent(proficiencyComponent);

        SimpleAutoOutputComponent outputComponent = new SimpleAutoOutputComponent(thiz);
        thiz.setOutputComponent(outputComponent);


        switch (prototypeId) {
            case IdleMushroomConstructionPrototypeId.EPOCH_1_EMPTY_CELL:
                thiz.getExistenceComponent().setBuyCandidateConfigs(JavaFeatureForGwt.listOf(
                        ConstructionBuyCandidateConfig.builder()
                                .prototypeId(IdleMushroomConstructionPrototypeId.EPOCH_1_MUSHROOM_AUTO_PROVIDER)
                                .buyCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                                        ResourceType.MUSHROOM, 50
                                )))
                                .build()
                ));
                break;
            case IdleMushroomConstructionPrototypeId.EPOCH_2_EMPTY_CELL:
                thiz.getExistenceComponent().setBuyCandidateConfigs(JavaFeatureForGwt.listOf(
                        ConstructionBuyCandidateConfig.builder()
                                .prototypeId(IdleMushroomConstructionPrototypeId.EPOCH_2_MUSHROOM_AUTO_PROVIDER)
                                .buyCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                                        ResourceType.MUSHROOM, 100
                                )))
                                .build()
                ));
                break;
            case IdleMushroomConstructionPrototypeId.EPOCH_3_EMPTY_CELL:
                thiz.getExistenceComponent().setBuyCandidateConfigs(JavaFeatureForGwt.listOf(
                        ConstructionBuyCandidateConfig.builder()
                                .prototypeId(IdleMushroomConstructionPrototypeId.EPOCH_3_MUSHROOM_AUTO_PROVIDER)
                                .buyCostPack(IdleMushroomConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                                        ResourceType.MUSHROOM, 200
                                )))
                                .build()
                ));
                break;
            default:
        }


        return thiz;
    }
}
