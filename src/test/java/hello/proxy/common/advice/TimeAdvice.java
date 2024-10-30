package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodInterceptor
 * 메서드 안에서 직접 메서드를 호출하는 방식으로, 메서드 호출의 완전한 제어를 제공.
 * 메서드 호출 전후 및 중간에 로직을 배치할 수 있으며, 메서드 호출을 직접 제어할 수 있기 때문에
 * 시간 측정, 트랜잭션 관리 등 전후 처리가 필요한 경우 유리.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    /**
     * invoke(MethodINvocation invocation)
     * 부가 기능을 정의하는 메서드
     * 메서드 호출을 가로채는 시점에 실행됨.
     * 내부에 부가기능을 추가할 수 있으며, proceed()를 호출해 언본 메서드로 흐름을 넘겨줌.
     * @param invocation 대상 메서드(Method method), 인수(args) 가지고 있음.
     * invocation.proceed() : 대상 메서드 호출.
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

//        Object result = method.invoke(target, args);
        /*
        invocation.proceed()
        MethodInvocation 객체에서 제공하는 메서드로, 원본 메서드 호출을 실행
         */
        Object result = invocation.proceed(); // 프록시 팩토리에서 생성 시 target, args를 받음.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료, resultTime = {}", resultTime);

        return result;
    }
}
