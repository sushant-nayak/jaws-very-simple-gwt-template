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

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.util.tools.Utility;

/**
 * @author Jonathan Andrew Wolter <jaw@jawspeak.com>
 */
public class VerySimpleGwtTemplateGenerator extends Generator {
  
  private String generatedClassName;
  private String packageName;
  private String typeName;
  /* Used to match ${xyz} but not $${xyz} template tokens.
   * Regex does not have a $ prefix, or starts with nothing, then contains ${token}, 
   * matching token in group 1. */
  public final Pattern STRING_TOKEN_REGEX = Pattern.compile("(?:[^\\$]|^)(\\$\\{[^\\}]+\\})");
  
  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
          throws UnableToCompleteException {
    this.typeName = typeName;
    TypeOracle typeOracle = context.getTypeOracle();
    
    try {
      JClassType classType = typeOracle.getType(typeName);
      packageName = classType.getPackage().getName();
      generatedClassName = classType.getSimpleSourceName()  + "Generated";
      typeName = classType.getSimpleSourceName();
      generateClass(logger, context);
    } catch (NotFoundException e) {
      logger.log(TreeLogger.ERROR, "VerySimpleGwtTemplateGenerator ERROR", e);
    } catch (SecurityException e) {
      logger.log(TreeLogger.ERROR, "VerySimpleGwtTemplateGenerator ERROR", e);
    } catch (ClassNotFoundException e) {
      logger.log(TreeLogger.ERROR, "VerySimpleGwtTemplateGenerator ERROR", e);
    } catch (IOException e) {
      logger.log(TreeLogger.ERROR, "VerySimpleGwtTemplateGenerator ERROR", e);
    } catch (CouldNotParseTemplateException e) {
      logger.log(TreeLogger.ERROR, "VerySimpleGwtTemplateGenerator ERROR", e);
    }
    return packageName + "." + generatedClassName;
  }

  private void generateClass(TreeLogger logger, GeneratorContext context) 
      throws SecurityException, ClassNotFoundException, IOException, CouldNotParseTemplateException {
    PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);
    if (printWriter == null) {
      return; // null means the source code was already generated
    }
    ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, generatedClassName);
    composer.addImplementedInterface(typeName);
    SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);
    generateMethodsForEachHtmlTemplate(logger, sourceWriter);
    sourceWriter.println("}");
    context.commit(logger, printWriter);
  }

  private void generateMethodsForEachHtmlTemplate(TreeLogger logger, SourceWriter sourceWriter) 
      throws SecurityException, ClassNotFoundException, IOException, CouldNotParseTemplateException {
    String folderPrefix = packageName.replace(".", "/");
    for (Method method : Class.forName(typeName).getMethods()) {
      String methodName = method.getName();
      if (!methodName.startsWith("get")) {
        break;
      }
      String path = folderPrefix + "/" + methodName.substring(3) + ".html";
      String template = Utility.getFileFromClassPath(path);
      String parameters = getParameters(method);
      sourceWriter.println("public VerySimpleGwtTemplate " + methodName + "(" + parameters + ") {");
      sourceWriter.indent();
      sourceWriter.println("VerySimpleGwtTemplate template = new VerySimpleGwtTemplate(\"" + Generator.escape(template) + "\");");
      for (Map.Entry<String, String> entry : getTemplateReplaceables(template).entrySet()) {
        // TODO(jwolter): look for the getter method, and throw an exception if it doesn't exist
        // TODO(jwolter): look at the getter's return type, and do nice null checking then "" + or call toString()
        // TODO(jwolter): allow an arbitrary way to "pipe" subsequent options to the setter, so that you can for a date i.e. call a date formatter on it.
        // TODO(jwolter): basically look at the features of FreeMarker, and see how to integrate the best ones in here. Or adopt to FreemarkerGWT.
        
        // as for now, we simply turn each value into a String by prepending a "" + to the value.
        // also for the time being, remember you can have a $${token} that will not get replaced, so you can set it manually. So,
        // you should only have ${token}'s that you are comfortable getting them set literally as Strings.
        sourceWriter.println("template.set(\"" + entry.getKey() + "\", " + "\"\" + " + entry.getValue() + ");");
      }
      sourceWriter.println("return template;");
      sourceWriter.outdent();
      sourceWriter.println("}");
    }
    sourceWriter.outdent();
  }
  
  
  // TODO(jwolter): this should be extracted into a new class, as it is a big enough responsibility.
  /*visible for testing*/ Map<String, String> getTemplateReplaceables(String template) throws CouldNotParseTemplateException {
    Map<String, String> result = new HashMap<String, String>();
    Matcher matcher = STRING_TOKEN_REGEX.matcher(template);
    while(matcher.find()) {
      String fullToken = matcher.group(1);
      String strippedToken = fullToken.replaceAll("\\$\\{", "").replaceAll("\\}", "");
      String[] split = strippedToken.split("\\.");
      if (split.length != 2) {
        throw new CouldNotParseTemplateException("One period required in token to determine local variable to call setter on. Token: " + fullToken);
      }
      String methodPart = split[1];
      String restOfMethod = methodPart.length() > 1 ? methodPart.substring(1) : "";
      String methodName = split[0] + ".get" + Character.toUpperCase(methodPart.charAt(0)) + restOfMethod + "()";
      result.put(fullToken, methodName);
    }
    return result;
  }

  private String getParameters(Method method) {
    StringBuilder sb = new StringBuilder();
    // We can't use reflection OR Paranamer to find out an interface's or abstract class' argument
    // names, becuase that information just insn't there in the bytecode. 
    // (Even if compiled with debug information).
    for (Class<?> parameterType : method.getParameterTypes()) {
      String clazz = parameterType.getSimpleName();
      String computedParameterName = Character.toLowerCase(clazz.charAt(0)) + clazz.substring(1);
      sb.append(parameterType.getCanonicalName()).append(" ").append(computedParameterName); 
    }
    return sb.toString();
  }

  
  public static class CouldNotParseTemplateException extends Exception {
    private static final long serialVersionUID = 1L;
    public CouldNotParseTemplateException(String message) {
      super(message);
    }
  }
}
