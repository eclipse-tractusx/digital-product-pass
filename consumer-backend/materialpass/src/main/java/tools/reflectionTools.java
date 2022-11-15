package tools;

public class reflectionTools {
    public static String getCurrentClassName(Class classObj){
        return classObj.getSimpleName();
    }
    public static Boolean classIsTest(Class classObj){
        return reflectionTools.getCurrentClassName(classObj).contains("test");
    }
}
