/*
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package software.amazon.awssdk.services.dynamodb.document.internal;

import software.amazon.awssdk.services.dynamodb.document.Page;


/**
 * @param <T> resource type
 * @param <R> low level result type
 */
public class PageIterable<T, R> implements Iterable<Page<T, R>> {
    private final PageBasedCollection<T, R> col;

    PageIterable(PageBasedCollection<T, R> col) {
        this.col = col;
    }

    @Override
    public PageIterator<T, R> iterator() {
        return new PageIterator<T, R>(col);
    }
}
