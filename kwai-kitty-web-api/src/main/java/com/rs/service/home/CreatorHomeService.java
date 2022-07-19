package com.rs.service.home;

import java.util.Map;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
public interface CreatorHomeService {

    Map<String, Object> buildHomeSections(long userId, String username);
}
