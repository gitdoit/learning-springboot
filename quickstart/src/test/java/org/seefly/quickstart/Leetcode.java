package org.seefly.quickstart;

import org.junit.Test;

/**
 * @author liujianxin
 * @date 2018-08-05 17:16
 * 描述信息：
 **/
public class Leetcode {


    @Test
    public void test1() {
        String str = "babadada";
        System.out.println(longestPalindrome(str));
    }

    public String longestPalindrome(String s) {
        int start, end;
        String temp, result = "".equals(s) ? "" : s.charAt(0) + "";
        StringBuilder palindrome;
        for (start = 0; start < s.length(); start++) {
            end = s.lastIndexOf(s.charAt(start));

            temp = s.substring(start, end + 1);
            palindrome = new StringBuilder(temp).reverse();
            for(;!palindrome.toString().equals(temp);){
            }

            do {
                end = s.lastIndexOf(s.charAt(start));
                temp = s.substring(start, end + 1);
                palindrome = new StringBuilder(temp).reverse();

            } while (!palindrome.toString().equals(temp));

            end = s.lastIndexOf(s.charAt(start));
            temp = s.substring(start, end + 1);
            while (!palindrome.reverse().toString().equals(temp)) {


            }
            if (palindrome.reverse().toString().equals(temp)) {
                if (result.length() < temp.length()) {
                    result = temp;
                }
            } else {
                String tempResult = longestPalindrome(s.substring(start, end));
                if (result.length() < tempResult.length()) {
                    result = tempResult;
                }
            }
        }
        return result;
    }
}
