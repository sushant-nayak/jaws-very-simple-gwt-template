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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Jonathan Andrew Wolter <jaw@jawspeak.com>
 */
public class DefaultDateFormatter {

  // warning - this static is a smell
  public static String formatDateToStandard(Date date) {
    return DateTimeFormat.getShortDateFormat().format(date) + " "
        + DateTimeFormat.getShortTimeFormat().format(date);
  }
}
