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
package com.jawspeak.gwt.verysimpletemplate.rebind;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Matcher;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.GeneratedResource;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.UnitTestTreeLogger;
import com.jawspeak.gwt.verysimpletemplate.client.template.TemplateResources;
import com.jawspeak.gwt.verysimpletemplate.client.template.VerySimpleGwtTemplate;
import com.jawspeak.gwt.verysimpletemplate.client.template.VerySimpleGwtTemplated;
import com.jawspeak.gwt.verysimpletemplate.rebind.VerySimpleGwtTemplateGenerator.CouldNotParseTemplateException;

/**
 * @author Jonathan Andrew Wolter <jaw@jawspeak.com>
 */
public class VerySimpleGwtTemplateGeneratorTest {

  @Test
  public void readsHtmlTemplateAndWritesMethodToReturnIt() throws Exception {
    String committedString = generateTemplateForClass(TemplateResources.class);

    System.out.println(committedString);

    // Caution: it fails if too much or to little indenting! (be on the lookout)
    assertTrue(committedString.contains(
        "public VerySimpleGwtTemplate getTopHeaderLayoutTemplate() {\n" + 
      "    " /* expected leading indent */ + "VerySimpleGwtTemplate template = new VerySimpleGwtTemplate(\"<div class=\\\"header\\\""));
    assertTrue(committedString.contains("return template;"));
    
    assertTrue(committedString.contains(
        "public VerySimpleGwtTemplate getDomainDtoDetailsTopHtmlTableTemplate(com.jawspeak.gwt.verysimpletemplate.domain.DomainDto domainDto) {\n" + 
        "    " /* expected leading indent */ + "VerySimpleGwtTemplate template = new VerySimpleGwtTemplate(\""));
    assertTrue(committedString.contains("template.set(\"${domainDto.count}\", \"\" + domainDto.getCount());"));
    assertTrue(committedString.contains("template.set(\"${domainDto.someRatio}\", \"\" + domainDto.getSomeRatio());"));
    assertTrue(committedString.contains("return template;"));
  }

  private static class MyDto {
    // used for tests below
  }
  
  private static interface TemplateMultipleParameters extends VerySimpleGwtTemplated {
    public VerySimpleGwtTemplate getBogusHtmlForTest1(
            VerySimpleGwtTemplateGeneratorTest.MyDto myDto);
  }
  
  @Test
  public void doesNotCallSettersOnTemplateForGetterMethodsThatHaveWidgetAssignableReturnTypes() throws Exception {
    
  }
  
  @Test
  public void unsupportedToHaveAMethodThatTakesTwoOfTheSameTypeParameters() throws Exception {
    
  }

  @Test
  public void ignoresMethodsWithoutGetAtStartForFindingFiles() throws Exception {
  
  }
  
  @Test
  public void writesErrorIfCantFindTemplateFileWhileWritingMethods() throws Exception {

  }

  private String generateTemplateForClass(
          final Class<? extends VerySimpleGwtTemplated> templateInterfaceClazz)
          throws NotFoundException, UnableToCompleteException {
    VerySimpleGwtTemplateGenerator generator = new VerySimpleGwtTemplateGenerator();
    Mockery mockery = new Mockery() {{
      setImposteriser(ClassImposteriser.INSTANCE);
    }};
    // Unfortunately we have a lot to mock, because the GWT API digs around a lot in objects,
    // and the JClassType instance seemed more difficult to construct than I had time for.
    final JPackage jPackage = mockery.mock(JPackage.class);
    final JClassType jClassType = mockery.mock(JClassType.class);
    final TypeOracle typeOracle = mockery.mock(TypeOracle.class);
    mockery.checking(new Expectations() {{
      one(jPackage).getName();will(returnValue(templateInterfaceClazz.getPackage().getName()));
      one(jClassType).getPackage();will(returnValue(jPackage));
      exactly(2).of(jClassType).getSimpleSourceName();will(returnValue(templateInterfaceClazz.getSimpleName()));
      one(typeOracle).getType(templateInterfaceClazz.getName());will(returnValue(jClassType));
    }});
    
    GenerateContextCommittingSpy contextSpy = new GenerateContextCommittingSpy(typeOracle);
    
    UnitTestTreeLogger logger = new UnitTestTreeLogger.Builder().createLogger();
    generator.generate(logger, contextSpy, templateInterfaceClazz.getName());
    logger.assertCorrectLogEntries();
    mockery.assertIsSatisfied();
    
    return contextSpy.getCommittedString();
  }
  
