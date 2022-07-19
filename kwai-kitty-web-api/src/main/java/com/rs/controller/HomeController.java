package com.rs.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rs.service.home.CreatorHomeService;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
@Lazy
@RestController
public class HomeController {

    @Autowired
    private CreatorHomeService creatorHomeService;

    @RequestMapping("/rest/o/w/creator/home")
    public Map<String, Object> home(@RequestParam long userId, @RequestBody String username) {
        return creatorHomeService.buildHomeSections(userId, username);
    }

}
