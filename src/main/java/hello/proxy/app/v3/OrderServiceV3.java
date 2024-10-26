package hello.proxy.app.v3;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepositoryV3;

    public OrderServiceV3(OrderRepositoryV3 orderRepository) {
        this.orderRepositoryV3 = orderRepository;
    }


    public void orderItem(String itemId) {
        orderRepositoryV3.save(itemId);
    }
}
