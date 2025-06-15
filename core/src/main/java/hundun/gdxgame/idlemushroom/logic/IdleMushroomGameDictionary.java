package hundun.gdxgame.idlemushroom.logic;

import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.model.achievement.AchievementManager.AchievementState;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author hundun
 * Created on 2021/11/22
 */
public class IdleMushroomGameDictionary {

    public static final class LanguageCode {

        public static final String ENGLISH = "en";
        public static final String CHINESE = "zh";

        public static final Locale localeEN = new Locale(ENGLISH);
        public static final Locale localeZH = new Locale(CHINESE);

        public static final Map<String, Locale> displayLanguageMap = Map.of(
            localeEN.getDisplayLanguage(localeEN), localeEN,
            localeZH.getDisplayLanguage(localeZH), localeZH
        );

        public static final List<Locale> values = new ArrayList<>(displayLanguageMap.values());
    }

    public List<String> getMenuScreenTexts(Locale locale) {
        switch (locale.getLanguage()) {
            case LanguageCode.CHINESE:
                return JavaFeatureForGwt.arraysAsList("放置蘑菇", "新游戏", "继续游戏", "语言", "重启后生效");
            default:
                return JavaFeatureForGwt.arraysAsList("Idle Mushroom", "New Game", "Continue", "Language", "Take effect after restart");
        }
    }


    public List<String> getAchievementTexts(Locale locale)
    {
        switch (locale.getLanguage())
        {
            case LanguageCode.CHINESE:
                return JavaFeatureForGwt.listOf(
                        "任务：", "状态：", "回到游戏", "奖励内容：", "领取",
                        "无"
                );
            default:
                return JavaFeatureForGwt.listOf(
                        "Quest: ", "status: ", "back", "reward: ", "Get it",
                        "None"
                );
        }
    }


    public List<String> getStageSelectMaskBoardTexts(Locale locale)
    {
        return null;
    }

    public String achievementStatus(Locale locale, AchievementState state) {
        switch (locale.getLanguage()) {
            case LanguageCode.CHINESE:
                switch (state) {
                    case LOCKED:
                        return "未解锁";
                    case RUNNING:
                        return "进行中";
                    case COMPLETED:
                        return "已完成";
                    default:
                        return "[dic lost]";
                }
            default:
                switch (state) {
                    case LOCKED:
                        return "locked";
                    case RUNNING:
                        return "in progress";
                    case COMPLETED:
                        return "completed";
                    default:
                        return "[dic lost]";
                }
        }
    }
}
