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

import com.jawspeak.gwt.verysimpletemplate.domain.DomainDto;

/**
 * This is the class you need to create in your implementations that use
 * the template html files.
 * 
 * Abstract class to enable code generation for the html templates.
 * Use one method per name of the template html file. 
 *
 * @author Jonathan Andrew Wolter
 */
public interface TemplateResources extends VerySimpleGwtTemplated {
  
  /** This will automatically replace the simple ${myDto.value} strings with 
   * <tt>template.set(\"myDto.value\", myDto.getValue());</tt>
   * 
   * <p>Note: you need to have the name of the parameter as the same as the type, but lower-cased.
   * And this does not support multiple parameters of the same type.</p>
   * 
   * @return template that has all of the simple (non-Widget return type) methods'
   * setters called on it. 
   */
  public VerySimpleGwtTemplate getTopHeaderLayoutTemplate();
  
  /**
   * This example takes in a value object called DomainDto, which will be used to do the
   * automatic data binding to the <tt>DomainDtoDetailsTopHtmlTableTemplate.html</tt> template's
   * single dollar sign placeholders (i.e. <tt>${domainDto.name}</tt>).
   */
  public VerySimpleGwtTemplate getDomainDtoDetailsTopHtmlTableTemplate(DomainDto domainDto);
}
