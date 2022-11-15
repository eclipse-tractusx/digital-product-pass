package tools;

public final class numericTools {
    /**
     * Static Tools to parse numbers if is possible
     *
     */

    public static Integer parseInt(String value){
        try{
            return Integer.parseInt(value);
        }catch(Exception e){
            return null;
        }
    }
    public static Float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return null;
        }
    }
    public static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }
}
