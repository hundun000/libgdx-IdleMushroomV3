package hundun.gdxgame.idlemushroom.ui.world;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomScreenContext.IdleMushroomPlayScreenLayoutConst;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomWorldPlayScreen;
import hundun.gdxgame.idlemushroom.ui.shared.BaseCellDetailNodeVM;
import hundun.gdxgame.idlemushroom.ui.shared.BaseIdleMushroomPlayScreen;
import hundun.gdxgame.idleshare.core.framework.PopupBoardCallerClickListener;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;


/**
 * @author hundun
 * Created on 2021/11/05
 */
public class WorldTreeDetailNode extends BaseCellDetailNodeVM {
    BaseIdleMushroomPlayScreen parent;
    BaseConstruction model;
    Label constructionNameLabel;
    Label positionLabel;




    public WorldTreeDetailNode(
            IdleMushroomWorldPlayScreen parent
            ) {
        super();
        final IdleMushroomPlayScreenLayoutConst playScreenLayoutConst = parent.getLayoutConst();
        this.parent = parent;



        int CHILD_WIDTH = playScreenLayoutConst.CONSTRUCION_CHILD_WIDTH;
        int CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_BUTTON_HEIGHT;
        int NAME_CHILD_HEIGHT = playScreenLayoutConst.CONSTRUCION_CHILD_NAME_HEIGHT;

        this.constructionNameLabel = new Label("", parent.getGame().getMainSkin());
        constructionNameLabel.setWrap(true);

        this.positionLabel = new Label("", parent.getGame().getMainSkin());


        Container<?> questionMarkArea = new Container<>(new Image(parent.getGame().getTextureManager().getQuestionMarkTexture()));
        questionMarkArea.setBackground(parent.getGame().getTextureManager().getQuestionMarkTableDrawable());
        questionMarkArea.setTouchable(Touchable.enabled);
        questionMarkArea.addListener(new PopupBoardCallerClickListener<>(() -> model, parent.getWikiPopupBoardCallback()));

        // ------ leftPart ------
        this.add(constructionNameLabel).size(CHILD_WIDTH, NAME_CHILD_HEIGHT);
        this.add(questionMarkArea).size(parent.getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize, parent.getIdleMushroomPlayScreenLayoutConst().questionMarkAreaSize);
        this.row();
        this.setBackground(parent.getGame().getTextureManager().getTableType3Drawable());

    }







    private void update() {
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
        constructionNameLabel.setText(JavaFeatureForGwt.stringFormat(
                "%s",
                model.getDescriptionPackage().getName()
        ));
        positionLabel.setText(model.getSaveData().getPosition().toShowText());

    }

    @Override
    public void updateForNewConstruction(BaseConstruction construction, GridPosition position) {
        this.model = construction;
        update();
    }

    @Override
    public void subLogicFrame() {
        update();
    }

}
