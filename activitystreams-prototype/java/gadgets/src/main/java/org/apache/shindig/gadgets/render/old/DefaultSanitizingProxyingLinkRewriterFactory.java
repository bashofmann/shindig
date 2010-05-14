/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shindig.gadgets.render.old;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.rewrite.ContentRewriterFeature;
import org.apache.shindig.gadgets.rewrite.old.ContentRewriterUris;

@Singleton
public class DefaultSanitizingProxyingLinkRewriterFactory implements
    SanitizingProxyingLinkRewriterFactory {

  private final ContentRewriterUris rewriterUris;

  @Inject
  public DefaultSanitizingProxyingLinkRewriterFactory(
      ContentRewriterUris rewriterUris) {
    this.rewriterUris = rewriterUris;
  }

  public SanitizingProxyingLinkRewriter create(Uri gadgetUri,
      ContentRewriterFeature.Config rewriterFeature, String container,
      String expectedMime, boolean debug, boolean nocache) {
    return new SanitizingProxyingLinkRewriter(rewriterUris, gadgetUri,
        rewriterFeature, container, expectedMime, debug, nocache);
  }

}
