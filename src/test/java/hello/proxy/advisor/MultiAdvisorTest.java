package hello.proxy.advisor;

import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
public class MultiAdvisorTest {

    /*
     * 구조 : client -> proxy2(advisor2) -> proxy1(advisor1) -> target
     */
    @Test
    @DisplayName("다중 프록시")
    void multiAdvisorTest1() {

        // 프록시 1 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        // 프록시 2 생성
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory2.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        proxy2.save();
    }

    @Test
    @DisplayName("단일 프록시에 복수 어드바이저")
    void multiAdvisorTest2() {
        /*
         * 구조 : client -> proxy(advisor2 -> advisor1) -> target
         */
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());

        // 어드바이저를 프록시 팩토리에 등록한 순서대로 작동한다.
        proxyFactory.addAdvisor(advisor2);
        proxyFactory.addAdvisor(advisor1);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save(); // 호출 시 벌어지는 일
        /*
         * proxy.save()
         * -> advisor2의 Advice2.invoke 실행
         * -> 전처리
         * -> invocation.proceed()로 다음 어드바이스 체인 호출
         * -> advisor1의 Advice1.invoke 실행
         * -> 전처리
         * -> invocation.proceed()로 타겟 메서드 호출
         * -> 타겟 메서드 실행
         * -> advisor1의 후처리 및 Advice1.invoke 종료
         * -> advisor2의 후처리 및 Advice2.invoke 종료
         * -> 종료
         */

    }

    @Slf4j
    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }
    @Slf4j
    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
