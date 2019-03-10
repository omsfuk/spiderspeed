package cn.omsfuk.spider.speed;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class Result {

    public Map<String, String> resultMap = new HashMap<>();

    public String get(String key) {
        return resultMap.get(key);
    }

    public void put(String key, String value) {
        resultMap.put(key, value);
    }

    public Collection<String> values() {
        return resultMap.values();
    }

    public List<String> keys() {
        return resultMap.keySet().stream().collect(Collectors.toList());
    }
}
