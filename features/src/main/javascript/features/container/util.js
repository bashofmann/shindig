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


/**
 * @fileoverview Utility methods for common container.
 */


/**
 * @type {Object}
 */
osapi.container.util = {};


/**
 * Return value of json at key, if valid. Otherwise, return defaultValue.
 * @param {Object} json The JSON to look up key param from.
 * @param {string} key Key in config.
 * @param {*=} defaultValue The default value to return.
 * @return {*}
 */
osapi.container.util.getSafeJsonValue = function(json, key, defaultValue) {
  return (json[key] != undefined && json[key] != null)
      ? json[key] : defaultValue;
};


/**
 * Merge two JSON together. Keys in json2 will replace than in json1.
 * @param {Object} json1 JSON to start merge with.
 * @param {Object} json2 JSON to append/replace json1.
 * @return {Object} the resulting JSON.
 */
osapi.container.util.mergeJsons = function(json1, json2) {
  var result = {};
  for (var key in json1) {
    result[key] = json1[key];
  }
  for (var key in json2) {
    result[key] = json2[key];
  }
  return result;
};


/**
 * Construct a JSON request to get gadget metadata. For now, this will request
 * a super-set of data needed for all CC APIs requiring gadget metadata, since
 * the caching of response is not additive.
 * @param {Array} gadgetUrls A list of gadget URLs.
 * @return {Object} the resulting JSON.
 */
osapi.container.util.newMetadataRequest = function(gadgetUrls) {
  return {
      'container': window.__CONTAINER,
      'ids': gadgetUrls,
      'fields': [
          'iframeUrl',
          'modulePrefs.*',
          'needsTokenRefresh',
          'userPrefs.*',
          'views.preferredHeight',
          'views.preferredWidth',
          'expireTimeMs',
          'responseTimeMs'
      ]
  };
};


/**
 * Construct a JSON request to get gadget token.
 * @param {Array} A list of gadget URLs.
 * @return {Object} the resulting JSON.
 */
osapi.container.util.newTokenRequest = function(gadgetUrls) {
  return {
      'container': window.__CONTAINER,
      'ids': gadgetUrls,
      'fields': [
          'token'
      ]
  };
};


/**
 * Extract keys from a JSON to an array.
 * @param {Object} json to extract keys from.
 * @return {Array.<string>} keys in the json.
 */
osapi.container.util.toArrayOfJsonKeys = function(json) {
  var result = [];
  for (var key in json) {
    result.push(key);
  }
  return result;
};


/**
 * Return true if json is empty.
 * @param {Object} json to check.
 * @return {Boolean}
 */
osapi.container.util.isEmptyJson = function(json) {
  for (var key in json) {
    return false;
  }
  return true;
};


/**
 * Put up a warning message to console.
 * @param {String} message to warn with.
 */
osapi.container.util.warn = function(message) {
  if (console && console.warn) {
    console.warn(message);
  }
};


/**
 * @return {number} current time in ms.
 */
osapi.container.util.getCurrentTimeMs = function() {
  return new Date().getTime();
};
