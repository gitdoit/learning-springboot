package org.seefly.springweb.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * @author liujianxin
 * @date 2020/3/5 14:40
 */
public class ToString {

    private static  String  beanToXml(Object obj){
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            marshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("xml序列化失败",e);
        }
    }
}
