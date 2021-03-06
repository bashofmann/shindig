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
package org.apache.shindig.gadgets.js;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.shindig.gadgets.uri.JsUriManager.JsUri;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

public class AddJslCallbackProcessorTest {
  
  private IMocksControl control;
  private JsUri jsUri;
  private JsRequest request;
  private JsResponseBuilder response;
  private AddJslCallbackProcessor processor;  
  
  @Before
  public void setUp() {
    control = EasyMock.createControl();
    jsUri = control.createMock(JsUri.class);
    request = control.createMock(JsRequest.class);
    response = new JsResponseBuilder();
    processor = new AddJslCallbackProcessor();
    EasyMock.expect(request.getJsUri()).andReturn(jsUri);
  }

  @Test
  public void testWithHint() throws Exception {
    EasyMock.expect(jsUri.isNohint()).andReturn(false);
    control.replay();
    assertTrue(processor.process(request, response));
    assertEquals(AddJslCallbackProcessor.JSL_CALLBACK_JS,
        response.build().toJsString());
    control.verify();
  }

  @Test
  public void testWithoutHint() throws Exception {
    EasyMock.expect(jsUri.isNohint()).andReturn(true);
    control.replay();
    assertTrue(processor.process(request, response));
    assertEquals("", response.build().toJsString());
    control.verify();
  }
}
