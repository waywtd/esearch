package com.globalegrow.esearch.stat.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *
 *  File: HBaseController.java
 *
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年07月05日           nieruiqun               Initial.
 *
 * </pre>
 */
@RestController
@RequestMapping("/data/keyword")
public class KeywordDailyController extends BaseController {

    @RequestMapping(value = "/daily", method = {RequestMethod.GET, RequestMethod.POST})
    public String daily(){
        log.info("====================================>");
        return "test";
    }


}
