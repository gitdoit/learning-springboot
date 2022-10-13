package org.seefly.springbasic.parambind.beans;

import lombok.Data;

import java.util.List;

@Data
public class RiskTableConfig {
    private List<Integer[]> one;
    private List<Integer[]> tow;
    private List<Integer[]> three;
    private List<Integer[]> four;
    private List<Integer[]> five;
}
