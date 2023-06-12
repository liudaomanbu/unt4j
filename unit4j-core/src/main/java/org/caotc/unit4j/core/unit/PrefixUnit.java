/*
 * Copyright (C) 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 有词头的单位
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@Getter
public abstract class PrefixUnit extends Unit {
    /**
     * 组合该对象的词头和标准单位的id和别名的方法
     *
     * @param prefix       词头id或别名
     * @param standardUnit 准单位id或别名
     * @return 组合后的id或别名
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    public static String composite(@NonNull String prefix, @NonNull String standardUnit) {
        return StringConstant.UNDERLINE_JOINER.join(prefix, standardUnit);
    }

    /**
     * 词头
     */
    @NonNull
    Prefix prefix;

    protected PrefixUnit(@NonNull Prefix prefix) {
        this.prefix = prefix;
        Preconditions.checkArgument(!prefix().isEmpty());
    }

    /**
     * 该词头单位的标准单位
     *
     * @return 标准单位
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    public abstract StandardUnit standardUnit();

    @Override
    @NonNull
    public UnitType type() {
        return standardUnit().type();
    }

    @Override
    @NonNull
    public ImmutableMap<Unit, Integer> componentToExponents() {
        return ImmutableMap.of(this, 1);
    }

    @Override
    @NonNull
    public String id() {
        return composite(prefix().id(), standardUnit().id());
    }
}
