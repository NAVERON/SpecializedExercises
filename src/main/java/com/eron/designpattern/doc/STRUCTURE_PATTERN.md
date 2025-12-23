# 结构设计模式

- [适配器模式](#AdapterPattern)  
- [桥接模式](#BridgePattern)  
- [过滤器模式](#FilterChainPattern)  
- [外观模式](#FacatePattern)  
- [代理模式](#ProxyPattern)  

## AdapterPattern 

例如一个电脑只支持一种内存卡，现在需要让电脑支持另一种内存卡，需要一个中间件，可以认为是驱动  
使用播放器作为例子  

```java
interface Player {
   public void play(String type, String fileName);
}/*  www.jav  a  2  s .com*/
interface AudioPlayer {  
   public void playAudio(String fileName);
}
interface VideoPlayer {  
   public void playVideo(String fileName);
}
class MyAudioPlayer implements AudioPlayer {
   @Override
   public void playAudio(String fileName) {
      System.out.println("Playing. Name: "+ fileName);    
   }
}
class MyVideoPlayer implements VideoPlayer {
   @Override
   public void playVideo(String fileName) {
      System.out.println("Playing. Name: "+ fileName);    
   }
}

class MyPlayer implements Player {

   AudioPlayer audioPlayer = new MyAudioPlayer();
   VideoPlayer videoPlayer = new MyVideoPlayer();
   
   public MyPlayer(){      
   }
   @Override
   public void play(String audioType, String fileName) {
      if(audioType.equalsIgnoreCase("avi")){
         videoPlayer.playVideo(fileName);
      }else if(audioType.equalsIgnoreCase("mp3")){
         audioPlayer.playAudio(fileName);
      }
   }
}
public class Main{
   public static void main(String[] args) {
      MyPlayer myPlayer = new MyPlayer();

      myPlayer.play("mp3", "h.mp3");
      myPlayer.play("avi", "me.avi");
   }
}
```

## BridgePattern 

使用虚类对接实现同一接口的类，这样就可以间接调用了  

```java
interface Printer {
   public void print(int radius, int x, int y);
}/*w w w . j av a  2s.  c  o m*/
class ColorPrinter implements Printer {
   @Override
   public void print(int radius, int x, int y) {
      System.out.println("Color: " + radius +", x: " +x+", "+ y +"]");
   }
}
class BlackPrinter implements Printer {
   @Override
   public void print(int radius, int x, int y) {
      System.out.println("Black: " + radius +", x: " +x+", "+ y +"]");
   }
}
abstract class Shape {
   protected Printer print;
   protected Shape(Printer p){
      this.print = p;
   }
   public abstract void draw();  
}
class Circle extends Shape {
   private int x, y, radius;

   public Circle(int x, int y, int radius, Printer draw) {
      super(draw);
      this.x = x;  
      this.y = y;  
      this.radius = radius;
   }

   public void draw() {
      print.print(radius,x,y);
   }
}
public class Main {
   public static void main(String[] args) {
      Shape redCircle = new Circle(100,100, 10, new ColorPrinter());
      Shape blackCircle = new Circle(100,100, 10, new BlackPrinter());

      redCircle.draw();
      blackCircle.draw();
   }
}
```

## FilterChainPattern 

在同一接口中放置数据，实现接口可以对接口进行操作，在实现接口的类中对数据进行不同标准的筛选

```java
import java.util.List;
import java.util.ArrayList;
//from   ww  w.  j  av a2s  . c o m
class Employee {
  private String name;
  private String gender;
  private String retireStatus;

  public Employee(String name, String gender, String r) {
    this.name = name;
    this.gender = gender;
    this.retireStatus = r;
  }

  public String getName() {
    return name;
  }

  public String getGender() {
    return gender;
  }

  public String getRetireStatus() {
    return retireStatus;
  }

  @Override
  public String toString() {
    return "Employee [name=" + name + ", gender=" + gender
        + ", retireStatus=" + retireStatus + "]";
  }
}

interface Criteria {
  public List<Employee> meetCriteria(List<Employee> persons);
}

class CriteriaMale implements Criteria {

  @Override
  public List<Employee> meetCriteria(List<Employee> persons) {
    List<Employee> malePersons = new ArrayList<Employee>();
    for (Employee person : persons) {
      if (person.getGender().equalsIgnoreCase("MALE")) {
        malePersons.add(person);
      }
    }
    return malePersons;
  }
}

class CriteriaFemale implements Criteria {

  @Override
  public List<Employee> meetCriteria(List<Employee> persons) {
    List<Employee> femalePersons = new ArrayList<Employee>();
    for (Employee person : persons) {
      if (person.getGender().equalsIgnoreCase("FEMALE")) {
        femalePersons.add(person);
      }
    }
    return femalePersons;
  }
}

class CriteriaRetire implements Criteria {

  @Override
  public List<Employee> meetCriteria(List<Employee> persons) {
    List<Employee> singlePersons = new ArrayList<Employee>();
    for (Employee person : persons) {
      if (person.getRetireStatus().equalsIgnoreCase("YES")) {
        singlePersons.add(person);
      }
    }
    return singlePersons;
  }
}

class AndCriteria implements Criteria {

  private Criteria criteria;
  private Criteria otherCriteria;

  public AndCriteria(Criteria criteria, Criteria otherCriteria) {
    this.criteria = criteria;
    this.otherCriteria = otherCriteria;
  }

  @Override
  public List<Employee> meetCriteria(List<Employee> persons) {
    List<Employee> firstCriteriaPersons = criteria.meetCriteria(persons);
    return otherCriteria.meetCriteria(firstCriteriaPersons);
  }
}

class OrCriteria implements Criteria {

  private Criteria criteria;
  private Criteria otherCriteria;

  public OrCriteria(Criteria criteria, Criteria otherCriteria) {
    this.criteria = criteria;
    this.otherCriteria = otherCriteria;
  }

  @Override
  public List<Employee> meetCriteria(List<Employee> persons) {
    List<Employee> firstCriteriaItems = criteria.meetCriteria(persons);
    List<Employee> otherCriteriaItems = otherCriteria.meetCriteria(persons);

    for (Employee person : otherCriteriaItems) {
      if (!firstCriteriaItems.contains(person)) {
        firstCriteriaItems.add(person);
      }
    }
    return firstCriteriaItems;
  }
}

public class Main {
  public static void main(String[] args) {
    List<Employee> persons = new ArrayList<Employee>();

    persons.add(new Employee("Tom", "Male", "YES"));
    persons.add(new Employee("Jack", "Male", "NO"));
    persons.add(new Employee("Jane", "Female", "NO"));
    persons.add(new Employee("Diana", "Female", "YES"));
    persons.add(new Employee("Mike", "Male", "NO"));
    persons.add(new Employee("Bob", "Male", "YES"));

    Criteria male = new CriteriaMale();
    Criteria female = new CriteriaFemale();
    Criteria retire = new CriteriaRetire();
    Criteria retireMale = new AndCriteria(retire, male);
    Criteria retireOrFemale = new OrCriteria(retire, female);

    System.out.println("Males: ");
    printPersons(male.meetCriteria(persons));

    System.out.println("Females: ");
    printPersons(female.meetCriteria(persons));

    System.out.println("Retire Males: ");
    printPersons(retireMale.meetCriteria(persons));

    System.out.println("Retire Or Females: ");
    printPersons(retireOrFemale.meetCriteria(persons));
  }

  public static void printPersons(List<Employee> persons) {
    for (Employee person : persons) {
      System.out.println(person);
    }
  }
}
```

# FacatePattern 

提供一个供外界调用的接口，里面有具体的实现  

```java
class ShapeFacade {
  interface Shape {
    void draw();//  w w  w . j a  va2 s .  c  o m
  }
  class Rectangle implements Shape {
    @Override
    public void draw() {
      System.out.println("Rectangle::draw()");
    }
  }
  class Square implements Shape {
    @Override
    public void draw() {
      System.out.println("Square::draw()");
    }
  }
  class Circle implements Shape {
    @Override
    public void draw() {
      System.out.println("Circle::draw()");
    }
  }
  private Shape circle = new Circle();
  private Shape rectangle = new Rectangle();
  private Shape square = new Square();

  public ShapeFacade() {
  }
  public void drawCircle() {
    circle.draw();
  }
  public void drawRectangle() {
    rectangle.draw();
  }
  public void drawSquare() {
    square.draw();
  }
}
public class Main {
  public static void main(String[] args) {
    ShapeFacade shapeFacade = new ShapeFacade();
    shapeFacade.drawCircle();
    shapeFacade.drawRectangle();
    shapeFacade.drawSquare();
  }
}
```


# ProxyPattern 

顾名思义

```java
interface Printer {
   void print();//from   ww  w  .ja  v  a  2s.  com
}
class ConsolePrinter implements Printer {
   private String fileName;

   public ConsolePrinter(String fileName){
      this.fileName = fileName;
   }
   @Override
   public void print() {
      System.out.println("Displaying " + fileName);
   }
}
class ProxyPrinter implements Printer{
   private ConsolePrinter consolePrinter;
   private String fileName;

   public ProxyPrinter(String fileName){
      this.fileName = fileName;
   }

   @Override
   public void print() {
      if(consolePrinter == null){
         consolePrinter = new ConsolePrinter(fileName);
      }
      consolePrinter.print();
   }
}
public class Main {
  
   public static void main(String[] args) {
      Printer image = new ProxyPrinter("test");
      image.print();   
   }
}
```








