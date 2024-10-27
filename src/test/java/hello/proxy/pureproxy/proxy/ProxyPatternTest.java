package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProxyPatternTest {

    /*
     캐시 기능이 없는 경우.
     같은 대상에 대한 조회가 각각 1초씩 3초 걸림.
     한번 조회했던 데이터는 캐시로 보관하여 바로 반환 필요성.
     */
    @Test
    void noProxyTest() {
        long start = System.currentTimeMillis();
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        client.execute();
        client.execute();
        client.execute();
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("time = {}", time);
    }

    @Test
    void cacheProxyTest() {
        long start = System.currentTimeMillis();
        RealSubject target = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(target);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);

        client.execute();
        client.execute();
        client.execute();
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("time = {}", time);
    }
}
