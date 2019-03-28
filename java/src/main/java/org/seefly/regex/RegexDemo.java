package org.seefly.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://www.jianshu.com/p/9c4979a3b7e4
 *
 * https://www.jb51.net/tools/zhengze.html
 *
 *
 * @author liujianxin
 * @date 2019-03-18 10:42
 */
public class RegexDemo {

    /**
     * 1.[abc] : 表示可能是a，可能是b，也可能是c。
     *      abc中的任意一个，如果元素不是由单个字符组成则需要这样 (ab|b|c)
     * 2.[^abc]: 表示不是a,b,c中的任意一个
     * 3.[a-zA-Z]: 表示是英文字母
     * 4.[0-9]:表示是数字
     */
    @Test
    public void testRange(){
        boolean match = match("[你我他]们", "我们今天很开心");
        boolean match1 = match("[^你我他]们", "我们今天很开心");
        System.out.println("1:"+match);
        System.out.println("2:"+match1);
    }

    /**
     * .：匹配任意的字符
     * \d：表示数字
     * \D：表示非数字
     * \s：表示由空字符组成，[ \t\n\r\x\f]
     * \S：表示由非空字符组成，[^\s]
     * \w：表示字母、数字、下划线，[a-zA-Z0-9_]
     * \W：表示不是由字母、数字、下划线组成
     */
    @Test
    public void testChar(){
        // 简单的11位手机号校验
        boolean match = match("^1\\d{10}", "13665555555");
        System.out.println("手机号:"+match);

        // 简单的hello world校验
        boolean match1 = match("^\\w+\\s\\w+", "hello world");
        System.out.println("hello world:"+match1);

    }

    /**
     * ？ 		表示出现0次或多次
     * +  		表示出现一次或多次
     * *  		表示出现0次、1次或多次
     * {n} 	    表示出现n次
     * {n~m}    表示出现n~m次
     * {n,}	    表示出现n次及以上
     */
    public void testTimes(){
        boolean match = match("s?", "dfdfdf");
        System.out.println();
    }


    @Test
    public void testE(){
        String text = "测试(副本2)";
        Pattern pattern =Pattern.compile("\\(副本\\d*\\)$");
        Matcher matcher=pattern.matcher(text);
        if(matcher.find()){
            String group = matcher.group();
            System.out.println(matcher.group());
        }
    }







    @Test
    public void test(){
        //[^没有|不]有?需要
        // 我没有需要  我暂时不需要  我不需要  我真的不需要 我现在比较需要 我现在还不需要
        System.out.println(match("[^没有不]有?需要","我没有需要"));
    }


    @Test
    public void testTalk(){
        // 你说一下  你讲一下  你说  你讲
        boolean match = match("^你(说|讲)(一下)?$", "你讲一下");
        System.out.println(match);
    }

    @Test
    public void testOK(){
        // 行啊   ^(?<!不)[好行][啊的呀]?
        boolean match = match("^[好行是][啊的呀]?", "你是谁");
        System.out.println(match);
    }


    @Test
    public void testNoNeed(){
        // 我不需要  我没需要 没需要 暂时不需要
        boolean match = match("(不|没|没有)需要", "暂时不需要");
        System.out.println(match);
    }

    @Test
    public void testNoOK(){
        // 不考虑  不感兴趣 不用 不要 不愿意 不想
        boolean match = match("(不|没|没有).*(考虑|兴趣|用|原意|想)", "没想法");
        System.out.println(match);
    }


    private boolean match(String reg,String word){
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(word);
        return matcher.find();
    }


}
