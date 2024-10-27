package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClitent;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ConcreteProxyTest {

    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClitent clitent = new ConcreteClitent(concreteLogic);

        clitent.execute();
    }

    @Test
    void addProxy() {
        ConcreteLogic target = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(target);
        ConcreteClitent clitent = new ConcreteClitent(timeProxy);

        clitent.execute();
    }
}
