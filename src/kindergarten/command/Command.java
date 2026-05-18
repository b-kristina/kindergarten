package kindergarten.command;

public interface Command {
    void execute();
    CommandType getType();
}