package hundun.gdxgame.idlemushroom.ui.shared;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.eskalon.commons.screen.ManagedScreen;
import hundun.gdxgame.idlemushroom.ui.screen.IdleMushroomMenuScreen;
import hundun.gdxgame.libv3.gamelib.starter.listerner.IGameAreaChangeListener;
import hundun.gdxgame.idlemushroom.logic.ProxyManager.ProxyState;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author hundun
 * Created on 2021/11/20
 */
public class IdleMushroomGameAreaControlBoard extends Table implements IGameAreaChangeListener {

    BaseIdleMushroomScreen parent;
    Map<String, Node> nodes = new LinkedHashMap<>();

    public IdleMushroomGameAreaControlBoard(BaseIdleMushroomScreen parent) {
        super();
        this.parent = parent;
        this.setBackground(parent.getGame().getTextureManager().getDefaultBoardNinePatchDrawable());
        this.padRight(0);

    }



    @Override
    public void onGameAreaChange(String last, String current) {
        rebuildChild(current);
    }

    private void rebuildChild(String current) {

        nodes.entrySet().forEach(entry -> {
            if (Objects.equals(entry.getKey(), current)) {
                entry.getValue().changeVersion(true);
            } else {
                entry.getValue().changeVersion(false);
            }

        });
    }

    public void lazyInit(List<Supplier<BaseIdleMushroomScreen>> controlBoardScreenIds) {

        nodes.clear();
        this.clear();

        for (Supplier<BaseIdleMushroomScreen> controlBoardScreenId : controlBoardScreenIds) {

            Node node = new Node(parent, controlBoardScreenId, false);
            nodes.put(controlBoardScreenId.get().screenId, node);
            this.add(node)
                    .width(parent.getLayoutConst().AREA_BOARD_BORDER_WIDTH)
                    .height(parent.getLayoutConst().AREA_BOARD_CELL_HEIGHT)
                    .row();
        }

        if (parent.getGame().debugMode) {
            Image proxyControlButton = new Image(parent.getGame().getTextureManager().getProxyDisabledIcon());
            proxyControlButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (parent.getGame().getProxyManager().getProxyState() == ProxyState.RUNNING) {
                        parent.getGame().getProxyManager().setProxyState(ProxyState.PAUSE);
                    } else {
                        parent.getGame().getProxyManager().setProxyState(ProxyState.RUNNING);
                    }
                    if (parent.getGame().getProxyManager().getProxyState() == ProxyState.RUNNING) {
                        proxyControlButton.setDrawable(new TextureRegionDrawable(parent.getGame().getTextureManager().getProxyEnabledIcon()));
                    } else {
                        proxyControlButton.setDrawable(new TextureRegionDrawable(parent.getGame().getTextureManager().getProxyDisabledIcon()));
                    }
                }
            });
            this.add(proxyControlButton)
                    .width(parent.getLayoutConst().AREA_BOARD_CELL_HEIGHT)
                    .height(parent.getLayoutConst().AREA_BOARD_CELL_HEIGHT)
                    .row();
        }


        rebuildChild(null);

    }

    public static class Node extends Table {

        BaseIdleMushroomScreen parent;
        //Image image;
        //Label label;

        Supplier<BaseIdleMushroomScreen> gotoScreenId;

        public Node(BaseIdleMushroomScreen parent, Supplier<BaseIdleMushroomScreen> gotoScreenId, boolean longVersion) {
            this.parent = parent;
            this.gotoScreenId = gotoScreenId;
            this.setTouchable(Touchable.enabled);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    parent.getGame().setLastScreenId(parent.getScreenId());
                    parent.getGame().getScreenManager().pushScreen(gotoScreenId.get(), null);
                    parent.getGame().getAudioPlayManager().intoScreen(gotoScreenId.get().getScreenId());
                }
            });
/*            this.label = new Label(gotoScreenId, parent.getGame().getMainSkin());
            this.add(label).right();*/
        }


        public void changeVersion(boolean longVersion) {
            this.setBackground(new TextureRegionDrawable(
                    parent.getGame().getTextureManager().getGameAreaTexture(gotoScreenId.get().screenId, longVersion)
            ));
/*            label.setVisible(longVersion);*/
        }

    }
}
