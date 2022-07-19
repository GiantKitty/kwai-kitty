package com.rs.service.home;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
@Lazy
@Service
public class CreatorHomeServiceImpl implements CreatorHomeService{

    @Autowired
    private List<CreatorHomeSectionService> sectionServices;

    private Map<String, CreatorHomeSectionService> serviceMap = Collections.emptyMap();

    @PostConstruct
    public void init() {
        serviceMap = sectionServices.stream()
                .collect(Collectors.toMap(CreatorHomeSectionService::name, Function.identity()));
    }

    @Override
    public Map<String, Object> buildHomeSections(long userId, String username) {
        Map<String, Object> models = new HashMap<>();
        serviceMap.values().forEach(service -> {
            if (service.enable(userId)) {
                models.put(service.name(), service.build(userId, username));
            }
        });
        return models;
    }
}
