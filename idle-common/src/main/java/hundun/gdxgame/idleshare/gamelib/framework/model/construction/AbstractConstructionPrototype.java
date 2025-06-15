package hundun.gdxgame.idleshare.gamelib.framework.model.construction;

import hundun.gdxgame.idleshare.gamelib.export.IIdleFrontend;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;
import lombok.Getter;

import java.util.Locale;

@Getter
public abstract class AbstractConstructionPrototype {

    protected String prototypeId;


    public AbstractConstructionPrototype(
        String prototypeId
    )
    {
        this.prototypeId = prototypeId;



    }


    public abstract BaseConstruction getInstance(GridPosition position, DescriptionPackage descriptionPackage);
}
