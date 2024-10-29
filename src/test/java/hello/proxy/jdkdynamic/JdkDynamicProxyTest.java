package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    /*
    동적 프록시로 메서드 호출을 가로채서 공통 로직을 적용.
     */
    @Test
    void dynamicA() {
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        AInterface proxy = (AInterface)Proxy.newProxyInstance(
                AInterface.class.getClassLoader(),
                new Class[]{AInterface.class},
                handler
        );

        //
        proxy.call();
        log.info("TargetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }


    @Test
    void dynamicB() {

        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("TargetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }
}
