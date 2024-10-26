package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 스프링은 @Controller 또는 @RequestMapping이 있어야 스프링 컨트롤러로 인식.
@RequestMapping
@ResponseBody
public interface OrderControllerV1 {

    /*
     컨트롤러를 인터페이스로 작성 시, 자동 파라미터 바인딩이 되지 않는 경우가 있어 명시적으로
     @RequestParam 사용함.
     */
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
