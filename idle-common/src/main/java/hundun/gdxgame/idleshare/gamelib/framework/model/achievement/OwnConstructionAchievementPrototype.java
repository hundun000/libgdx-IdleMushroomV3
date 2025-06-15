package hundun.gdxgame.idleshare.gamelib.framework.model.achievement;

import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.resource.ResourcePair;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OwnConstructionAchievementPrototype extends AbstractAchievementPrototype {
    public Map<String, Entry<Integer, Integer>> requirementMap;


    public OwnConstructionAchievementPrototype(
            String id,
            Map<String, Entry<Integer, Integer>> requirementMap,
            List<ResourcePair> awardResourceMap,
            List<String> nextAchievementId
    )

    {
        super(id, awardResourceMap, nextAchievementId);
        this.requirementMap = requirementMap;
    }

    @Override
    public boolean checkComplete()
    {
        List<BaseConstruction> allConstructions = gameplayContext.getConstructionManager().getAllConstructionInstances();

        for (Entry<String, Entry<Integer, Integer>> requiredEntry : requirementMap.entrySet())
        {
            int requiredAmount = requiredEntry.getValue().getKey();
            int requiredLevel = requiredEntry.getValue().getValue();
            List<String> targetConstructionPrototypeIds = JavaFeatureForGwt.listOf(requiredEntry.getKey().split("\\|"));
            boolean matched = allConstructions.stream()
                    .filter(it -> targetConstructionPrototypeIds.contains(it.getPrototypeId()))
                    .filter(it -> it.getSaveData().getLevel() >= requiredLevel)
                    .count() >= requiredAmount;
            if (!matched)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void lazyInitDescription(IdleGameplayContext gameplayContext) {
        super.lazyInitDescription(gameplayContext);
    }

    public static class Companion {
        /**
         * textMap.value : String name, String description, String congratulationText <br>
         * requireds.value: key requiredAmount ; value requiredLevel;
         */
        public static void quickAddOwnConstructionAchievement(
                Map<String, AbstractAchievementPrototype> map,
                String id,
                Map<String, Entry<Integer, Integer>> requireds,
                List<String> nextAchievementId,
                ResourcePair... awardResourceMap
        )
        {
            AbstractAchievementPrototype achievement =  new OwnConstructionAchievementPrototype(
                    id,
                    requireds,
                    JavaFeatureForGwt.listOf(awardResourceMap),
                    nextAchievementId
            );


            map.put(id, achievement);
        }
    }
}
