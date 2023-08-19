/*
 * Copyright (C) 2023 the original author or authors.
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

package org.caotc.unit4j.support.mybatis.factory;

import com.google.common.collect.ImmutableMap;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.common.util.QuantityUtil;
import org.caotc.unit4j.support.mybatis.constant.AmountPropertyConstant;

import java.util.function.Function;

/**
 * @author caotc
 * @date 2020-07-01
 * @since 1.0.0
 */
@Slf4j
@Value
public class AmountPropertyObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return !QuantityUtil.writableAmountPropertiesFromClass(object.getClass()).isEmpty();
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new AmountPropertyObjectWrapper(metaObject, object);
    }

    public static class AmountPropertyObjectWrapper extends BeanWrapper {
        ImmutableMap<String, AccessibleProperty<Object, Quantity>> propertyNameToAmountProperties;

        public AmountPropertyObjectWrapper(MetaObject metaObject, Object object) {
            super(metaObject, object);
            propertyNameToAmountProperties = QuantityUtil.accessibleAmountPropertyStreamFromClass(object)
                    .collect(ImmutableMap.toImmutableMap(Property::name, Function.identity()));
        }

        @Override
        public void set(PropertyTokenizer prop, Object value) {
            if (prop.getName().contains(AmountPropertyConstant.DELIMITER)) {
                prop = new PropertyTokenizer(prop.getName().replaceAll(AmountPropertyConstant.DELIMITER, StringConstant.DOT));
                if (propertyNameToAmountProperties.containsKey(prop.getName())) {
                    AccessibleProperty<Object, Quantity> property = propertyNameToAmountProperties.get(prop.getName());
                    Quantity quantity = property.read(metaObject.getOriginalObject()).orElse(Quantity.UNKNOWN);
                    if (prop.getChildren().equals(Quantity.Fields.VALUE)) {
                        //TODO value类型处理
                        quantity = quantity.withValue((Number) value);
                    }
                    if (prop.getChildren().equals(Quantity.Fields.UNIT)) {
                        quantity = quantity.withUnit((Unit) value);
                    }
                    property.write(metaObject.getOriginalObject(), quantity);
                }
            } else {
                super.set(prop, value);
            }
        }
    }
}