package tools;

public final class stringTools {
    private stringTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static Boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}

