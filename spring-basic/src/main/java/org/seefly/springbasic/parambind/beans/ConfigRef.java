package org.seefly.springbasic.parambind.beans;

import lombok.Data;

@Data
public class ConfigRef {
    private String driver;
    private Option options;

    @Data
    public static class Option {
        private String maxSize;
        private String maxFile;

    }
}
