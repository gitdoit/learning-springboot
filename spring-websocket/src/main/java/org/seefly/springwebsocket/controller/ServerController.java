package org.seefly.springwebsocket.controller;

import org.seefly.springwebsocket.context.WebSocketSessionHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * @author liujianxin
 * @date 2019-02-25 18:12
 */
@RequestMapping("/send")
@RestController
public class ServerController {

    @GetMapping("/{msg}")
    public String getMessage(@PathVariable("msg") String mes) throws IOException {
        WebSocketSessionHolder.getAny().sendMessage(new TextMessage(mes));
        return "OK";
    }
}
