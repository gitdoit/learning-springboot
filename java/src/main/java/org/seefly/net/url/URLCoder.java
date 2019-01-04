package org.seefly.net.url;

import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 本类用来演示当网址中出现了非西欧字符时需要用URLEncoder将之转换成‘乱码’
 * 在用URLDecoder将‘乱码’解码
 * 注：如果仅包含西欧字符那么 将无需转换。
 * 转换 方法：每个中文字符占两个字节，每个字节可以转换成2个十六进制的数字，所以每个中文字符
 * 将转换成%XX%XX的形式。当使用不同的字符集时中文所占用的字节不同所以解码编码时要指定字符集
 * @author liujianxin
 */
public class URLCoder {

    public static void main(String[] args) throws Exception {
        //使用GBK编码表将字符转换成URL中对应的形式
        String keyword = URLEncoder.encode("柯南", "GBK");
        System.out.println(keyword);

        //将‘乱码’转换成字符
        String chr = URLDecoder.decode(keyword,"GBK");
        System.out.println(chr);

    }
}
