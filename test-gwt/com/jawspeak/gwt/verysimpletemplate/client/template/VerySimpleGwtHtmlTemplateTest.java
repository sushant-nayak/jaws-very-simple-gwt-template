/*
 * Copyright 2008 Jonathan Andrew Wolter
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jawspeak.gwt.verysimpletemplate.client.template;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.jawspeak.gwt.verysimpletemplate.client.AppConstants;

/**
 * @author Jonathan Andrew Wolter
 */
public class VerySimpleGwtHtmlTemplateTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return AppConstants.APP_MODULE_NAME;
  }
    
  public void testSetTwoFields() throws Exception {
    VerySimpleGwtTemplate template = 
      new VerySimpleGwtTemplate("<p>${field1}</p><p>${field1}</p>");
    template.set("${field1}", "Value 1");
    HTML html = template.toHTML();
    assertEquals("<p>Value 1</p><p>Value 1</p>", html.getHTML());
  }
  
  public void testThrowsExceptionIfNotAllTemplateFieldsAreSet() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${field1}</p>");
    try {
      template.toHTML();
      fail();
    } catch (IllegalStateException e) {
      //expected
    }
  }
  
  public void testRenderIntoHTMLPanel() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${field1}</p>");
    template.set("${field1}", "Value 1");
    HTMLPanel htmlPanel = template.toHTMLPanel();
    assertEquals(0, htmlPanel.getWidgetCount());
    assertEquals("<p>Value 1</p>", htmlPanel.getElement().getInnerHTML());
  }
  
  public void testSetWidgetInTemplateAndReturnPanel() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${label1}</p>");
    template.set("${label1}", new Label("My Word"));
    HTMLPanel htmlPanel = template.toHTMLPanel();
    assertEquals(1, htmlPanel.getWidgetCount());
    assertEquals("<p><div class=\"gwt-Label\">My Word</div></p>", htmlPanel.getElement().getInnerHTML());
  }

  public void suppressedTestSetTwoWidgetsWithEventsInTemplateAndReturnPanel() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>$${focus1}<br />$${focus2}</p>");
    FocusPanel focus1 = new FocusPanel();
    FocusPanel focus2 = new FocusPanel();
    final boolean[] focus1MouseDown = new boolean[] { false }; // array to get around final
    final boolean[] focus2MouseDown =  new boolean[] { false };
    focus1.addMouseListener(new MouseListenerAdapter() {
      @Override
      public void onMouseDown(Widget sender, int x, int y) {
        focus1MouseDown[0] = true;
      }
    });
    focus2.addMouseListener(new MouseListenerAdapter() {
      @Override
      public void onMouseDown(Widget sender, int x, int y) {
        focus2MouseDown[0] = true;
      }
    });
    template.set("$${focus1}", focus1);
    template.set("$${focus2}", focus2);
    HTMLPanel htmlPanel = template.toHTMLPanel();
    RootPanel.get().add(htmlPanel);
// TODO(jwolter): re-enable this test
// Unfortunately, I don't see a quick and easy way to create an event. I know I can with jsni, but
// I'm not sure the right way to do this, so I'm going to defer this for the time being.
//    focus1.onBrowserEvent(fakeEvent);
//    focus2.onBrowserEvent(fakeEvent);
    assertEquals(2, htmlPanel.getWidgetCount());
    assertTrue(focus1MouseDown[0]);
    assertTrue(focus2MouseDown[0]);
  }

  public void testCallToHTMLAndWidgetsWereAddedThrowsException() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${label1}</p>");
    template.set("${label1}", new Label("My Word"));
    try {
      template.toHTML();
      fail();
    } catch (IllegalStateException e) {
      //expected
    }
  }
  
  public void testIfNotAllWidgetsAreSetThrowsException() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${label1}</p>");
    try {
      template.toHTMLPanel();
      fail();
    } catch (IllegalStateException e) {
      //expected
    }
  }
  
  public void testSetNullStringValueTurnsIntoEmptyString() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${field}</p>");
    template.set("${field}", (String)null);
    assertEquals("<p></p>", template.toHTML().getHTML());
  }

  public void testSetNullWidgetValueTurnsIntoEmptyString() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${label1}</p>");
    template.set("${label1}", (Widget)null);
    HTMLPanel htmlPanel = template.toHTMLPanel();
    assertEquals(0, htmlPanel.getWidgetCount());
    assertEquals("<p></p>", htmlPanel.getElement().getInnerHTML());
  }
  
  public void testSetAKeyThatDoesNotExistThrowsAnException() throws Exception {
    VerySimpleGwtTemplate template = new VerySimpleGwtTemplate("<p>${label1}</p>");
    try {
      template.set("${bogusLabel}", "x");
      fail();
    } catch (IllegalArgumentException e) {
      //expected
    }
    try {
      template.set("${bogusLabel}", new Label("x"));
      fail();
    } catch (IllegalArgumentException e) {
      //expected
    }
  }

}
