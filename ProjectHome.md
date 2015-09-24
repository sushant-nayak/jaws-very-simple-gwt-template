Update 2009/7/29: This is a great way to see how GWT generators and deferred binding works, but for real work I recommend Google’s newish UiBinder (formerly called the Declarative UI). See this thread in the google group.



If you try JAW's Very Simple GWT Html Template, all you need to do is follow a few conventions and you get the following.

  1. Create a html file with your complex layout. For instance a really fancy 12x12 html table with all sorts of colspans and rowspans for displaying detailed tabular data.
  1. Create a value object (in Java, on the GWT side) that you want to automatically use to dump some attributes into the template.
  1. In the html template, use `${valueObject.someProperty}` style notation where you want automatic setting of values into the template.
  1. In the html template, use `$${specialDoubleDollarSign}` notation where you want to manually bind the template values.

Read more on my blog post: http://jawspeak.com/2008/11/29/introducting-jaws-very-simple-gwt-html-template

Browse Source Here: http://code.google.com/p/jaws-very-simple-gwt-template/source/browse/#svn/trunk/src

If you want to try it out:

  1. Check out the source code: <tt>svn checkout <a href='http://jaws-very-simple-gwt-template.googlecode.com/svn/trunk/'>http://jaws-very-simple-gwt-template.googlecode.com/svn/trunk/</a> jaws-very-simple-gwt-template-read-only</tt>
  1. Create a buildlocal.properties and change the jar locations from what the build.properties says.
  1. Run <tt>ant all</tt> to run tests.
  1. Run <tt>ant gwt.shell</tt> to start the example entrypoint. (You can also run tests in the included Eclipse project, or launch the app with the Eclipse launch configuration).
  1. Then look at the source code and tests to see how it works using Generators.

Thanks!


