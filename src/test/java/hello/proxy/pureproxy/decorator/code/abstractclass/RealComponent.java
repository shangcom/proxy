package hello.proxy.pureproxy.decorator.code.abstractclass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component {

    @Override
    public String operation() {
        log.info("RealCompoent 실행");
        return "data";
    }
}
