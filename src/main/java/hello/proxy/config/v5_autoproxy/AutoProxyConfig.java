package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * Advisor를 Bean으로 등록.
     * 컨테이너가 빈을 생성하고 초기화할 때 AnnotationAwareAspectJAutoProxyCreator가 빈 후처리기로 작동하면서
     * '@Aspect'를 적용해야 할 클래스나 Advisor를 탐색.
     * 프록시 적용 조건(Pointcut)에 맞는 빈에 대해 프록시 객체를 생성하고, Advice(부가 기능)를 해당 프록시에 설정.
     */
    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        // OrderController의 noLog()은 대상에서 제외함.
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        //advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
