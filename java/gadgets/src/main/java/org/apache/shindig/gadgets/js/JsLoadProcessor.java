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

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.uri.JsUriManager;
import org.apache.shindig.gadgets.uri.JsUriManager.JsUri;

import javax.servlet.http.HttpServletResponse;

/**
 * Emit JS loader code if the jsload query parameter is present in the request.
 */
public class JsLoadProcessor implements JsProcessor {
  private static final String CODE_ID = "[jsloader-bootstrap]";

  @VisibleForTesting
  public static final String JSLOAD_ONLOAD_ERROR = "jsload requires onload";

  @VisibleForTesting
  public static final String JSLOAD_JS_TPL = "(function() {" +
      "document.write('<scr' + 'ipt type=\"text/javascript\" src=\"%s\"></scr' + 'ipt>');" +
      "})();"; // Concatenated to avoid some browsers do dynamic script injection.

  private final JsUriManager jsUriManager;
  private int jsloadTtlSecs;

  @Inject
  public JsLoadProcessor(
      JsUriManager jsUriManager, @Named("shindig.jsload.ttl-secs") int jsloadTtlSecs) {
    this.jsUriManager = jsUriManager;
    this.jsloadTtlSecs = jsloadTtlSecs;
  }

  @VisibleForTesting
  public void setJsloadTtlSecs(int jsloadTtlSecs) {
    this.jsloadTtlSecs = jsloadTtlSecs;
  }

  public boolean process(JsRequest request, JsResponseBuilder builder)
      throws JsException {
    JsUri jsUri = request.getJsUri();
    
    // Don't emit the JS itself; instead emit JS loader script that loads
    // versioned JS. The loader script is much smaller and cacheable for a
    // configurable amount of time.
    if (jsUri.isJsload()) {
      doJsload(jsUri, builder);
      return false;
    }
    return true;
  }
  
  private void doJsload(JsUri jsUri, JsResponseBuilder resp)
      throws JsException {
    String onloadStr = jsUri.getOnload();

    // Require users to specify &onload=. This ensures a reliable continuation
    // of JS execution. IE asynchronously loads script, before it loads
    // source-scripted and inlined JS.
    if (onloadStr == null) {
      throw new JsException(HttpServletResponse.SC_BAD_REQUEST, JSLOAD_ONLOAD_ERROR);
    }

    jsUri.setJsload(false);
    jsUri.setNohint(true);
    Uri incUri = jsUriManager.makeExternJsUri(jsUri);

    int refresh = getCacheTtlSecs(jsUri);
    resp.setCacheTtlSecs(refresh);
    resp.setProxyCacheable(true);
    resp.appendJs(createJsloadScript(incUri), CODE_ID);
  }

  private int getCacheTtlSecs(JsUri jsUri) {
    if (jsUri.isNoCache()) {
      return 0;
    } else {
      Integer jsUriRefresh = jsUri.getRefresh();
      return (jsUriRefresh != null && jsUriRefresh >= 0)
          ? jsUriRefresh : jsloadTtlSecs;
    }
  }

  @VisibleForTesting
  String createJsloadScript(Uri uri) {
    String uriString = uri.toString();
    return String.format(JSLOAD_JS_TPL, uriString);
  }

}