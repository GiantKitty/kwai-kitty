package com.rs.service.home;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
public interface CreatorHomeSectionService<T> {

    String name();

    default boolean enable(long userId) {
        return true;
    }

    T build(long userId, String username);

}
