package com.globalegrow.esearch.stat.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *
 *  File: BaseController.java
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
public class BaseController {

    protected static final Logger log = LogManager.getLogger(BaseController.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> resolveException(HttpServletRequest request, Exception ex) {
        Map<String, Object> result = new LinkedHashMap<>(2);
        if (ex instanceof HttpMessageNotReadableException) {
            log.error("Json format error.", ex);
        } else {
            log.error("Missing system error.", ex);
        }
        result.put("code", 500);
        result.put("msg", ex.getMessage());
        return result;
    }

}
