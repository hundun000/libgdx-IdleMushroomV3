package hundun.gdxgame.idleshare.gamelib.framework.data;

import hundun.gdxgame.idleshare.gamelib.framework.util.text.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2023/03/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemSettingSaveData {
    Language language;
}
