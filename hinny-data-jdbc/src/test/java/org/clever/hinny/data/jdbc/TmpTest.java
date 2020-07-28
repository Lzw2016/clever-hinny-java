package org.clever.hinny.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/10 21:22 <br/>
 */
@Slf4j
public class TmpTest {

    @Test
    public void t01() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE - 10);
        for (int i = 0; i <= 15; i++) {
            log.info("# -> {}", atomicInteger.incrementAndGet());
        }
        log.info("# ==================================================================================");
        atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        for (int i = 0; i <= 15; i++) {
            log.info("# -> {}", atomicInteger.incrementAndGet());
        }
    }
}
