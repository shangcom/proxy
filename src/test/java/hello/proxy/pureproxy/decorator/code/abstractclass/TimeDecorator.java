package hello.proxy.pureproxy.decorator.code.abstractclass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator extends AbstractDecorator implements Component {

    public TimeDecorator(Component component) {
        super(component);
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        String result = component.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime = {}ms", resultTime);

        return result;
    }
}