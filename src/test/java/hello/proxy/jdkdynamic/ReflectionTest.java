package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    // 만약 여기에 정적 메서드(static)이 있었다면, 구현체 없어도 리플렉션 가능함)
    @Slf4j
    static class Hello {
        // 인스턴스 메서드.
        public String callA() {
            log.info("callA");
            return "A";
        }

        // 인스턴스 메서드.
        public String callB() {
            log.info("callB");
            return "B";
        }
    }

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("result = {}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB();
        log.info("result = {}", result2);
        // 공통 로직2 종료
    }

    /*
    리플렉션을 사용해 동적으로 메서드를 호출하고 공통 로직을 적용하는 방식
     */
    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Hello target = new Hello();

        // 클래스 정보 (상단의 static class)
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Method methodCallA = classHello.getMethod("callA"); // 매개변수 : 원본 클래스에 정의된 메서드 이름.
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Test
    void reflection2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Hello target = new Hello();

        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Test
    void reflection3() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Hello target = new Hello();

        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }


    private static void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("Start");
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }


}
