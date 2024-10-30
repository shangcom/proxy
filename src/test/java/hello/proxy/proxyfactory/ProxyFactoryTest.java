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

/**
 * ProxyFactoryTest와 같이 사용자가 어드바이저를 명시적으로 추가하지 않고 Advice만 추가할 경우,
 * 스프링은 자동으로 DefaultPointcutAdvisor를 생성하며,
 * 이때 포인트컷으로는 Pointcut.TRUE(모든 메서드에 적용)를 사용함.
 * 이를 통해 어드바이스가 모든 메서드에 적용되도록 하여 기본적인 부가 기능을 일괄 적용.
 * Advice는 어드바이저 없이도 사용할 수 있는 것처럼 보이지만, 사실 스프링 AOP 내부에서는 항상 어드바이저를 통해 적용됩니다.
 * 포인트컷은 항상 어드바이저와 함께 사용되어야 하며, 포인트컷이 없을 경우에는 스프링이 자동으로 Pointcut.TRUE와 결합하여
 * 모든 메서드에 어드바이스를 적용할 수 있도록 한다.
 */
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
         매개변수로 넘긴 target의 인터페이스 유무를 자동으로 확인하고, 여기서는 있기 때문에 jdk동적 프록시로 생성한다.
         또한, target의 정보를 이미 넘겼기 때문에 TimeAdvice에서 target 정보 따로 받을 필요 없이
         target의 메서드 정보를 가지고 있는 invocation으로 메서드를 바로 실행 가능.
         */
        // target 객체를 자동으로 검사해 인터페이스 유뮤 확인을 통한 프록시 생성 방식 결정. (CGLIB으로 결정)
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // addAdvice() : 프록시에 추가할 부가 기능(어드바이스) 설정. 프록시 생성 방식과 무관.
        proxyFactory.addAdvice(new TimeAdvice());
        // 프록시팩토리 생성시 결정된대로 인터페이스 기반의 프록시 객체 생성.
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
