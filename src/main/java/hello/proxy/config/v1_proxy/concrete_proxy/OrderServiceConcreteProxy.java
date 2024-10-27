package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;


public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    /*
    OrderServiceConcreteProxy는 OrderServiceV2를 상속함.
    따라서 OrderServiceConcreteProxy는 OrderServiceV2에 OrderRepositoryV2를 주입 받는 필드가 존재
    그러나 OrderServiceConcreteProxy는 OrderRepositoryV2를 직접 사용하지 않는다.
    대신, 주입받은 target 객체(원본 객체)의 OrderRepositoryV2를 사용하여 비즈니스 로직을 수행한다.
    따라서, 상위 클래스의 생성자를 호출할 때 OrderRepositoryV2를 null로 전달해도 문제가 없으며,
    이렇게 함으로써 프록시에서 불필요한 의존성을 제거할 수 있다.
     */
    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {

        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderServiceV2.orderItem()");
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}
