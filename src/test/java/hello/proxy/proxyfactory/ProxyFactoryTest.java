package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    /**
     * InvocationHandler(인터페이스 O)
     */
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();

        /*
         프록시 객체 생성.
         매개변수로 넘긴 target의 인터페이스 유무를 자동으로 확인하고, 여기서는 있기 때문에 jdk동적 프록시로 생성한다
         또한, target의 정보를 이미 넘겼기 때문에 TimeAdvice에서 target 정보 따로 받을 필요 없이
         target의 메서드 정보를 가지고 있는 invocation으로 메서드를 바로 실행 가능.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
        proxy.find();
        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체클래스가 있으면 CGLIB 사용")
    void concreteProxy() {

        ConcreteService target = new ConcreteService();
        /*
         프록시 객체 생성.
         여기서 target의 정보를 proxyFactory에 넘겼기 때문에
         TimeAdvice에서 target 정보 따로 받을 필요 없이 invocation으로 실행 가능.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 사용하고, 클래스 기반 프록시 생성")
    void proxyTargetTest() {
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        /*
        target이 인터페이스 구현체임에도, CGLIB 기반의 프록시를 만들겠다는 설정.
        여기서는 ServiceInterface를 구현하는 프록시 객체를 만드는 것이 아니라,
        ServiceImpl를 상속하는 클래스 기반 프록시 객체를 만든다.
         */
        proxyFactory.setProxyTargetClass(true);

        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
        proxy.find();
        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

}
