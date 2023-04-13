package org.caotc.unit4j.core;

import lombok.NonNull;

/**
 * 有主键的
 *
 * @author caotc
 * @date 2018-09-28
 * @since 1.0.0
 **/
public interface Identifiable {

    /**
     * 获取{@code this}主键
     *
     * @return 主键
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
  String id();
}
