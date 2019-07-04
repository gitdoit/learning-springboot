package org.seefly.springweb.controller;

import org.seefly.springweb.controller.request.ConverterRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujianxin
 * @date 2019-07-04 22:18
 */
@RestController
@RequestMapping
public class ParamConverterController {

    @RequestMapping("/date-converter")
    public String dateConverter( ConverterRequest request){
        System.out.println(request);
        return null;
    }
}
