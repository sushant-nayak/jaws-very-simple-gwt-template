package com.jawspeak.gwt.verysimpletemplate.client;

import junit.framework.Test;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.jawspeak.gwt.verysimpletemplate.client.template.TemplateResourcesTest;
import com.jawspeak.gwt.verysimpletemplate.client.template.VerySimpleGwtHtmlTemplateTest;

/**
 * @author Jonathan Andrew Wolter <jaw@jawspeak.com>
 */
public class AllGwtTests extends GWTTestSuite {
  
  public static Test suite() {
    GWTTestSuite suite = new GWTTestSuite();
    suite.addTestSuite(TemplateResourcesTest.class);
    suite.addTestSuite(VerySimpleGwtHtmlTemplateTest.class);
    return suite;
  }
  
}
