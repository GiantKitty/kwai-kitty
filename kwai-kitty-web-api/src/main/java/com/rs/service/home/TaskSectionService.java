package com.rs.service.home;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
@Slf4j
@Lazy
@Service
@Order(2)
public class TaskSectionService implements CreatorHomeSectionService<String>{

    @Override
    public String name() {
        return "task";
    }

    @Override
    public boolean enable(long userId) {
        return CreatorHomeSectionService.super.enable(userId);
    }

    @Override
    public String build(long userId, String username) {
        return username + ": task section, user=" + userId;
    }
}
