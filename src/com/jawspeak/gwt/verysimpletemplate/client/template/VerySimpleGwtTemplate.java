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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerySimpleGwtTemplate {

  private String htmlTemplate;
  private Map<String, Widget> widgets = new HashMap<String, Widget>();

  public VerySimpleGwtTemplate(String htmlTemplate) {
    this.htmlTemplate = htmlTemplate;
  }
  
  /** 
   * @param templateKey, what to replace. i.e. <tt>${foo.token}</tt>.
   * @param value to replace the template key with.
   */
  public void set(String templateKey, String value) {
    verifyTemplateKeyExistsInTemplate(templateKey);
    if (value == null) {
      value = "";
    }
    htmlTemplate = htmlTemplate.replace(templateKey, value);
  }

  public void set(String templateKey, Widget widget) {
    verifyTemplateKeyExistsInTemplate(templateKey);
    if (widget == null) {
      set(templateKey, "");
      return;
    }
    widgets.put(templateKey, widget);
  }
  
  public HTML toHTML() {
    verifyNoWidgetsSetWhenCallingToHTMLMethod();
    verifyTemplateCompletelyFilledOut();
    return new HTML(htmlTemplate);
  }

  public HTMLPanel toHTMLPanel() {
    Map<String, Widget> idToWidget = new HashMap<String, Widget>(); 
    for (Entry<String, Widget> entryByToken : widgets.entrySet()) {
      String id = DOM.createUniqueId();
      htmlTemplate = htmlTemplate.replace(entryByToken.getKey(),
              "<span id='" + id + "'></span>");
      idToWidget.put(id, entryByToken.getValue());
    }
    widgets = null; // finally all done using it
    HTMLPanel panel = new HTMLPanel(htmlTemplate);
    for (Entry<String, Widget> entryById : idToWidget.entrySet()) {
      panel.addAndReplaceElement(entryById.getValue(), entryById.getKey());
    }
    verifyTemplateCompletelyFilledOut();
    return panel;
  }

  private void verifyTemplateKeyExistsInTemplate(String templateKey) {
    if (!htmlTemplate.contains(templateKey)) {
      throw new IllegalArgumentException("The templateKey: " + templateKey + ", you provided was not found in the template.");
    }
  }

  private void verifyNoWidgetsSetWhenCallingToHTMLMethod() {
    if (widgets.size() > 0) {
      throw new IllegalStateException("Can not call toHTML when you have widgets, use toHTMLPanel instead");
    }
  }

  private void verifyTemplateCompletelyFilledOut() {
    if (htmlTemplate.indexOf("${") >= 0) {
      throw new IllegalStateException("Lefover template variables \"${\" in htmlTemplate!\n" +
              htmlTemplate);
    }
  }

}
