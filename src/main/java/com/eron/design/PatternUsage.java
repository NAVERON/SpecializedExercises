package com.eron.design;

import com.eron.design.pattern.AbstractFactory.AbstractGuiFramework;
import com.eron.design.pattern.AbstractFactory.MyButton;
import com.eron.design.pattern.AbstractFactory.MyCheckBox;
import com.eron.design.pattern.AbstractFactory.UnixLikeGui;
import com.eron.design.pattern.AbstractFactory.WinGui;
import com.eron.design.pattern.Adaptor.RoundHole;
import com.eron.design.pattern.Adaptor.RoundPeg;
import com.eron.design.pattern.Adaptor.SquarePeg;
import com.eron.design.pattern.Adaptor.SquarePegAdaptor;
import com.eron.design.pattern.Bridge.AdvanceRemoteController;
import com.eron.design.pattern.Bridge.Device;
import com.eron.design.pattern.Bridge.Radio;
import com.eron.design.pattern.Bridge.Remote;
import com.eron.design.pattern.Bridge.RemoteController;
import com.eron.design.pattern.Bridge.TV;
import com.eron.design.pattern.Builder.Car;
import com.eron.design.pattern.Builder.CarBuilder;
import com.eron.design.pattern.Builder.Director;
import com.eron.design.pattern.Builder.Thing;
import com.eron.design.pattern.Builder.ThingBuilder;
import com.eron.design.pattern.ChainOfResponsibility;
import com.eron.design.pattern.ChainOfResponsibility.FakeService;
import com.eron.design.pattern.ChainOfResponsibility.Middleware;
import com.eron.design.pattern.ChainOfResponsibility.ThrottlingMiddleware;
import com.eron.design.pattern.ChainOfResponsibility.UserExistMiddleWare;
import com.eron.design.pattern.ChainOfResponsibility.UserRoleMiddleWare;
import com.eron.design.pattern.Composite.CompoundGraphic;
import com.eron.design.pattern.Composite.Dot;
import com.eron.design.pattern.Composite.Graphic;
import com.eron.design.pattern.Composite.Triangle;
import com.eron.design.pattern.Decorator.CompressionDataSourceDecorator;
import com.eron.design.pattern.Decorator.DataSourceWrapper;
import com.eron.design.pattern.Decorator.EncryptionDataSourceDecorator;
import com.eron.design.pattern.Decorator.FileDataSource;
import com.eron.design.pattern.Facade.VideoConversionFacade;
import com.eron.design.pattern.FactoryMethod.MyDialog;
import com.eron.design.pattern.FactoryMethod.MyDialogA;
import com.eron.design.pattern.FactoryMethod.MyDialogB;
import com.eron.design.pattern.FlyWeight.Forest;
import com.eron.design.pattern.Prototype.Circle;
import com.eron.design.pattern.Prototype.Rectangle;
import com.eron.design.pattern.Prototype.Shape;
import com.eron.design.pattern.Proxy.ThirdPartLib;
import com.eron.design.pattern.Proxy.YouTubeCacheProxy;
import com.eron.design.pattern.Proxy.YouTubeSource;
import com.eron.design.pattern.Singleton;
import com.eron.design.pattern.Singleton.DataBase;
import com.eron.design.pattern.Singleton.SingletonEnum;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设计模式的实际使用
 */
