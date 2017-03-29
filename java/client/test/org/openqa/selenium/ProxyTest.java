// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.


package org.openqa.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.JsonToBeanConverter;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class ProxyTest {

  @Test
  public void testNotInitializedProxy() {
    Proxy proxy = new Proxy();

    assertEquals(ProxyType.UNSPECIFIED, proxy.getProxyType());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertNull(proxy.getProxyAutoconfigUrl());
    assertFalse(proxy.isAutodetect());
  }

  @Test
  public void testCanNotChangeAlreadyInitializedProxyType() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.DIRECT);

    try {
      proxy.setAutodetect(true);
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setSocksPassword("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setSocksUsername("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setSocksProxy("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setFtpProxy("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setHttpProxy("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setNoProxy("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setProxyAutoconfigUrl("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setProxyType(ProxyType.SYSTEM);
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    try {
      proxy.setSslProxy("");
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }

    proxy = new Proxy();
    proxy.setProxyType(ProxyType.AUTODETECT);

    try {
      proxy.setProxyType(ProxyType.SYSTEM);
      fail("Didn't throw expected assertion");
    } catch (IllegalStateException e) {
      // Success - expected.
    }
  }

  @Test
  public void testManualProxy() {
    Proxy proxy = new Proxy();

    proxy.
        setHttpProxy("http.proxy:1234").
        setFtpProxy("ftp.proxy").
        setSslProxy("ssl.proxy").
        setNoProxy("localhost,127.0.0.*").
        setSocksProxy("socks.proxy:65555").
        setSocksUsername("test1").
        setSocksPassword("test2");

    assertEquals(ProxyType.MANUAL, proxy.getProxyType());
    assertEquals("ftp.proxy", proxy.getFtpProxy());
    assertEquals("http.proxy:1234", proxy.getHttpProxy());
    assertEquals("ssl.proxy", proxy.getSslProxy());
    assertEquals("socks.proxy:65555", proxy.getSocksProxy());
    assertEquals("test1", proxy.getSocksUsername());
    assertEquals("test2", proxy.getSocksPassword());
    assertEquals("localhost,127.0.0.*", proxy.getNoProxy());

    assertNull(proxy.getProxyAutoconfigUrl());
    assertFalse(proxy.isAutodetect());
  }

  @Test
  public void testPACProxy() {
    Proxy proxy = new Proxy();
    proxy.setProxyAutoconfigUrl("http://aaa/bbb.pac");

    assertEquals(proxy.getProxyType(), ProxyType.PAC);
    assertEquals("http://aaa/bbb.pac", proxy.getProxyAutoconfigUrl());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertFalse(proxy.isAutodetect());
  }

  @Test
  public void testAutodetectProxy() {
    Proxy proxy = new Proxy();
    proxy.setAutodetect(true);

    assertEquals(proxy.getProxyType(), ProxyType.AUTODETECT);
    assertTrue(proxy.isAutodetect());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertNull(proxy.getProxyAutoconfigUrl());
  }


  @Test
  public void manualProxyFromMap() {
    Map<String, String> proxyData = new HashMap<>();
    proxyData.put("proxyType", "manual");
    proxyData.put("httpProxy", "http.proxy:1234");
    proxyData.put("ftpProxy", "ftp.proxy");
    proxyData.put("sslProxy", "ssl.proxy");
    proxyData.put("noProxy", "localhost,127.0.0.*");
    proxyData.put("socksProxy", "socks.proxy:65555");
    proxyData.put("socksUsername", "test1");
    proxyData.put("socksPassword", "test2");

    Proxy proxy = new Proxy(proxyData);

    assertEquals(ProxyType.MANUAL, proxy.getProxyType());
    assertEquals("ftp.proxy", proxy.getFtpProxy());
    assertEquals("http.proxy:1234", proxy.getHttpProxy());
    assertEquals("ssl.proxy", proxy.getSslProxy());
    assertEquals("socks.proxy:65555", proxy.getSocksProxy());
    assertEquals("test1", proxy.getSocksUsername());
    assertEquals("test2", proxy.getSocksPassword());
    assertEquals("localhost,127.0.0.*", proxy.getNoProxy());

    assertNull(proxy.getProxyAutoconfigUrl());
    assertFalse(proxy.isAutodetect());
  }

  @Test
  public void manualProxyToJson() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.MANUAL);
    proxy.setHttpProxy("http.proxy:1234");
    proxy.setFtpProxy("ftp.proxy");
    proxy.setSslProxy("ssl.proxy");
    proxy.setNoProxy("localhost,127.0.0.*");
    proxy.setSocksProxy("socks.proxy:65555");
    proxy.setSocksUsername("test1");
    proxy.setSocksPassword("test2");

    Map<String, Object> json = proxy.toJson();

    assertEquals("manual", json.get("proxyType"));
    assertEquals("ftp.proxy", json.get("ftpProxy"));
    assertEquals("http.proxy:1234", json.get("httpProxy"));
    assertEquals("ssl.proxy", json.get("sslProxy"));
    assertEquals("socks.proxy:65555", json.get("socksProxy"));
    assertEquals("test1", json.get("socksUsername"));
    assertEquals("test2", json.get("socksPassword"));
    assertEquals("localhost,127.0.0.*", json.get("noProxy"));
    assertEquals(8, json.entrySet().size());
  }

  @Test
  public void pacProxyFromMap() {
    Map<String, String> proxyData = new HashMap<>();
    proxyData.put("proxyType", "PAC");
    proxyData.put("proxyAutoconfigUrl", "http://aaa/bbb.pac");

    Proxy proxy = new Proxy(proxyData);

    assertEquals(ProxyType.PAC, proxy.getProxyType());
    assertEquals("http://aaa/bbb.pac", proxy.getProxyAutoconfigUrl());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertFalse(proxy.isAutodetect());
  }

  @Test
  public void pacProxyToJson() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.PAC);
    proxy.setProxyAutoconfigUrl("http://aaa/bbb.pac");

    Map<String, Object> json = proxy.toJson();

    assertEquals("pac", json.get("proxyType"));
    assertEquals("http://aaa/bbb.pac", json.get("proxyAutoconfigUrl"));
    assertEquals(2, json.entrySet().size());
  }

  @Test
  public void autodetectProxyFromMap() {
    Map<String, Object> proxyData = new HashMap<>();
    proxyData.put("proxyType", "AUTODETECT");
    proxyData.put("autodetect", true);

    Proxy proxy = new Proxy(proxyData);

    assertEquals(ProxyType.AUTODETECT, proxy.getProxyType());
    assertTrue(proxy.isAutodetect());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertNull(proxy.getProxyAutoconfigUrl());
  }

  @Test
  public void autodetectProxyToJson() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.AUTODETECT);
    proxy.setAutodetect(true);

    Map<String, ?> json = proxy.toJson();

    assertEquals("autodetect", json.get("proxyType"));
    assertTrue((Boolean) json.get("autodetect"));
    assertEquals(2, json.entrySet().size());
  }

  @Test
  public void systemProxyFromMap() {
    Map<String, String> proxyData = new HashMap<>();
    proxyData.put("proxyType", "system");

    Proxy proxy = new Proxy(proxyData);

    assertEquals(ProxyType.SYSTEM, proxy.getProxyType());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertFalse(proxy.isAutodetect());
    assertNull(proxy.getProxyAutoconfigUrl());
  }

  @Test
  public void systemProxyToJson() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.SYSTEM);

    Map<String, Object> json = proxy.toJson();

    assertEquals("system", json.get("proxyType"));
    assertEquals(1, json.entrySet().size());
  }

  @Test
  public void directProxyFromMap() {
    Map<String, String> proxyData = new HashMap<>();
    proxyData.put("proxyType", "DIRECT");

    Proxy proxy = new Proxy(proxyData);

    assertEquals(ProxyType.DIRECT, proxy.getProxyType());

    assertNull(proxy.getFtpProxy());
    assertNull(proxy.getHttpProxy());
    assertNull(proxy.getSslProxy());
    assertNull(proxy.getSocksProxy());
    assertNull(proxy.getSocksUsername());
    assertNull(proxy.getSocksPassword());
    assertNull(proxy.getNoProxy());
    assertFalse(proxy.isAutodetect());
    assertNull(proxy.getProxyAutoconfigUrl());
  }

  @Test
  public void directProxyToJson() {
    Proxy proxy = new Proxy();
    proxy.setProxyType(ProxyType.DIRECT);

    Map<String, Object> json = proxy.toJson();

    assertEquals("direct", json.get("proxyType"));
    assertEquals(1, json.entrySet().size());
  }

  @Test
  public void constructingWithNullKeysWorksAsExpected() {
    Map<String, String> rawProxy = new HashMap<>();
    rawProxy.put("ftpProxy", null);
    rawProxy.put("httpProxy", "http://www.example.com");
    rawProxy.put("autodetect", null);
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(PROXY, rawProxy);

    Proxy proxy = Proxy.extractFrom(caps);

    assertNull(proxy.getFtpProxy());
    assertFalse(proxy.isAutodetect());
    assertEquals("http://www.example.com", proxy.getHttpProxy());
  }

  @Test
  @Ignore
  public void serialiazesAndDeserializesWithoutError() {
    Proxy proxy = new Proxy();
    proxy.setProxyAutoconfigUrl("http://www.example.com/config.pac");

    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(PROXY, proxy);

    String rawJson = new BeanToJsonConverter().convert(caps);
    Capabilities converted = new JsonToBeanConverter().convert(Capabilities.class, rawJson);

    Object returnedProxy = converted.getCapability(PROXY);
    assertTrue(returnedProxy instanceof Proxy);
  }
}