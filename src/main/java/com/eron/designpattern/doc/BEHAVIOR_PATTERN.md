# 行为设计模式

- [责任链模式](#TaskChain)
- [命令模式](#CommandPattern)
- [迭代模式](#Iterators)
- [观察者模式](#ObserverPattern)
- [中介模式](#MediatorPattern)
- [调解模式](#InterpreterPattern)
- [状态模式](#StatsPattern)
- [策略模式](#StrategyPattern)
- [访问者模式](#VisitorPattern)
- [MVC模式](#MVCPattern)
- [数据库访问模式](#DataAccessObjectPattern)

## TaskChain 

任务链可以生成一系列的任务序列，直接调用相同方法，可以实现一个流水线作业  
注意在`Logger`类中，`logMessage`方法里面存在`nextLogger`判断  

```java
abstract class Logger {
   protected Logger nextLogger;
//from  www.java2s.com
   public void setNextLogger(Logger nextLogger){
      this.nextLogger = nextLogger;
   }

   public void logMessage(String message){
      log(message);
      if(nextLogger !=null){
         nextLogger.logMessage(message);
      }
   }
   abstract protected void log(String message);  
}
class ConsoleLogger extends Logger {
   public ConsoleLogger(){
   }
   @Override
   protected void log(String message) {    
      System.out.println("Console::Logger: " + message);
   }
}
class EMailLogger extends Logger {
   public EMailLogger(){
   }
   @Override
   protected void log(String message) {    
      System.out.println("EMail::Logger: " + message);
   }
}
class FileLogger extends Logger {
   public FileLogger(){
   }
   @Override
   protected void log(String message) {    
      System.out.println("File::Logger: " + message);
   }
}
public class Main {  
   private static Logger getChainOfLoggers(){
      Logger emailLogger = new EMailLogger();
      Logger fileLogger = new FileLogger();
      Logger consoleLogger = new ConsoleLogger();
      emailLogger.setNextLogger(fileLogger);
      fileLogger.setNextLogger(consoleLogger);
      return emailLogger;  
   }
   public static void main(String[] args) {
      Logger loggerChain = getChainOfLoggers();
      loggerChain.logMessage("Null pointer");
      loggerChain.logMessage("Array Index Out of Bound");
      loggerChain.logMessage("Illegal Parameters");
   }
}
```

## CommandPattern 

这里体现了接口的一个特性，传入接口类型，可以调用时传入实现接口的任意类  
首先将实现接口命令的类压入  命令  列表，然后可以一次性执行  

```java
import java.util.ArrayList;
import java.util.List;
/*from  w w w.  j  ava  2s.c o  m*/
interface Command {
  void execute();
}

class MouseCursor {
  private int x = 10;
  private int y = 10;
  public void move() {
    System.out.println("Old Position:"+x +":"+y);
    x++;
    y++;
    System.out.println("New Position:"+x +":"+y);
    
  }

  public void reset() {
    System.out.println("reset");
    x = 10;
    y = 10;
  }
}

class MoveCursor implements Command {
  private MouseCursor abcStock;

  public MoveCursor(MouseCursor abcStock) {
    this.abcStock = abcStock;
  }

  public void execute() {
    abcStock.move();
  }
}

class ResetCursor implements Command {
  private MouseCursor abcStock;

  public ResetCursor(MouseCursor abcStock) {
    this.abcStock = abcStock;
  }

  public void execute() {
    abcStock.reset();
  }
}

class MouseCommands {
  private List<Command> orderList = new ArrayList<Command>();

  public void takeOrder(Command order) {
    orderList.add(order);
  }

  public void placeOrders() {
    for (Command order : orderList) {
      order.execute();
    }
    orderList.clear();
  }
}

public class Main {
  public static void main(String[] args) {
    MouseCursor cursor = new MouseCursor();

    MoveCursor moveCursor = new MoveCursor(cursor);
    ResetCursor resetCursor = new ResetCursor(cursor);

    MouseCommands commands= new MouseCommands();
    commands.takeOrder(moveCursor);
    commands.takeOrder(resetCursor);

    commands.placeOrders();
  }
}
```

## Iterators 

迭代模式可以内部进行查询迭代  
这种实现在链表里面存在，Iterator好像可以作为接口供其它类实现`Implement`，通过在内部写一个迭代器，实现内部类调用变量列表，依次实现`index`叠加  

```Java
interface Iterator {
   public boolean hasNext();
   public Object next();
}/*from w w w  . j a  v  a  2  s .  c  om*/
class LetterBag {
   public String names[] = {"R" , "J" ,"A" , "L"};
   public Iterator getIterator() {
      return new NameIterator();
   }
   class NameIterator implements Iterator {
      int index;
      @Override
      public boolean hasNext() {
         if(index < names.length){
            return true;
         }
         return false;
      }
      @Override
      public Object next() {
         if(this.hasNext()){
            return names[index++];
         }
         return null;
      }    
   }
}
public class Main {
   public static void main(String[] args) {
      LetterBag bag = new LetterBag();
      for(Iterator iter = bag.getIterator(); iter.hasNext();){
         String name = (String)iter.next();
         System.out.println("Name : " + name);
      }   
   }
}
```

## ObserverPattern 

`Java`里面有一套体系，只要分别实现即可，不需要自己实现结构  
观察者模式  
从我的理解，是将观察者放在了需要观察的对象里面，被观察对象在改变时，主动通知观察者（观察者在前期被加入到观察者对象列表里了）  

```Java
import java.util.ArrayList;
import java.util.List;
/*w ww  .  ja va2 s. c  o  m*/
class MyValue {
   private List<Observer> observers 
      = new ArrayList<Observer>();
   private int state;

   public int getState() {
      return state;
   }

   public void setState(int state) {
      this.state = state;
      notifyAllObservers();
   }

   public void attach(Observer observer){
      observers.add(observer);    
   }

   public void notifyAllObservers(){
      for (Observer observer : observers) {
         observer.update();
      }
   }   
}
abstract class Observer {
   protected MyValue subject;
   public abstract void update();
}
class PrinterObserver extends Observer{
   public PrinterObserver(MyValue subject){
      this.subject = subject;
      this.subject.attach(this);
   }

   @Override
   public void update() {
      System.out.println("Printer: " + subject.getState() ); 
   }
}
class EmailObserver extends Observer{

   public EmailObserver(MyValue subject){
      this.subject = subject;
      this.subject.attach(this);
   }

   @Override
   public void update() {
     System.out.println("Email: "+ subject.getState() ); 
   }
}
class FileObserver extends Observer{

   public FileObserver(MyValue subject){
      this.subject = subject;
      this.subject.attach(this);
   }

   @Override
   public void update() {
      System.out.println("File: " + subject.getState()); 
   }
}

public class Main {
   public static void main(String[] args) {
      MyValue subject = new MyValue();

      new FileObserver(subject);
      new EmailObserver(subject);
      new PrinterObserver(subject);

      subject.setState(15);
  
      subject.setState(10);
   }
}
```

## MediatorPattern  

在两个类之间传递, 降低对象之间的复杂度  
不明白  
使用一个类，在两个类之间实现数据传递  

```Java
class Printer {/*  w  w  w.  j  a  v a 2 s .co m*/
   public static void showMessage(Machine user, String message){
      System.out.println(new java.util.Date().toString()
         + " [" + user.getName() +"] : " + message);
   }
}
class Machine {
   private String name;

   public Machine(String name){
      this.name  = name;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public void sendMessage(String message){
      Printer.showMessage(this,message);
   }
}
class Main {
   public static void main(String[] args) {
      Machine m1= new Machine("M1");
      Machine m2 = new Machine("M2");

      m1.sendMessage("Rebooting");
      m2.sendMessage("Computing");
   }
}
```

## InterpreterPattern 

解释器模式  

```Java 
interface Expression {
  public boolean evaluate(String context);
}//  w w w.  ja  v a  2 s . c om

class IsInExpression implements Expression {
  private String data;

  public IsInExpression(String data) {
    this.data = data;
  }

  @Override
  public boolean evaluate(String context) {
    if (context.contains(data)) {
      return true;
    }
    return false;
  }
}

class OrExpression implements Expression {

  private Expression expr1 = null;
  private Expression expr2 = null;

  public OrExpression(Expression expr1, Expression expr2) {
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  @Override
  public boolean evaluate(String context) {
    return expr1.evaluate(context) || expr2.evaluate(context);
  }
}

class AndExpression implements Expression {

  private Expression expr1 = null;
  private Expression expr2 = null;

  public AndExpression(Expression expr1, Expression expr2) {
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  @Override
  public boolean evaluate(String context) {
    return expr1.evaluate(context) && expr2.evaluate(context);
  }
}

public class Main {

  public static void main(String[] args) {
    Expression select = new IsInExpression("Select");
    Expression from = new IsInExpression("From");
    Expression isSelectFrom = new AndExpression(select, from);

    Expression insert = new IsInExpression("Insert");
    Expression update = new IsInExpression("Update");
    Expression isInsertOrUpdate = new OrExpression(insert, update);

    System.out.println(isSelectFrom.evaluate("Select"));
    System.out.println(isInsertOrUpdate.evaluate("Insert"));

    System.out.println(isSelectFrom.evaluate("Select From"));
    System.out.println(isInsertOrUpdate.evaluate("Update"));
  }
}
```

## StatsPattern 

通过接口实现将一个内容设置成不同的状态，在多个状态之间切换，并且给便`State`的状态  

```Java
interface State {
  public void doAction(Context context);
}/*from   w  w  w  . j a v a  2s.c  o m*/

class StartState implements State {
  public void doAction(Context context) {
    System.out.println("In start state");
    context.setState(this);
  }

  public String toString() {
    return "Start State";
  }
}

class StopState implements State {

  public void doAction(Context context) {
    System.out.println("In stop state");
    context.setState(this);
  }

  public String toString() {
    return "Stop State";
  }
}

class PlayState implements State {
  public void doAction(Context context) {
    System.out.println("In play state");
    context.setState(this);  
  }
  public String toString() {
    return "Play State";
  }
}

class Context {
  private State state;

  public Context() {
    state = null;
  }

  public void setState(State state) {
    this.state = state;
  }

  public State getState() {
    return state;
  }
}

public class Main {
  public static void main(String[] args) {
    Context context = new Context();

    StartState startState = new StartState();
    startState.doAction(context);

    System.out.println(context.getState().toString());

    PlayState playState = new PlayState();
    playState.doAction(context);
    
    StopState stopState = new StopState();
    stopState.doAction(context);

    System.out.println(context.getState().toString());
  }
}
```

## StrategyPattern 

在策略模式中，算法可以在运行时改变  
传进去的虽然是两个值，但是实现的是不同的类，所以实现的功能不同  
传进不同的接口实现，后面实现不同的功能(调用的是同一个变量)  

```Java
interface MathAlgorithm {
   public int calculate(int num1, int num2);
}//from  w  w  w .  java 2  s. c om
class MathAdd implements MathAlgorithm{
   @Override
   public int calculate(int num1, int num2) {
      return num1 + num2;
   }
}
class MathSubstract implements MathAlgorithm{
   @Override
   public int calculate(int num1, int num2) {
      return num1 - num2;
   }
}
class MathMultiply implements MathAlgorithm{
   @Override
   public int calculate(int num1, int num2) {
      return num1 * num2;
   }
}
class MathContext {
   private MathAlgorithm algorithm;

   public MathContext(MathAlgorithm strategy){
      this.algorithm = strategy;
   }

   public int execute(int num1, int num2){
      return algorithm.calculate(num1, num2);
   }
}
public class Main {
   public static void main(String[] args) {
      MathContext context = new MathContext(new MathAdd());    
      System.out.println("10 + 5 = " + context.execute(10, 5));

      context = new MathContext(new MathSubstract());    
      System.out.println("10 - 5 = " + context.execute(10, 5));

      context = new MathContext(new MathMultiply());    
      System.out.println("10 * 5 = " + context.execute(10, 5));
   }
}
```

## VisitorPattern 

一个节点允许另一个类访问自己的数据  

```Java
class TreeNode {/*from   ww  w  .  j a  va  2s .c o m*/
  private String name;
  public TreeNode(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public void accept(NodeVisitor v) {
    v.visit(this);
  }
}
interface NodeVisitor {
  public void visit(TreeNode n);
}
class ConsoleVisitor implements NodeVisitor {
  @Override
  public void visit(TreeNode n) {
    System.out.println("console:" + n.getName());
  }
}

class EmailVisitor implements NodeVisitor {
  @Override
  public void visit(TreeNode n) {
    System.out.println("email:" + n.getName());
  }
}

public class Main {
  public static void main(String[] args) {

    TreeNode computer = new TreeNode("Java2s.com");
    computer.accept(new ConsoleVisitor());
    computer.accept(new EmailVisitor());
  }
}
```

## MVCPattern 

> MVC Pattern stands for Model-View-Controller Pattern.  
> From the name we can see that the MVC pattern involves three parts:  
>    **Model** - Model represents an object carrying data. It can also have logic to update controller if its data changes.  
>    **View** - View represents the visualization of the data that model contains. Usually it has the UI logic.  
>    **Controller** - Controller references both Model and view. It controls the data flow into model object and updates the view whenever data changes. It keeps View and Model separate.  

```Java
class Employee {//from  w  w w. j  av  a  2 s. co m
  private String id;
  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class EmployeeView {
  public void printEmployeeDetails(String name, String no) {
    System.out.println("Employee: ");
    System.out.println("Name: " + name);
    System.out.println("ID: " + no);
  }
}

class EmployeeController {
  private Employee model;
  private EmployeeView view;

  public EmployeeController(Employee model, EmployeeView view) {
    this.model = model;
    this.view = view;
  }

  public void setEmployeeName(String name) {
    model.setName(name);
  }

  public String getEmployeeName() {
    return model.getName();
  }

  public void setEmployeeId(String rollNo) {
    model.setId(rollNo);
  }

  public String getEmployeeId() {
    return model.getId();
  }

  public void updateView() {
    view.printEmployeeDetails(model.getName(), model.getId());
  }
}

public class Main {
  public static void main(String[] args) {
    Employee model = new Employee();
    model.setName("Tom");
    model.setId("1");
    EmployeeView view = new EmployeeView();

    EmployeeController controller = new EmployeeController(model, view);
    controller.updateView();
    controller.setEmployeeName("New Name");
    controller.updateView();
  }
}
```

## DataAccessObjectPattern 

Data Access Object Pattern or DAO pattern separates data accessing API from high level business services.  
A DAO pattern usually has the following interface and classes.  

Data Access Object Interface defines the standard operations on a model object(s).  
Data Access Object class implements above interface. There could be more than one implementations, for example, one for database, one for file.  
Model Object Simple POJO containing get/set methods to store data.  

```Java
import java.util.ArrayList;
import java.util.List;
//  w w w . j a va 2 s. com
class Employee {
  private String name;
  private int id;

  Employee(String name, int id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}

interface EmployeeDao {
  public List<Employee> getAll();

  public Employee get(int id);

  public void updateStudent(Employee student);

  public void delete(Employee student);
}

class EmployeeDaoImpl implements EmployeeDao {
  List<Employee> employeeList;

  public EmployeeDaoImpl() {
    employeeList = new ArrayList<Employee>();
    Employee emp1 = new Employee("Jack", 0);
    Employee emp2 = new Employee("Tom", 1);
    employeeList.add(emp1);
    employeeList.add(emp2);
  }

  @Override
  public void delete(Employee student) {
    employeeList.remove(student.getId());
    System.out.println("Employee: No " + student.getId()
        + ", deleted from database");
  }

  @Override
  public List<Employee> getAll() {
    return employeeList;
  }

  @Override
  public Employee get(int rollNo) {
    return employeeList.get(rollNo);
  }

  @Override
  public void updateStudent(Employee emp) {
    employeeList.get(emp.getId()).setName(emp.getName());
    System.out.println("Emp:No " + emp.getId()
        + ", updated in the database");
  }
}

public class Main {
  public static void main(String[] args) {
    EmployeeDao empDao = new EmployeeDaoImpl();
    for (Employee emp : empDao.getAll()) {
      System.out.println("Emp: [No : " + emp.getId() + ", Name : "
          + emp.getName() + " ]");
    }
    Employee emp = empDao.getAll().get(0);
    emp.setName("Jane");
    empDao.updateStudent(emp);

    empDao.get(0);
    System.out.println("Emp: [No : " + emp.getId() + ", Name : "
        + emp.getName() + " ]");
  }
}
```