public class PatternUsage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatternUsage.class);

    public static void main(String[] args) {
        PatternUsage patternUsage = new PatternUsage();

        // 创建型 设计模式
        patternUsage.factoryMethod();
        patternUsage.abstractFactory();
        patternUsage.builder();
        patternUsage.prototype();
        patternUsage.singleton();

        // 结构型设计模式
        patternUsage.adaptor();
        patternUsage.bridge();
        patternUsage.composite();
        patternUsage.decorator();
        patternUsage.facade();
        patternUsage.flyweight();
        patternUsage.proxy();

        // 行为型设计模式
        patternUsage.chainOfResponsibility();

    }

    // 工厂方法
    private void factoryMethod() {
        // 工厂方法 有点类似于模板方法
        String configKey = System.getProperty("os.name");
        MyDialog dialog = "windows".equals(configKey) ? new MyDialogA() : new MyDialogB();
        dialog.render();
    }

    // 虚拟工厂
    private void abstractFactory() {
        // 待确定工厂和时间生产的产品
        AbstractGuiFramework factory;
        MyButton button;
        MyCheckBox checkBox;

        // 获取环境配置
        String configKey = System.getProperty("os.name");

        // 替换不同的工厂，生产同类型不同风格的组件
        factory = "windows".equals(configKey) ? new WinGui() : new UnixLikeGui();
        // 同一工厂 生产的产品属于同一风格/同组
        button = factory.createButton();
        checkBox = factory.createCheckBox();

        // 其他底层的实现...
        button.paint();
        button.onHover();
        checkBox.paint();
        checkBox.onChecked();
    }

    // 创建模式
    private void builder() {
        Director director = new Director();
        CarBuilder carBuilder = new CarBuilder();
        ThingBuilder thingBuilder = new ThingBuilder();

        // 没有主管director 可以直接使用builder内部默认实现构造对象
        director.constructSUV(carBuilder);
        Car car = carBuilder.build();
        LOGGER.info("builder pattern car --> {}", car);

        director.constructSportCar(thingBuilder);
        Thing thing = thingBuilder.build();
        LOGGER.info("builder pattern thing --> {}", thing);
    }

    // 原型模式
    private void prototype() {
        List<Shape> shapes = new LinkedList<>();

        Rectangle rectangle = new Rectangle();
        rectangle.x = 10;
        rectangle.y = 5;
        rectangle.color = "RED";
        rectangle.width = 100;
        rectangle.height = 35;

        shapes.add(rectangle);
        Rectangle anotherRectangle = (Rectangle) rectangle.clone();
        anotherRectangle.color = "BLUE";
        shapes.add(anotherRectangle);

        Circle circle = new Circle();
        circle.radius = 15;
        shapes.add(circle);
        LOGGER.info("prototype clone object --> {}", shapes);

        // 可以实现原型对象 缓存，封装快速获取新的原型对象
        Map<String, Shape> cache = new ConcurrentHashMap<>();
        cache.put("RECTANGLE", rectangle);
        cache.put("CIRCLE", circle);
        // 当需要获取新的原型克隆对象时

        Shape newShape = cache.get("RECTANGLE").clone();
        LOGGER.info("从缓存原型中获取克隆对象 --> {}", newShape);
    }

    // 单例模式
    private void singleton() {
        Singleton singleton1 = Singleton.getInstance();

        DataBase singleton2 = DataBase.getInstance();

        SingletonEnum singleton3 = SingletonEnum.INSTANCE;

        singleton1.action();
        singleton2.action();
        singleton3.action();
    }

    // 适配器
    private void adaptor() {
        RoundHole roundHole = new RoundHole(10);
        RoundPeg roundPeg = new RoundPeg(5);

        LOGGER.info("圆孔 vs 圆钉 --> {}", roundHole.fits(roundPeg));
        SquarePeg squarePeg = new SquarePeg(20);
        SquarePegAdaptor squarePegAdaptor = new SquarePegAdaptor(squarePeg);
        LOGGER.info("圆孔 vs 方钉 --> {}", roundHole.fits(squarePegAdaptor));
    }

    // 桥接模式
    private void bridge() {
        Device device = new TV();
        Remote controller = new RemoteController(device);
        LOGGER.info("设备状态 --> {}", controller.getDevice());

        controller.togglePower();
        controller.volumeUp();
        controller.channelUp();
        LOGGER.info("当前设备状态 --> {}", controller.getDevice());

        Device radio = new Radio();
        controller = new AdvanceRemoteController(radio);
        LOGGER.info("更换后 设备状态 --> {}", controller.getDevice());

        controller.togglePower();
        controller.volumeUp();
        controller.channelUp();
        LOGGER.info("更换后 设备状态 --> {}", controller.getDevice());
    }

    // 组合模是
    private void composite() {
        List<Graphic> imageEditor = new ArrayList<>();

        Dot dot = new Dot(10, 20);
        Triangle triangle = new Triangle(5, 5, 10);
        imageEditor.add(dot);
        imageEditor.add(triangle);

        CompoundGraphic groupGraphic = new CompoundGraphic(); // 组合的图形 也算作一种图形
        groupGraphic.addGraphic(dot);
        groupGraphic.addGraphic(triangle);

        imageEditor.add(groupGraphic);

        imageEditor.forEach(Graphic::draw);
    }

    // 装饰器
    public void decorator() {
        String rawText = "hello world";

        FileDataSource dataSource = new FileDataSource();
        // 以下为装饰器部分
        DataSourceWrapper wrapper = new DataSourceWrapper(dataSource); // 包装之前的实现
        DataSourceWrapper encryptDecorator = new EncryptionDataSourceDecorator(wrapper);
        DataSourceWrapper compressionDecorator = new CompressionDataSourceDecorator(encryptDecorator);

        compressionDecorator.writeData(rawText);
        compressionDecorator.readData();
    }

    // 门面
    public void facade() {
        VideoConversionFacade facade = new VideoConversionFacade();
        facade.convertFile("hello", "xxx");
    }

    // 享元
    public void flyweight() {
        Forest forest = new Forest();
        forest.plantTree(5, 5, "hello", "RED", "xxx");
        forest.plantTree(10, 10, "world", "RED", "sss");

        forest.plantTree(0, 0, "hello", "", ""); // color 空, 读取缓存中的内容
    }

    // 代理
    public void proxy() {
        ThirdPartLib source = new YouTubeSource();
        YouTubeCacheProxy cacheProxy = new YouTubeCacheProxy(source);

        cacheProxy.popularVideos();
        cacheProxy.getVideo("xxx");

        cacheProxy.popularVideos();
    }

    // 责任链模式
    public void chainOfResponsibility() {
        FakeService service = new FakeService(); // 底层数据服务
        Middleware throttlingMiddleware = new ThrottlingMiddleware(10);
        Middleware userCheck = new UserExistMiddleWare(service);
        Middleware roleCheck = new UserRoleMiddleWare();

        // 设置检查链
        Middleware.linkMiddleWare(throttlingMiddleware, userCheck, roleCheck);
        service.setMiddleWare(throttlingMiddleware);

        // 提前注册几个用户
        service.register("admin@root.com", "xxx");
        service.register("test@user.com", "sss");

        service.login("admin@root.com", "xxx");
        service.login("test@user.com", "sss");
        service.login("", "");
    }


}



