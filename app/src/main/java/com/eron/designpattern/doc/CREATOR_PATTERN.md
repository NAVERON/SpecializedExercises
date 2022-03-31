# 创建设计模式

- [Java工厂模式](#FactoryPattern)
- [虚拟工厂模式](#AbstractFactory)
- [单例模式](#SingletonPattern)
- [Java创建模式](#BuilderPattern)
- [Java原型模式](#PrototypePattern)


## FactoryPattern 

通过类型判断来创建不同的对象，工厂依据传进来的参数识别应该制造那种产品  

```java
public class ShapeFactory {
   //use getShape method to get object of type shape 
   public Shape getShape(String shapeType){
      if(shapeType == null){
         return null;
      }
      if(shapeType.equalsIgnoreCase("CIRCLE")){
         return new Circle();
      } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
         return new Rectangle();
      } else if(shapeType.equalsIgnoreCase("SQUARE")){
         return new Square();
      }
      return null;
   }
}
```

## AbstractFactory 

> 创建工厂的工厂模式

同工厂模式，首先创建一个接口  

```java
interface Shape {
   void draw();
}
////////////////////////////////
interface Printer{
   void print();
}
```

然后创建一个实现这个接口的类

```java
class Rectangle implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Rectangle::draw() method.");
   }
}
class Square implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Square::draw() method.");
   }
}
class Circle implements Shape {

   @Override
   public void draw() {
      System.out.println("Inside Circle::draw() method.");
   }
}
///////////////////////////////////////////////////////////////
class PaperPrinter implements Printer{

   @Override
   public void print() {
      System.out.println("paper");
   }
}
class WebPrinter implements Printer{

   @Override
   public void print() {
      System.out.println("web");
   }
}
class ScreenPrinter implements Printer{

   @Override
   public void print() {
      System.out.println("screen");
   }
}
```

之后我们可以创建一个虚拟类来得到打印和图形对象  

```java
abstract class AbstractFactory {
   abstract Printer getPrinter(String type);
   abstract Shape getShape(String shape) ;
}
//然后创建具体的类继承这个虚拟类
class ShapeFactory extends AbstractFactory {
  
   @Override
   public Shape getShape(String shapeType){
      if(shapeType == null){
         return null;
      }    
      if(shapeType.equalsIgnoreCase("CIRCLE")){
         return new Circle();
      } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
         return new Rectangle();
      } else if(shapeType.equalsIgnoreCase("SQUARE")){
         return new Square();
      }
      return null;
   }
   
   @Override
   Printer getPrinter(String type) {
      return null;
   }
}
class PrinterFactory extends AbstractFactory {
  
   @Override
   public Shape getShape(String shapeType){
      return null;
   }
   
   @Override
   Printer getPrinter(String type) {
   if(type == null){
      return null;
    }    
  if(type.equalsIgnoreCase("paper")){
      return new PaperPrinter();
  } else if(type.equalsIgnoreCase("web")){
      return new WebPrinter();
  } else if(type.equalsIgnoreCase("Screen")){
      return new ScreenPrinter();
  }
  return null;
 }
}
```

之后我们可以创建一个工厂生成类  

```java
class FactoryProducer {
   public static AbstractFactory getFactory(String choice){
      if(choice.equalsIgnoreCase("SHAPE")){
         return new ShapeFactory();
      } else if(choice.equalsIgnoreCase("Printer")){
         return new PrinterFactory();
      }
      return null;
   }
}
```

**那么现在如何使用呢？下面是如何使用虚拟工厂模式的例子**  

```java
public class Main {
   public static void main(String[] args) {

      //get shape factory
      AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");

      //get an object of Shape Circle
      Shape shape1 = shapeFactory.getShape("CIRCLE");

      //call draw method of Shape Circle
      shape1.draw();

      //get an object of Shape Rectangle
      Shape shape2 = shapeFactory.getShape("RECTANGLE");

      //call draw method of Shape Rectangle
      shape2.draw();
      
      //get an object of Shape Square 
      Shape shape3 = shapeFactory.getShape("SQUARE");

      //call draw method of Shape Square
      shape3.draw();

      //get printer factory
      AbstractFactory printerFactory = FactoryProducer.getFactory("printer");

    Printer printer1 = printerFactory.getPrinter("Paper");
    printer1.print();
    Printer printer2 = printerFactory.getPrinter("Web");
    printer2.print();
    Printer printer3 = printerFactory.getPrinter("Screen");
    printer3.print();
   }
}
```

## SingletonPattern 

特点：  
a 一个类只能通过自己创建自己  
b 保证只有一个实例可以被创建  
c 提供访问对象的方法  

例如，我们设计一个用户界面，我们只允许有一个界面，这个时候我们就需要使用单例模式了  

**案例**  
下面创建一个`MainWindow.class`的文件，构造函数是私有的，并且有一个自己的静态实例，提供一个静态方法去得到它的静态方法  

```java
class MainWindow {
   //create an object of MainWindow
   private static MainWindow instance = new MainWindow();

   //make the constructor private so that this class cannot be
   //instantiated by other class
   private MainWindow(){}

   //Get the only object available
   public static MainWindow getInstance(){
      return instance;
   }

   public void showMessage(){
      System.out.println("Hello World!");
   }
}

public class Main {
   public static void main(String[] args) {
      //Get the only object available
      MainWindow object = MainWindow.getInstance();

      //show the message
      object.showMessage();
   }
}
```

## BuilderPattern  

用来创建一些比较复杂的对象，比如说我们创建一个窗口，里面需要创建工具栏、菜单、小窗口等，我们可以使用创建者模式来隐藏创建的具体细节  

```java
class Menu {/*from  w ww .  ja v a 2s . c om*/
}
class ToolBar {
}
class MainWindow {
  Menu menu;
  ToolBar toolBar;
  public Menu getMenu() {
    return menu;
  }
  public void setMenu(Menu menu) {
    this.menu = menu;
  }
  public ToolBar getToolBar() {
    return toolBar;
  }
  public void setToolBar(ToolBar toolBar) {
    this.toolBar = toolBar;
  }
}
class WindowBuilder{
  public static MainWindow createWindow(){
    MainWindow window = new MainWindow();
    Menu menu = new Menu();
    ToolBar toolBar = new ToolBar();
    window.setMenu(menu);
    window.setToolBar(toolBar);
    return window;
  }
}
public class Main {
  public static void main(String[] args) {
    MainWindow object = WindowBuilder.createWindow();

  }
}
```

## PrototypePattern 

下面演示如何创建原型模式  
首先创建一个虚拟图形类，继承`Clonable`类，可以实现完全复制，原型模式中只是复制存在的对象，并不进行创建新的对象  
在创建一个对象挥消耗很大或者是资源密集型的，我们可以使用原型模式  

```java
abstract class Shape implements Cloneable {
   
   private String id;
   protected String type;
   
   abstract void draw();
   
   public String getType(){
      return type;
   }
   
   public String getId() {
      return id;
   }
   
   public void setId(String id) {
      this.id = id;
   }
   
   public Object clone() {
      Object clone = null;
      try {
         clone = super.clone();
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }
      return clone;
   }
}
```

然后创建3个继承Shape类  

```java
class Rectangle extends Shape {

   public Rectangle(){
     type = "Rectangle";
   }

   @Override
   public void draw() {
      System.out.println("Inside Rectangle::draw() method.");
   }
}
class Square extends Shape {

   public Square(){
     type = "Square";
   }

   @Override
   public void draw() {
      System.out.println("Inside Square::draw() method.");
   }
}
class Circle extends Shape {

   public Circle(){
     type = "Circle";
   }

   @Override
   public void draw() {
      System.out.println("Inside Circle::draw() method.");
   }
}
```

之后去创建`ShapeProtoType.class`去返回图形Shape的原型  

```java
class ShapeProtoType{
  /*ww w.j a v  a  2  s . co  m*/
   private static Hashtable<String, Shape> shapeMap 
      = new Hashtable<String, Shape>();

   public static Shape getShape(String shapeId) {
      Shape cachedShape = shapeMap.get(shapeId);
      return (Shape) cachedShape.clone();
   }
   public static void loadCache() {
      Circle circle = new Circle();
      circle.setId("1");
      shapeMap.put(circle.getId(),circle);

      Square square = new Square();
      square.setId("2");
      shapeMap.put(square.getId(),square);

      Rectangle rectangle = new Rectangle();
      rectangle.setId("3");
      shapeMap.put(rectangle.getId(),rectangle);
   }
}
public class Main{
   public static void main(String[] args) {
      ShapeProtoType.loadCache();

      Shape clonedShape = (Shape) ShapeProtoType.getShape("1");
      System.out.println("Shape : " + clonedShape.getType());    

      Shape clonedShape2 = (Shape) ShapeProtoType.getShape("2");
      System.out.println("Shape : " + clonedShape2.getType());    

      Shape clonedShape3 = (Shape) ShapeProtoType.getShape("3");
      System.out.println("Shape : " + clonedShape3.getType());    
   }
}
```








