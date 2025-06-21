package hundun.gdxgame.idleshare.gamelib.framework.model.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementDescriptionPackage {
    String name;
    String wikiText;
    String congratulationText;
    String descriptionReplaceConstructionPrototypeId;
}
