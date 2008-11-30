package com.jawspeak.gwt.verysimpletemplate.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.jawspeak.gwt.verysimpletemplate.client.template.DefaultDateFormatter;
import com.jawspeak.gwt.verysimpletemplate.client.template.TemplateResources;
import com.jawspeak.gwt.verysimpletemplate.client.template.VerySimpleGwtTemplate;
import com.jawspeak.gwt.verysimpletemplate.domain.DomainDto;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JawsVerySimpleGwtHtmlTemplateExample implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    FlowPanel flowPanel = new FlowPanel();
    RootPanel.get().add(flowPanel);
    
    // Here is how you use the template
    TemplateResources template = GWT.create(TemplateResources.class);
    VerySimpleGwtTemplate topHeaderLayoutTemplate = template.getTopHeaderLayoutTemplate();
    flowPanel.add(topHeaderLayoutTemplate.toHTML());
    
    DomainDto domainDto = new DomainDto();
    domainDto.setCount(14); 
    domainDto.setMyStartDate(new Date());
    domainDto.setName("JAW");
    domainDto.setSomeRatio(3.999);
    VerySimpleGwtTemplate domainDtoDetailsTopHtmlTableTemplate = template.getDomainDtoDetailsTopHtmlTableTemplate(domainDto);
    domainDtoDetailsTopHtmlTableTemplate.set("$${domainDto.startDate}", DefaultDateFormatter.formatDateToStandard(domainDto.getMyStartDate()));
    Button button = new Button("Click Me");
    domainDtoDetailsTopHtmlTableTemplate.set("$${x.hookForMouseOverEvent}", button);
    
    // Since we added a widget, we need to render this into an HTMLPanel, not HTML.
    flowPanel.add(domainDtoDetailsTopHtmlTableTemplate.toHTMLPanel());
    
    // The following example of event behavior is from the default starting GWT project.
    // It shows how you can attach widgets into double dollar sign template placeholders,
    // i.e. $${thisSyntax}.
    
    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.center();
        dialogBox.show();
      }
    });
    
    
    
  }
}
