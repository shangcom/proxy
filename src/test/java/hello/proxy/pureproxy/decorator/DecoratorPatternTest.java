package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.MessageDecorator;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import hello.proxy.pureproxy.decorator.code.TimeDecorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    @Test
    void decorator() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator decorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(decorator);

        client.execute();
    }

    /**
     * 추상 클래스 적용
     */
    @Test
    void decorator2() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);

        client.execute();
    }

    @Test
    void decorator3() {
        hello.proxy.pureproxy.decorator.code.abstractclass.RealComponent realComponent = new hello.proxy.pureproxy.decorator.code.abstractclass.RealComponent();
        hello.proxy.pureproxy.decorator.code.abstractclass.MessageDecorator messageDecorator = new hello.proxy.pureproxy.decorator.code.abstractclass.MessageDecorator(realComponent);
        hello.proxy.pureproxy.decorator.code.abstractclass.TimeDecorator timeDecorator = new hello.proxy.pureproxy.decorator.code.abstractclass.TimeDecorator(messageDecorator);
        hello.proxy.pureproxy.decorator.code.abstractclass.DecoratorPatternClient decoratorPatternClient = new hello.proxy.pureproxy.decorator.code.abstractclass.DecoratorPatternClient(timeDecorator);
        decoratorPatternClient.execute();
    }
}
