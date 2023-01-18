package com.depcue.util;

public class StateUtil {
    public StateUtil() {
    }

    public static final String STATE_ACTIVE = "A";
    public static final String STATE_LOCKED = "B";
    public static final String STATE_CANCELED  = "C";

    public enum StateDocument {
        REGISTRADO,
        ANULADO,
        AUTORIZADO,
        DEVUELTO
    }


}
