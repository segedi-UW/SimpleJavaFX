# Resources
## JavaFX Resources
- [Building JavaFX with Maven](https://edencoding.com/javafx-maven/)
- [JavaFX Objects w/ Tutorials](http://tutorials.jenkov.com/javafx/index.html)
- [JavaFX Documentation](https://openjfx.io/javadoc/11/)
### FXML
- [Getting Started With FXML](https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm)
- [Jenkov FXML Tutorial](http://tutorials.jenkov.com/javafx/fxml.html)
- [FXML Reference](https://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html#scripting)
- [Mastering FXML](https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm)
## I have also made an [advanced tutorial](https://github.com/segedi-UW/SDC-IDE) if you are interested.
# Simple JavaFX Setup and Use
This is a simple tutorial on getting up and running with JavaFX. For completion I have included this mini tutorial on how to create your own JavaFX project, but you should just use files in the repo that I have provided for you as setup.

# JavaFX with Java 11 using Maven
JavaFX is not included in java versions past 8, so the java version that we typically use (the latest LTS java 11) does not include it. This means we have to either A) install it and handle it as a dependency alongside our application ourselves, or B) let a dependency manager / build tool handle it for us. 

Since we don't care to handle that mess ourselves (I have done it and can say I didn't care for it), we are going to use Maven. I chose Maven over Gradle because Gradle does not output errors in the way you would expect, making debugging a nightmare for those who don't know how to deal with it, myself included. I also have found Maven to be a lot more consistent and easier to use even though it is more verbose.

# JavaFX project creation (skip if you want)
I am going to run through the process of getting up and going the fastest way possible - using Intellij's builtin JavaFX projects

1. Open Intellij and navigate to where you can create a 'new project'
    * File > new > project
    * or simply click new project if you open to the project selection screen
2. On the left sidebar, select JavaFX. You should fill in the Name, Group, and Artifact (group convention is com.\<fill in the name\>). Make sure that Language is Java, Build system is Maven, and Test framework is JUnit. When done click next.
3. Click Finish. You don't need to worry about any JavaFX dependencies yet - if you want to include some or want suggestions talk to me and I will show you how to using Maven. Either way we can add them later.

That is it, Intellij should provide you with a working application!
# JavaFX Hierarchy
JavaFX works by creating a scene graph, which is simply a tree of visible Nodes that are displayed. The base of the scene graph is the `Stage`, which is essentially a window that you can supply a `Scene` too. The `Scene` is the object that you add the other JavaFX component objects to, for example buttons and containers etc.

The next level down from the `Scene` are `Pane` nodes, which javaFX calls Layout Panes, and are simply containers that hold multiple nodes inside them in specific ways. The most useful ones that you should know right away are `HBox`, `VBox`, and `StackPane`. `HBox`, or horizontal box, aligns its children nodes left to right. `VBox`, vertical box, aligns its children top to bottom. `StackPane` puts its children on top of one another in its center by default. There are bunch of great containers that you can read up on [here](https://www.tutorialspoint.com/javafx/javafx_layout_panes.htm).

The lowest level you will likely be dealing with is JavaFX UIControls. It includes the following objects that are self-explanatory:
* `Label`
* `Button`
* `CheckBox`
* `RadioButton`
* `TextField`
* `PasswordField`
* `Slider`
One non-UIControl to mention is the `Canvas`.

There are more but you can read about them as you need them [here](https://www.tutorialspoint.com/javafx/javafx_ui_controls.htm).

# JavaFX - How it Works
The HelloApplication.java file has the main class that starts JavaFX. In it you will see that the class extends `Application`, which is the JavaFX class that handles the initialization of the framework.

Whenever you want to make the first window you need to launch the JavaFX framework (if it has not already been launched) by calling `launch()` from the `Application` extending object, or by calling `launch(App.class)`, where App is the `Application` extending object, from outside the class. Command line input can be provided with `launch(String[] args)` and retrieved with `getParameters()`. 

This simple application calls launch from the main method. I have also included some simple utility functions from our `DemoApplication` class, including `loadFXML(String res)` and `getPrimaryStage()`. Looking at our `loadFXML(String)` function for a sec, we see the following:

```java
    public static Parent loadFXML(String resource) {
        URL url = DemoApplication.class.getResource(resource);
        if (url == null) throw new NullPointerException("No resource found for: " + resource);
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        throw new IllegalStateException("Could not load FXML for resource: " + resource);
    }
```
Going through this code we see one way to load a resource into java: `DemoApplication.class.getResource(resource)`. This is how you would go about doing this in a static context as we do here, but the other way would to just call `getClass().getResource(resource)`. 

Note that you may run into issues if the structure of the project is incorrect (namely the resource directory), and you should know that some of the particulars of this `getResource()` call is handled under the hood by Intellij. Talk to me if that happens.

Design qualms aside about being static, this method is useful as we don't need to keep handling exceptions all over the place whenever we load in some FXML.

The `FXMLLoader` object is created with the resource and then when `load()` is called on it the loader parses the content of the file, returning the JavaFX object that the file describes (many times containing a hierarchy of child nodes).

But what is FXML? I am glad you asked. If you are familiar with the popular web tech stack HTML, CSS, and JS, then in JavaFX, FXML is the functional equivalent to HTML. JavaFX also has its own limited flavor of CSS, and obviously the 'JS' is Java itself as we see here. 

## FXML and Injection

The first load in the DemoApplication is to the resource "demo-view.fxml", which is found in the resources directory under src/main. Taking a look at it here, we can see that it is also an XML based language like HTML and follows the same conventions: 

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<?import javafx.scene.control.Button?>
<VBox alignment="CENTER" spacing="20.0" xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.sdc.javafx_demo.HelloController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>

    <Label fx:id="welcomeText"/>
    <Button text="Hello!" onAction="#onHelloButtonClick"/>
</VBox>

```

To start we see that our outer object is a `VBox`, which has a nested (or child) `Label` and `Button`. The `<padding>` tag is a nested attribute of the `VBox`. The `alignment="CENTER"` in the starting `VBox` tag is an example of an in tag attribute (where 'alignment' is the attribute, and "CENTER" is what the attribute is set to).

It is important to know that the `xmlns:fx="http://javafx.com/fxml"` attribute in the outermost object is necessary for the injection properties FXML has. 

The `fx:controller="com.sdc.javafx_demo.HelloController"` attribute defines what object has control of the parts of this object that are given names / have actions. When not set an object is only created and injected if the `FXMLLoader` is assigned an object back in the java code.

To clarify, injection is the process of having an object, and then setting its fields that is has declared with values externally. This is done using reflection (don't worry about it).

 and the `FXMLLoader` will automatically create the object specified by this call, injecting the fields with a `fx:id` attribute into the controller object (such as the `<Label fx:id="welcomeText"/>), and calling methods of that object when a functions such as the Button's `onAction` attribute delegate to a method (all method calls must start with '#').

This FXML allows us to create a controller that looks like this:
```java
package com.sdc.javafx_demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DemoController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
```
**You probably noticed the @FXML annotation above the field and method that FXML injects / refers to. This is necessary for the reflection to properly work!**

Note that if we wanted to access the welcomeText label to do some processing with it after it has been injected, which **happens after the object is constructed, i.e. cannot be accessed from the constructor**, we can do so by using the JavaFX named `initialize()` method like so:

```java
    @FXML
    private Label welcomeText;

    @FXML
    private void initialize() {
        welcomeText.setText("I set this in this initialize method!");
    }

```

Note that the method can be private because of reflection that the FXMLLoader takes care of for use, and allows us to keep our code abstracted away.

That is a lot to understand about what is happening when the `FXMLLoader` is parsing the file, so lets recap.
1. Call `FXMLLoader.load()` after setting the url
2. The `FXMLLoader` parses our FXML file. If it is valid it does not throw an exception and creates the FXML objects declared as they are defined.
3. If the controller is set for the `FXMLLoader`, or if one is set in the FXML itself, that controller object is created and the fields with `fx:id` attributes are injected into it. Methods that are set in the FXML are set to point to the controller object as well.

That is the just of how JavaFX works. I understand if this brief explanation leaves something to be desired. If you want to understand this at a deeper level please check out a tutorial I made for my teammates last semester [here](https://github.com/segedi-UW/SDC-IDE). It is more advanced and goes over a lot more detail so if you don't care for this tutorial I would recommend that one. I will also link it at the top of the page.

# Drawing Programmatically in JavaFX Canvas
In order to draw to the screen in a dynamic and non-ui manner, you will use a `Canvas` object. This canvas object contains a context that can be used to draw to the screen. An image can be drawn using the context in addition to shapes, lines, etc. Using this in conjunction with an `AnimationTimer` one can create a fully fledged Game using JavaFX dynamic images etc. As an example I have revamped the starter code with an example FXML file and Controller that takes advantage of a `Canvas` object and an `AnimationTimer` as a quick show for what can be done with it.

The FXML file is dead simple:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.canvas.Canvas?>
<?import javafx.scene.layout.StackPane?>
<StackPane xmlns:fx="http://javafx.com/fxml"
           fx:controller="com.sdc.javafx_demo.CanvasController">
    <Canvas fx:id="canvas" height="600" width="600"/>
</StackPane>
```
Note that the size of the canvas (height and width attributes) need to be set at some point in order for the canvas to work properly. These could also be set in the controller if wanted.

The controller takes advantage of all the stuff we have talked about so far minus the injected methods:

```java
// package declaration and imports

public class CanvasController {


    // injected field 
    @FXML private Canvas canvas;

    private AnimationTimer animationTimer;
    private double mx, my;
    private String hint;
    private Random random;

    private LinkedList<Explosion> explosions;

    @FXML
    private void initialize() {
        System.out.println("Canvas is null in initialize(): " + (canvas == null));
        canvas.setFocusTraversable(true);

        // mouse handling (updateMouse is below)
        canvas.setOnMouseMoved(this::updateMouse);
        canvas.setOnMouseClicked(this::updateMouse);

        // cannot use a lambda with an abstract class
        animationTimer = new AnimationTimer() {
            // handle is called to match 60 fps by JavaFX
            @Override
            public void handle(long now) {
                GraphicsContext c = canvas.getGraphicsContext2D();
                // clear the background
                c.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
                // draw a circle at the cursor if it is over the canvas
                if (canvas.contains(mx,my)) {
                    c.fillOval(mx, my, 4, 4);
                    c.fillText(hint, mx, my+30);
                }

                // common graphics methods
                explosions.forEach(e -> {
                    e.tick();
                    e.draw(c);
                });
                // remove dead explosions
                explosions.removeIf(e -> e.ticks <= 0);
            }
        };
        animationTimer.start();
        System.out.println("Started");
    }

    private void updateMouse(MouseEvent mouseEvent) {
        mx = mouseEvent.getX();
        my = mouseEvent.getY();
        if (mouseEvent.getClickCount() == 1 && canvas.contains(mx, my)) {
            hint = "Pretty cool eh?!";
            for (int i = 0; i < 100; i++) {
                double ry = (random.nextDouble())*2.0 * (random.nextFloat() > 0.5 ? 1 : -1);
                double rx = (random.nextDouble())*2.0 * (random.nextFloat() > 0.5 ? 1 : -1);
                double rr = random.nextFloat();
                double rg = random.nextFloat();
                double rb = random.nextFloat();
                explosions.add(new Explosion(mx, my, rx, ry, new Color(rr, rg, rb, 1.0)));
            }
        }
    }

    public CanvasController() {
        // note that the canvas is always null here!
        System.out.println("Canvas is null in constructor: " + (canvas == null));
        hint = "Try clicking!";
        random = new Random();
        explosions = new LinkedList<>();
    }

}

```

Notice that the `AnimationTimer` is called every 1/60th of a second by JavaFX, so we continually draw on the Canvas to achieve animations / pictures. The general format is as follows: 
1. Get the GraphicsContext
2. Clear the screen (this removes old artifacts)
2. Repaint by iterating over a list of drawable objects (objects that have both the `tick()` and `draw(GraphicsContext)` methods) calling `tick()` then `draw(GraphicsContext)`.

Efficient implementations of the iteration will remove the object in place, which can be done using a ListIterator, but I neglected to do so here as I did not want to complicate the example more than I already have. Instead, I have a `removeIf()` call with a lambda for the `Explosion` objects death criteria.

The Explosion referenced to is a private class as detailed here:

```java
    private static class Explosion {
        private double x, y;
        private double vx, vy;
        private Color color;
        private final int oTicks = 60*3;
        private int ticks;

        public Explosion(double x, double y, double vx, double vy, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.ticks = oTicks;
        }

        public void tick() {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (float)ticks/(float)oTicks);
            this.x += vx;
            this.y += vy;
            ticks--;
        }

        public void draw(GraphicsContext c) {
            // save the current context
            c.save();
            // can make changes freely
            c.setFill(color);
            c.fillRect(x, y, 3, 3);

            // restore the old context
            c.restore();
        }
    }
```

You should note the `tick()` and `draw(GraphicsContext c)` methods, which are a very common way of handling drawing objects. Typically you would make a super class or interface that has these two methods that other objects that you can draw inherit / implement.

In the `tick()` method we have a standard x and y coord update based off a set velocity. I also decrement ticks as they have a set lifetime of approx 3 seconds encoded into the oTicks field (3 * 60). I decrement the opacity of the explosions overtime which is that first line where I change the color.

The most important thing is to `save()` and `restore()` the context each time you use it. Not doing so will result in many bugs that you will feel like came out of nowhere as the state of the context does not reset, so when we change things like the fill or set points (another way of drawing) they wll persist and end up as artifacts unless we save / restore.

This is a very brief introduction, and I have more to add but unfortunately there is a time constraint. Please reach out to me with any questions and I will be more than happy to help you out!