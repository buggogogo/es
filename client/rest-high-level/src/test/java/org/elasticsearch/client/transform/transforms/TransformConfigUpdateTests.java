/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.client.transform.transforms;

import org.elasticsearch.client.transform.TransformNamedXContentProvider;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.test.AbstractXContentTestCase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.client.transform.transforms.DestConfigTests.randomDestConfig;
import static org.elasticsearch.client.transform.transforms.SourceConfigTests.randomSourceConfig;

public class TransformConfigUpdateTests extends AbstractXContentTestCase<TransformConfigUpdate> {

    public static TransformConfigUpdate randomTransformConfigUpdate() {
        return new TransformConfigUpdate(
            randomBoolean() ? null : randomSourceConfig(),
            randomBoolean() ? null : randomDestConfig(),
            randomBoolean() ? null : TimeValue.timeValueMillis(randomIntBetween(1_000, 3_600_000)),
            randomBoolean() ? null : randomSyncConfig(),
            randomBoolean() ? null : randomAlphaOfLengthBetween(1, 1000));
    }

    public static SyncConfig randomSyncConfig() {
        return TimeSyncConfigTests.randomTimeSyncConfig();
    }

    @Override
    protected TransformConfigUpdate doParseInstance(XContentParser parser) throws IOException {
        return TransformConfigUpdate.fromXContent(parser);
    }

    @Override
    protected boolean supportsUnknownFields() {
        return false;
    }

    @Override
    protected TransformConfigUpdate createTestInstance() {
        return randomTransformConfigUpdate();
    }

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        List<NamedXContentRegistry.Entry> namedXContents = searchModule.getNamedXContents();
        namedXContents.addAll(new TransformNamedXContentProvider().getNamedXContentParsers());

        return new NamedXContentRegistry(namedXContents);
    }
}
