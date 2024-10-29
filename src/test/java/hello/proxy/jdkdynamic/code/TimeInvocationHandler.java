package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target; // 프록시가 호출을 가로챌 대상.

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }


    /**
     *     프록시 객체의 모든 메서드 호출을 가로채서, 프록시 로직을 정의.
     *     JdkDynamicProxyTest에서 proxy.call(); 호출하면?
     *     프록시 객체(proxy)의 메서드(call)가 호출될 때마다 해당 호출을 InvocationHandler(handler)의 invoke 메서드로 위임.
     *     매개변수 proxy, method, args는 자동으로 전달된다.
     * @param proxy 프록시 객체 자체. newProxyInstance()로 만들어짐.
     *              현재 구현 메서드의 바디에서 전혀 사용하지 않고 있음.
     *              proxy 인자를 사용하는 경우 :
     *              1. 디버깅 또는 프록시 객체 정보 출력(예: proxy.getClass()).
     *              2. 여러 인터페이스를 구현한 프록시 객체에서 인터페이스별로 다른 로직을 적용할 경우
     * @param method  JDK는 리플렉션을 사용해 호출된 메서드(call)의 메타 정보를 Method 객체로 생성하고
     *                이를 invoke 메서드의 두 번째 인자로 전달.
     * @param args: 호출된 메서드에 전달된 인수의 배열. 인수가 없을 경우 args는 null 또는 빈 배열로 전달.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 원본 메서드를 대상 객체와 인수를 바탕으로 실행.
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료, resultTime = {}", resultTime);

        return result;
    }
}
