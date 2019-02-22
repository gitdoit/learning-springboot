package org.seefly.springwebsocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liujianxin
 * @date 2019-02-22 13:51
 */
@RequestMapping("/play")
public class AudioController {

    @GetMapping("/")
    public ModelAndView getAudio(String audioId){
        return null;
    }
}
