package org.caotc.unit4j.core.serializer;

import org.caotc.unit4j.core.Alias;

/**
 * 有别名的对象的{@link Alias}未注册时的策略枚举
 *
 * @author caotc
 * @date 2019-05-18
 * @since 1.0.0
 */
public enum AliasUndefinedStrategy {
    /**
     * 抛出异常
     */
    THROW_EXCEPTION,
    /**
     * 自动组合
     */
    AUTO_COMPOSITE,
    ID;
}
