/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.controller.status.history;

import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmbeddedQuestDbStatusHistoryRepositoryForNodeTest extends AbstractEmbeddedQuestDbStatusHistoryRepositoryTest {

    @Test
    public void testReadingEmptyRepository() {
        final StatusHistory nodeStatusHistory = repository.getNodeStatusHistory(START, END);
        final GarbageCollectionHistory garbageCollectionHistory = repository.getGarbageCollectionHistory(START, END);

        assertTrue(nodeStatusHistory.getStatusSnapshots().isEmpty());
        assertTrue(garbageCollectionHistory.getGarbageCollectionStatuses("gc1").isEmpty());
        assertTrue(garbageCollectionHistory.getGarbageCollectionStatuses("gc2").isEmpty());
    }

    @Test
    public void testWritingThenReadingComponents() throws Exception {
        repository.capture(givenNodeStatus(), new ProcessGroupStatus(), givenGarbageCollectionStatuses(INSERTED_AT), INSERTED_AT);
        waitUntilPersisted();

        final StatusHistory nodeStatusHistory = repository.getNodeStatusHistory(START, END);
        assertNodeStatusHistory(nodeStatusHistory.getStatusSnapshots().get(0));

        final GarbageCollectionHistory garbageCollectionHistory = repository.getGarbageCollectionHistory(START, END);
        assertGc1Status(garbageCollectionHistory.getGarbageCollectionStatuses("gc1"));
        assertGc2Status(garbageCollectionHistory.getGarbageCollectionStatuses("gc2"));
    }
}
