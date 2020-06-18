package org.seefly.webflux.domain;

import java.math.BigDecimal;

public record Book(String title, BigDecimal price,String category) {
}
