package org.seefly.lambda.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liujianxin
 * @date 2019-01-16 18:14
 */
public class Parallel {

    @Test
    public void testParallel() throws InterruptedException {
        List<String> list = new ArrayList<>();
        list.parallelStream().collect(Collectors.toList());
    }
}
