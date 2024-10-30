package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;


@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        /*
        Pointcut.TRUE : 정적 매칭만 지원하며, 런타임 인수 조건이나 isRuntime()이 필요하지 않은 포인트컷.
        스프링 AOP에서 제공하는 미리 정의된 포인트컷 구현체. 모든 메서드를 대상으로 항상 true를 반환하는 기본 포인트컷.
         */
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Pointcut.TRUE : 모든 메서드에 TimeAdvice를 적용
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    void advisorTest3() {

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // NameMatchMethodPointcut 생성, "save" 적용.
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());

        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    /*
    프록시가 메서드를 호출할 때마다, 스프링은 해당 메서드가 어드바이저의 포인트컷 조건을 만족하는지 확인.
    메서드 매칭은 MethodMatcher를 통해 수행하도록 지정.
     */
    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE; // 모든 클래스에 대해 적용
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMather(); // 메서드 매칭은 MyMethodMatcher를 통해 수행하도록 지정.
        }
    }

    static class MyMethodMather implements MethodMatcher {

        private String matchName = "save";

        /*
        정적 매칭 : 프록시 생성 시점에 평가되며, 이후 메서드 호출 시에 별도 평가 없이 기존 결과를 사용.
        메서드 매칭이 클래스와 메서드 시그니처에 의해 고정된 조건(메서드 이름, 파라미터, 반환 타입, 클래스 타입 등)으로 평가
        작동 시점: 프록시 생성 시, 스프링이 모든 메서드에 대해 초기 필터링을 수행할 때 이 메서드를 호출.
        프록시 생성 시 미리 평가되어 저장되므로, 성능에 유리
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method = {}, targetClass = {}", method.getName(), targetClass);
            log.info("포인트컷 결과 result = {}", result);
            return result;
        }

        /*
        런타임 매칭 :  메서드 호출 시점에서 매개변수 값을 기반으로 동적 평가. 메서드 호출 시점마다 평가가 반복.
        매칭 조건에 런타임 인수 값을 포함할지 여부를 지정.
        스프링은 매번 메서드 호출 시 마다 두 번째 matches() 메서드를 호출하여 인수 값에 따라 조건을 평가
        작동 시점 : 프록시가 생성된 후, 메서드가 실제 호출될 때마다 isRuntime()을 확인하여 인수 값을 고려한
                   추가 매칭이 필요한지 결정.
         */

        @Override
        public boolean isRuntime() {
            return false;
        }

        /*
         실제 메서드 호출 시점에서 매개변수 값까지 고려하여 매칭.
         작동 시점: 이 메서드는 isRuntime()이 true일 때, 메서드가 호출될 때마다 실행.
                  호출된 메서드의 인수 값을 포함한 매칭 조건을 평가.
         isRuntime()이 true일 경우에만 호출되며, 인수 값에 따라 어드바이스가 조건부로 적용.
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
