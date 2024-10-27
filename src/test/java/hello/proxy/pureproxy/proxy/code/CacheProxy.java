package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 기존 객체의 operation() 메서드에 캐싱이라는 추가 작업 수행
 * -> 프록시 패턴.
 * 아예 새롱ㄴ
 */
@Slf4j
public class CacheProxy implements Subject {

    private final Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation(); // operation() : 1초 뒤 "Data" 반환
        }
        return cacheValue; // 캐시에 데이터가 있을 경우 target 호출 X
    }
}
