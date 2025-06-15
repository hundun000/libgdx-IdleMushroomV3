package hundun.gdxgame.idleshare.gamelib.framework.model.construction;

import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;
import lombok.Getter;

import java.util.Locale;

@Getter
public abstract class AbstractConstructionPrototype {

    protected String prototypeId;
    protected Locale locale;

    protected DescriptionPackage descriptionPackage;

    public AbstractConstructionPrototype(
        String prototypeId,
        Locale locale
    )
    {
        this.prototypeId = prototypeId;
        this.locale = locale;


    }


    public abstract BaseConstruction getInstance(GridPosition position);
}
