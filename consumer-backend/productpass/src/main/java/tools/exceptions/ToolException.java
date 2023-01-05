package tools.exceptions;

public class ToolException extends RuntimeException  {
    public ToolException(Class tool, String errorMessage) {
        super("["+tool.getName()+"] " + errorMessage);
    }
    public ToolException(Class tool,Exception e, String errorMessage) {
        super("["+tool.getName()+"] " + errorMessage+", "+e.getMessage());
    }
}