  @Test
  public void regexFindsOnlyAutoFillOutTemplates() throws Exception {
    String template = "<p>$${myDto.widgetDoNotReplace}</p><p>${myDto.replaceMe}<br>and ${myDto.i} should be replaced too.</p>";
    Map<String, String> replaceables = new VerySimpleGwtTemplateGenerator().getTemplateReplaceables(template);
    assertThat(replaceables, is((Map<String, String>)ImmutableMap.of("${myDto.replaceMe}", "myDto.getReplaceMe()", "${myDto.i}", "myDto.getI()")));

    template = "${myDto.foo}";
    replaceables = new VerySimpleGwtTemplateGenerator().getTemplateReplaceables(template);
    assertThat(replaceables, is((Map<String, String>)ImmutableMap.of("${myDto.foo}", "myDto.getFoo()")));
    
    try {
      new VerySimpleGwtTemplateGenerator().getTemplateReplaceables("${forgotObjectPrefix}");
      fail();
    } catch (CouldNotParseTemplateException expected) {
    // expected
    }

    try {
      new VerySimpleGwtTemplateGenerator().getTemplateReplaceables("<p>${x.valid} and ${forgotObjectPrefix}</p>");
      fail();
    } catch (CouldNotParseTemplateException expected) {
      // expected
    }
  }
  
  @Test
  public void regexWorksAsExpected() throws Exception {
    VerySimpleGwtTemplateGenerator generator = new VerySimpleGwtTemplateGenerator();
    Matcher matcher = generator.STRING_TOKEN_REGEX.matcher("foo bar ${token} zap");
    assertTrue(matcher.find());
    assertEquals("${token}", matcher.group(1));
    assertFalse(matcher.find());
    
    matcher = generator.STRING_TOKEN_REGEX.matcher("foo bar $${notatoken} zap");
    assertFalse(matcher.find());

    matcher = generator.STRING_TOKEN_REGEX.matcher("foo bar ${token1} $${notatoken} ${token2}zap");
    assertTrue(matcher.find());
    assertEquals("${token1}", matcher.group(1));
    assertTrue(matcher.find());
    assertEquals("${token2}", matcher.group(1));
    assertFalse(matcher.find());
    
    matcher = generator.STRING_TOKEN_REGEX.matcher("foo bar ${x} ${} second is too short");
    assertTrue(matcher.find());
    assertEquals("${x}", matcher.group(1));
    assertFalse(matcher.find());

    matcher = generator.STRING_TOKEN_REGEX.matcher("${myDto.y}");
    assertTrue(matcher.find());
    assertEquals("${myDto.y}", matcher.group(1));
    assertFalse(matcher.find());
  }
  
  
  public static class GenerateContextCommittingSpy implements GeneratorContext {
    private final TypeOracle typeOracle;
    private StringWriter capturingStringWriter = new StringWriter();

    public GenerateContextCommittingSpy(TypeOracle typeOracle) {
      this.typeOracle = typeOracle;
    }
    
    public void commit(TreeLogger logger, PrintWriter pw) {
      pw.flush();
    }

    public void commitArtifact(TreeLogger logger, Artifact<?> artifact)
            throws UnableToCompleteException {
      throw new UnsupportedOperationException();
    }

    public GeneratedResource commitResource(TreeLogger logger, OutputStream os)
            throws UnableToCompleteException {
      throw new UnsupportedOperationException();
    }

    public PropertyOracle getPropertyOracle() {
      throw new UnsupportedOperationException();
    }

    public TypeOracle getTypeOracle() {
      return typeOracle;
    }

    public PrintWriter tryCreate(TreeLogger logger, String packageName, String simpleName) {
      return new PrintWriter(capturingStringWriter); 
    }

    public OutputStream tryCreateResource(TreeLogger logger, String partialPath)
            throws UnableToCompleteException {
      throw new UnsupportedOperationException();
    }

    /** For tests, this shows the String that was committed */
    public String getCommittedString() {
      return capturingStringWriter.toString();
    }
    
  }
}
