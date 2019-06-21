/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.enhanced.dynamodb.converter.attribute.bundled;

import java.util.concurrent.atomic.AtomicInteger;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.ConversionContext;
import software.amazon.awssdk.enhanced.dynamodb.converter.string.bundled.AtomicIntegerStringConverter;
import software.amazon.awssdk.enhanced.dynamodb.model.ItemAttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.TypeConvertingVisitor;
import software.amazon.awssdk.enhanced.dynamodb.model.TypeToken;

/**
 * A converter between {@link AtomicInteger} and {@link ItemAttributeValue}.
 *
 * <p>
 * This stores values in DynamoDB as a number.
 *
 * <p>
 * This supports reading numbers between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} from DynamoDB. For smaller
 * numbers, consider using {@link ShortAttributeConverter}. For larger numbers, consider using {@link LongAttributeConverter}
 * or {@link BigIntegerAttributeConverter}. Numbers outside of the supported range will cause a {@link NumberFormatException}
 * on conversion.
 *
 * <p>
 * This does not support reading decimal numbers. For decimal numbers, consider using {@link FloatAttributeConverter},
 * {@link DoubleAttributeConverter} or {@link BigDecimalAttributeConverter}. Decimal numbers will cause a
 * {@link NumberFormatException} on conversion.
 *
 * <p>
 * This can be created via {@link #create()}.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public final class AtomicIntegerAttributeConverter implements AttributeConverter<AtomicInteger> {
    private static final Visitor VISITOR = new Visitor();
    private static final AtomicIntegerStringConverter STRING_CONVERTER = AtomicIntegerStringConverter.create();

    private AtomicIntegerAttributeConverter() {}

    @Override
    public TypeToken<AtomicInteger> type() {
        return TypeToken.of(AtomicInteger.class);
    }

    public static AtomicIntegerAttributeConverter create() {
        return new AtomicIntegerAttributeConverter();
    }

    @Override
    public ItemAttributeValue toAttributeValue(AtomicInteger input, ConversionContext context) {
        return ItemAttributeValue.fromNumber(STRING_CONVERTER.toString(input));
    }

    @Override
    public AtomicInteger fromAttributeValue(ItemAttributeValue input,
                                            ConversionContext context) {
        return input.convert(VISITOR);
    }

    private static final class Visitor extends TypeConvertingVisitor<AtomicInteger> {
        private Visitor() {
            super(AtomicInteger.class, AtomicIntegerAttributeConverter.class);
        }

        @Override
        public AtomicInteger convertString(String value) {
            return STRING_CONVERTER.fromString(value);
        }

        @Override
        public AtomicInteger convertNumber(String value) {
            return STRING_CONVERTER.fromString(value);
        }
    }
}