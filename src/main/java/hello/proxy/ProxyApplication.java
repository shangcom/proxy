package hello.proxy;

import hello.proxy.config.v6_aop.AopConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/*
 config 바꿔가면서 할건데, 스프링 부트는 기본이 전체 스캔함.
 그래서 app 패키지로 스캔 범위 한정하고, config는 스캔 못하게 함.
 (@Configuration은 스캔 대상이라서, config 폴더에 들어있는 모든 파일 스캔됨.)
 대신 수동으로 설정 클래스 지정하기 위해 Import(xxx.class) 사용.
 */
//@Import({AppV1Config.class, AppV2Config.class}) // 설정 클래스 지정. {...}로 여러개 가능.
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicProxyFilterConfig.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(BeanPostProcessorConfig.class)
//@Import(AutoProxyConfig.class)
@Import(AopConfig.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
public class ProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
