package hundun.gdxgame.idleshare.gamelib.framework.data;

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
    String localeLanguageTag;
}
