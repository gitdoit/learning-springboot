package org.seefly.springweb.controller;

import org.seefly.springweb.annotation.MyParamAnno;
import org.seefly.springweb.component.AnnoArgumentResolver;
import org.seefly.springweb.controller.request.AbstractQuestion;
import org.seefly.springweb.controller.request.MultipleChoiceQuestion;
import org.seefly.springweb.controller.request.SingleChoiceQuestion;
import org.seefly.springweb.controller.request.UserRequest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.List;

/**
 * 参数绑定
 *  DispatcherServlet#doDispatch
 *      AbstractHandlerMethodAdapter#handle
 *          RequestMappingHandlerAdapter#handleInternal
 *
 *              RequestMappingHandlerAdapter#invokeHandlerMethod
 *               这一步创建{@link ServletInvocableHandlerMethod}并把事先定义好的参数解析器和返回值处理器放进去。
 *
 *                 ServletInvocableHandlerMethod#invokeAndHandle
 *                 这一步调用父类的也就是下面这个方法，获取控制器处理后的返回值，将返回值用returnValueHandler处理。
 *
 *                      InvocableHandlerMethod#invokeForRequest
 *                      这一步会调用参数解析器来准备一下调用目标控制器需要的那些参数，将这些参数从请求中拿出来
 *                      类型啥的都转换好。
 *
 *                          InvocableHandlerMethod#getMethodArgumentValues
 *                          真正开始调用参数解析器的方法，遍历参数解析器..然后流程你懂的。咱们的
 *                          RequestResponseBodyMethodProcessor就在这一步被调用
 *                      InvocableHandlerMethod#doInvoke(java.lang.Object...)
 *                      使用反射调用目标控制器方法。
 *
 *
 *   {@link RequestResponseBodyMethodProcessor} 实现 {@link HandlerMethodArgumentResolver} and {@link HandlerMethodReturnValueHandler}
 *    并且组合了N个{@link HttpMessageConverter}，并在{@link RequestMappingHandlerAdapter#afterPropertiesSet()}完成初始化。
 *    然后在RequestMappingHandlerAdapter#invokeHandlerMethod这里被调用。
 *
 *    那么参数解析器、返回值处理器和消息转换器有什么关系呢？？
 *    就我目前看来，消息转换器被组合到{@link RequestPartMethodArgumentResolver} {@link RequestResponseBodyMethodProcessor}里
 *
 *  {@link ServletModelAttributeMethodProcessor} 这个东西就是用来绑定表单数据到javaBean的
 *
 *
 *
 * 
 * 
 * 
 * @author liujianxin
 * @date 2019-07-04 22:18
 */
@RestController
@RequestMapping("/message")
public class MessageConverterController {


    /**
     * 测试自定义注解配合自定义消息解析器
     * 我自定义的消息解析器在检测到方法的参数上含有自定义注解@myParamAnno时会启用自定义
     * 消息转解析转换消息{@link AnnoArgumentResolver}
     */
    @RequestMapping(value = "/my-argument-resolver",method = RequestMethod.GET)
    public String testCustom(@MyParamAnno String name){
        System.out.println(name);
        return name;
    }


    /**
     * 演示多态序列化，能够根据传入的数据不同进行不同的序列化。
     * 父类做参数签名，实际传入子类，并能够正确序列化。
     */
    @PostMapping("/multi-type")
    public String multiSer(@RequestBody AbstractQuestion question){
        System.out.println(question);
        if (question instanceof SingleChoiceQuestion){
            System.out.println("单选题");
        }else if(question instanceof MultipleChoiceQuestion){
            System.out.println("多选题");
        }
        return "OK";
    }


    /**
     *
     * 使用自定义的Converter来转换参数
     * {@link ServletModelAttributeMethodProcessor}
     *
     *
     */
    @RequestMapping("/date-converter")
    public String dateConverter(UserRequest request){
        System.out.println(request);
        return "OK";
    }


    /**
     * ModelAttribute 注解，在这里起作用{@link ModelFactory#getNameForParameter}
     */
    @RequestMapping("/converter")
    public String converter(@ModelAttribute("request") UserRequest request){
        System.out.println(request);
        return "OK";
    }

    @RequestMapping("/converter-list")
    public String converterList(List<UserRequest> requests) {
        System.out.println(requests);
        return "OK";
    }

}
