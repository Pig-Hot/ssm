package design_mode.single;

public enum SingleEnum {
    INSTANCE;

    public SingleEnum getInstance() {
        return INSTANCE;
    }

}