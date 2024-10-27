package hello.proxy.pureproxy.decorator.code.abstractclass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator extends AbstractDecorator implements Component {

    public MessageDecorator(Component component) {
        super(component);
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        // data -> ****data****
        String result = component.operation();
        String decoResult = "****" + result + "****";
        log.info("MessageDecorator 꾸미기 적용 전 = {}, 후 = {}", result, decoResult);
        return decoResult;
    }
}
