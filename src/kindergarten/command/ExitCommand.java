package kindergarten.command;

public class ExitCommand implements Command {
    private boolean exitRequested = false;

    @Override
    public void execute() {
        System.out.println("Выход из программы.");
        exitRequested = true;
    }

    @Override
    public CommandType getType() {
        return CommandType.EXIT;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }
}