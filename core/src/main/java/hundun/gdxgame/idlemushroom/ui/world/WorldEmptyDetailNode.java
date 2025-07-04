package hundun.gdxgame.idlemushroom.ui.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomScreenContext.IdleMushroomPlayScreenLayoutConst;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomWorldPlayScreen;
import hundun.gdxgame.idlemushroom.ui.shared.BaseCellDetailNodeVM;
import hundun.gdxgame.idlemushroom.ui.shared.BaseIdleMushroomPlayScreen;
import hundun.gdxgame.idlemushroom.ui.shared.ConstructionDetailPartVM;
import hundun.gdxgame.idleshare.core.framework.PopupBoardCallerClickListener;
import hundun.gdxgame.idleshare.gamelib.framework.data.ChildGameConfig.ConstructionBuyCandidateConfig;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hundun
 * Created on 2021/11/05
 */
public class WorldEmptyDetailNode extends BaseCellDetailNodeVM {
    IdleMushroomWorldPlayScreen screen;
    BaseConstruction construction;
    GridPosition position;
    Label constructionNameLabel;
    Label buyCandidateKeyLabel;
    List<WorldBuyConstructionInfoNodeVM> children = new ArrayList<>();



    public WorldEmptyDetailNode(
            IdleMushroomWorldPlayScreen parent
            ) {
        super();
        this.screen = parent;


        this.constructionNameLabel = new Label("", parent.getGame().getMainSkin());
        constructionNameLabel.setWrap(true);

        this.buyCandidateKeyLabel = new Label("", parent.getGame().getMainSkin());
        // ------ this ------
        this.setBackground(parent.getGame().getTextureManager().getTableType3Drawable());
        this.pad(screen.getLayoutConst().WorldConstructionCellTablePad);
    }








    private void update() {
        // ------ update show-state ------
        if (construction == null) {
            setVisible(false);
            //textButton.setVisible(false);
            //Gdx.app.log("ConstructionView", this.hashCode() + " no model");
            return;
        } else {
            setVisible(true);
            //textButton.setVisible(true);
            //Gdx.app.log("ConstructionView", model.getDescriptionPackage().getName() + " set to its view");
        }
        // ------ update text ------

        constructionNameLabel.setText(construction.getDescriptionPackage().getName());
        buyCandidateKeyLabel.setText(construction.getDescriptionPackage().getExtraTexts().get(0));
    }


    @Override
    public void subLogicFrame() {
        update();
        children.forEach(it -> it.subLogicFrame());
    }

    @Override
    public void updateForNewConstruction(
            BaseConstruction construction,
            GridPosition position
    ) {
        this.construction = construction;
        this.position = position;

        this.clearChildren();
        final IdleMushroomPlayScreenLayoutConst playScreenLayoutConst = screen.getLayoutConst();
        int CHILD_WIDTH = playScreenLayoutConst.CONSTRUCION_CHILD_WIDTH;
        int CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_BUTTON_HEIGHT;
        int NAME_CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_NAME_HEIGHT;
        this.add(constructionNameLabel)
                .size(CHILD_WIDTH, NAME_CHILD_HEIGHT)
                .right();

        Container<?> questionMarkArea = new Container<>(new Image(screen.getGame().getTextureManager().getQuestionMarkTexture()));
        questionMarkArea.setBackground(screen.getGame().getTextureManager().getQuestionMarkTableDrawable());
        questionMarkArea.setTouchable(Touchable.enabled);
        questionMarkArea.addListener(new PopupBoardCallerClickListener<>(() -> construction, screen.getWikiPopupBoardCallback()));
        this.add(questionMarkArea)
                .width(screen.getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize)
                .height(screen.getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize)
                .left();

        this.row();
        this.add(buyCandidateKeyLabel);
        construction.getExistenceComponent().getBuyCandidateConfigs().stream()
                .limit(1)
                .forEach(constructionBuyCandidateConfig -> {
                    WorldBuyConstructionInfoNodeVM vm = new WorldBuyConstructionInfoNodeVM(
                            screen,
                            construction,
                            constructionBuyCandidateConfig
                    );
                    vm.update();
                    this.add(vm).grow();
                    children.add(vm);
                });

        update();
    }

    public static class WorldBuyConstructionInfoNodeVM extends Table {
        BaseIdleMushroomPlayScreen parent;
        BaseConstruction model;
        Label constructionNameLabel;
        TextButton buyButton;
        ConstructionBuyCandidateConfig constructionBuyCandidateConfig;
        DescriptionPackage descriptionPackage;
        Table costPart;
        public WorldBuyConstructionInfoNodeVM(
                BaseIdleMushroomPlayScreen parent,
                BaseConstruction model,
                ConstructionBuyCandidateConfig constructionBuyCandidateConfig
        ) {
            super();
            final IdleMushroomPlayScreenLayoutConst playScreenLayoutConst = parent.getLayoutConst();
            this.parent = parent;
            this.constructionBuyCandidateConfig = constructionBuyCandidateConfig;
            this.descriptionPackage = parent.getGame().getIdleMushroomExtraGameplayManager().getDescriptionPackageFactory().getConstructionDescriptionPackage(constructionBuyCandidateConfig.getPrototypeId());
            this.model = model;

            int CHILD_WIDTH = playScreenLayoutConst.CONSTRUCION_CHILD_WIDTH;
            int CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_BUTTON_HEIGHT;
            int NAME_CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_NAME_HEIGHT;

            this.constructionNameLabel = new Label("", parent.getGame().getMainSkin());
            constructionNameLabel.setWrap(true);

            this.buyButton = new TextButton(model.getDescriptionPackage().getTransformButtonText(), parent.getGame().getMainSkin());
            buyButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parent.getGame().getIdleGameplayExport().getGameplayContext()
                            .getConstructionManager()
                            .buyInstanceOfPrototype(constructionBuyCandidateConfig, model.getPosition());
                }
            });

            this.costPart = new Table(parent.getGame().getMainSkin());

            // ------ this ------
            this.add(constructionNameLabel).size(CHILD_WIDTH, CHILD_HEIGHT).row();
            this.add(costPart).row();
            this.add(buyButton).size(CHILD_WIDTH, CHILD_HEIGHT).row();
            this.setBackground(parent.getGame().getTextureManager().getTableType5Drawable());
        }

        public void update() {
            // ------ update show-state ------
            if (model == null) {
                setVisible(false);
                //textButton.setVisible(false);
                //Gdx.app.log("ConstructionView", this.hashCode() + " no model");
                return;
            } else {
                setVisible(true);
                //textButton.setVisible(true);
                //Gdx.app.log("ConstructionView", model.getDescriptionPackage().getName() + " set to its view");
            }
            // ------ update text ------

            constructionNameLabel.setText(
                descriptionPackage.getName()
            );
            costPart.clearChildren();
            ConstructionDetailPartVM.resourcePackAsActor(
                    constructionBuyCandidateConfig.getBuyDescriptionStart(),
                    constructionBuyCandidateConfig.getBuyCostPack(),
                    costPart, parent);
            // ------ update clickable-state ------
            boolean canBuyInstanceOfPrototype = parent.getGame().getIdleGameplayExport().getGameplayContext().getConstructionManager()
                    .canBuyInstanceOfPrototype(constructionBuyCandidateConfig, model.getPosition());
            if (canBuyInstanceOfPrototype) {
                buyButton.setDisabled(false);
                buyButton.getLabel().setColor(Color.WHITE);
            } else {
                buyButton.setDisabled(true);
                buyButton.getLabel().setColor(Color.RED);
            }
        }

        public void subLogicFrame() {
            update();
        }
    }
}
