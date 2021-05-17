package by.kuzmich.statistics;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Statistics {

    private final Map<StatisticsService, Integer> statisticsMap = new HashMap<>();

    public void addStatistics(Map<StatisticsService, Integer> serviceMap) {
        checkNotNull(serviceMap, "Service map must exist");

        serviceMap.forEach((key, value) -> {
            createEntryIfNotExists(key);

            statisticsMap.put(key, statisticsMap.get(key) + value);
            log.info("Statistics: {} = {}", key, statisticsMap.get(key));
        });
    }

    private boolean createEntryIfNotExists(StatisticsService service) {
        checkNotNull(service, "Service must exist");

        if (!statisticsMap.containsKey(service)) {
            statisticsMap.put(service, 0);
            return true;
        }
        return false;
    }

    public Map<StatisticsService, Integer> getStatisticsMap() {
        return Map.copyOf(statisticsMap);
    }

}
