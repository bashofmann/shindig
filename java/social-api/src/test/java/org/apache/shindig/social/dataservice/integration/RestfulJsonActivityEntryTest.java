/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.dataservice.integration;

import org.apache.shindig.protocol.ContentTypes;
import org.junit.Test;

/**
 * Tests the ActivityStreams REST API by comparing test fixtures to actual
 * server responses.
 * 
 * TODO: naming consistency with activity & activityEntry
 * TODO: test server errors with invalid requests e.g. "400 Activity not found: objectXYZ"
 */
public class RestfulJsonActivityEntryTest extends AbstractLargeRestfulTests{
  
  private static final String FIXTURE_LOC = "src/test/java/org/apache/shindig/social/dataservice/integration/fixtures/";
  
  @Test
  public void testGetActivityEntryJsonById() throws Exception {
    String resp = getResponse("/activitystreams/john.doe/@self/1/object1", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonId.json");
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
  
  @Test
  public void testGetActivityEntryJsonByIds() throws Exception {
    String resp = getResponse("/activitystreams/john.doe/@self/1/object1,object2", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonIds.json");
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
  
  @Test
  public void testGetActivityEntryJsonByGroup() throws Exception {
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonGroup.json");
    
    // Test @self
    String resp = getResponse("/activitystreams/john.doe/@self/1", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    assertTrue(TestUtils.jsonsEqual(expected, resp));
    
    // Test @friends with multiple people
    resp = getResponse("/activitystreams/jane.doe,canonical/@friends", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    assertTrue(TestUtils.jsonsEqual(expected, resp));  // same two activities returned
  }
  
  @Test
  public void testDeleteActivityEntryJson() throws Exception {
    // First delete object1, then retrieve & test
    getResponse("/activitystreams/john.doe/@self/1/object1", "DELETE", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String resp = getResponse("/activitystreams/john.doe/@self/1", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonDelete.json");;
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
  
  @Test
  public void testUpdateActivityEntryJson() throws Exception {
    // Update activity
    String postData = "{title : 'Super Updated Activity', actor: {id: 'john.doe'}, object : {id: 'object2'}}";
    getResponse("/activitystreams/john.doe/@self/1/object2", "PUT", postData, null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    
    // Retrieve updated activity & test
    String resp = getResponse("/activitystreams/john.doe/@self/1", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonUpdated.json");;
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
  
  @Test
  public void testCreateActivityEntryJson() throws Exception {
    // Create activity
    String postData = "{title : 'Super Created Activity', actor: {id: 'john.doe'}, object : {id: 'objectCreated'}}";
    getResponse("/activitystreams/john.doe/@self/1", "POST", postData, null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    
    // Retrieve created activity & test
    String resp = getResponse("/activitystreams/john.doe/@self/1/objectCreated", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityEntryJsonCreated.json");;
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
  
  @Test
  public void testGetActivityEntrySupportedFields() throws Exception {
    String resp = getResponse("/activitystreams/@supportedFields", "GET", null, ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    String expected = TestUtils.loadTestFixture(FIXTURE_LOC + "ActivityStreamsSupportedFields.json");;
    assertTrue(TestUtils.jsonsEqual(expected, resp));
  }
}