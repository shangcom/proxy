package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClitent {

    private final ConcreteLogic concreteLogic;

    public ConcreteClitent(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute() {
        concreteLogic.operation();
    }
}
