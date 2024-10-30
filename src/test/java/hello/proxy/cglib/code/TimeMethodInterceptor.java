package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * org.springframework.cglib.proxy.MethodInterceptor (CGLIB 전용)
 * 사용 대상: 클래스 기반 프록시. 구체 클래스를 상속받아 프록시를 생성
 * 프록시 생성 방식: CGLIB을 통해 생성되며, 인터페이스가 필요 없다.
 * 메서드 가로채기 방식 : invoke() 메서드에 MethodProxy 객체 제공.
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {
    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();


        // Object result = method.invoke(target, args);
        Object result = methodProxy.invoke(target, args); // cglib에서는 method 대신 methodProxy 사용


        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료, resultTime = {}", resultTime);

        return result;
    }
}
