package hundun.gdxgame.idleshare.gamelib.framework.model.history;

import hundun.gdxgame.idleshare.gamelib.framework.IdleGameplayContext;
import hundun.gdxgame.libv3.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.libv3.gamelib.starter.listerner.ILogicFrameListener;
import hundun.gdxgame.idleshare.gamelib.framework.listener.IResourceChangeListener;
import hundun.gdxgame.idleshare.gamelib.framework.model.event.EventManager.OneSecondResourceChangeEvent;
import hundun.gdxgame.idleshare.gamelib.framework.model.event.EventManager.OneSecondResourceChangeEventOneTagData;
import hundun.gdxgame.idleshare.gamelib.framework.model.resource.StorageManager.ModifyResourceTag;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 记录游戏进度：
 * - 建筑变化；
 * - 资源变化（每隔一段时间记录）；
 */
public class HistoryManager implements IResourceChangeListener, ILogicFrameListener {
    private final List<OneSecondResourceChangeEventOneTagData> outputHistoryList = new ArrayList<>();
    private final IdleGameplayContext gameplayContext;
    @Getter
    private List<ProxyRunRecord> proxyRunRecords = new ArrayList<>();


    public HistoryManager(IdleGameplayContext gameplayContext) {
        this.gameplayContext = gameplayContext;
    }

    public void addProxyRunRecord(ProxyActionType actionType, Object... extras) {
        proxyRunRecords.add(
                ProxyRunRecord.builder()
                        .second(gameplayContext.getIdleFrontend().getSecond())
                        .actionType(actionType)
                        .extra(JavaFeatureForGwt.listOf(extras).stream()
                                .map(it -> it.toString())
                                .collect(Collectors.toList())
                        )
                        .build()
        );
    }

    public void addProxyRunRecordTypeLogSaveCurrentResult(ProxyGameSituationDTO gameSituationDTO) {
        proxyRunRecords.add(
                ProxyRunRecord.builder()
                        .second(gameplayContext.getIdleFrontend().getSecond())
                        .actionType(ProxyActionType.LogSaveCurrentResult)
                        .situation(gameSituationDTO)
                        .build()
        );
    }

    /**
     *
     */
    private void addProxyRunRecordTypeLogResourcesDeltaMap() {
        Map<String, Float> avgResourceDeltaMap = new HashMap<>();
        outputHistoryList.stream()
                .forEach(it -> {
                    it.getSecondChangeMap().forEach((k, v) -> {
                        avgResourceDeltaMap.merge(k, v * 1.0f / outputHistoryList.size(), (o, n) -> o + n);
                    });
                });
        outputHistoryList.clear();
        proxyRunRecords.add(
                ProxyRunRecord.builder()
                        .second(gameplayContext.getIdleFrontend().getSecond())
                        .actionType(ProxyActionType.LogResourcesDeltaMap)
                        .avgResourceDeltaMap(avgResourceDeltaMap)
                        .build()
        );
    }

    @Override
    public void onResourceChange(OneSecondResourceChangeEvent event) {
        outputHistoryList.add(event.getTagDataMap().get(ModifyResourceTag.OUTPUT));
    }



    public void onLogicFrame() {
        // try LogSaveCurrentResult
        if (gameplayContext.getIdleFrontend().modLogicFrameSecondZero(5))
        {
            this.addProxyRunRecordTypeLogResourcesDeltaMap();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProxyGameSituationDTO {
        Map<String, Long> ownResources;
        List<ConstructionSituation> constructionSituations;

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConstructionSituation {
        public String prototypeId;
        private int level;
        private int count;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProxyRunRecord {
        float second;
        ProxyActionType actionType;
        List<String> extra;
        ProxyGameSituationDTO situation;
        Map<String, Float> avgResourceDeltaMap;
    }

    public enum ProxyActionType {
        LogSaveCurrentResult, doUpgrade, LogResourcesDeltaMap, changeWorkingLevel, proxyCauseExit, buyInstanceOfPrototype

    }
}
