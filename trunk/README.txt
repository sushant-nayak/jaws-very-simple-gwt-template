
If you try JAW's Very Simple GWT Html Template, all you need to do is follow a few conventions and you get the following.

   1. Create a html file with your complex layout. For instance a really fancy 12x12 html table with all sorts of colspans and rowspans for displaying detailed tabular data.
   2. Create a value object (in Java, on the GWT side) that you want to automatically use to dump some attributes into the template.
   3. In the html template, use ${valueObject.someProperty} style notation where you want automatic setting of values into the template.
   4. In the html template, use $${specialDoubleDollarSign} notation where you want to manually bind the template values. 

Read more on my blog post: http://jawspeak.com/2008/11/29/introducting-jaws-very-simple-gwt-html-template

If you want to try it out:

   1. Check out the source code: svn checkout http://jaws-very-simple-gwt-template.googlecode.com/svn/trunk/ jaws-very-simple-gwt-template-read-only
   2. Create a buildlocal.properties and change the jar locations from what the build.properties says.
   3. Run ant all to run tests.
   4. Run ant gwt.shell to start the example entrypoint. (You can also run tests in the included Eclipse project, or launch the app with the Eclipse launch configuration).
   5. Then look at the source code and tests to see how it works using Generators. 


