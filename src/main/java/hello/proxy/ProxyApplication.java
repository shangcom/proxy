package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/*
 config 바꿔가면서 할건데, 스프링 부트는 기본이 전체 스캔함.
 그래서 app 패키지로 스캔 범위 한정하고, config는 스캔 못하게 함.
 (@Configuration은 스캔 대상이라서, config 폴더에 들어있는 모든 파일 스캔됨.)
 대신 수동으로 설정 클래스 지정하기 위해 Import(xxx.class) 사용.
 */
@Import({AppV1Config.class, AppV2Config.class}) // 설정 클래스 지정. {}로 여러개 가능.
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
