package kindergarten.command;

public class ExitCommand implements Command {
    private boolean exitRequested = false;

    @Override
    public void execute() {
        System.out.println("Выход из программы.");
        exitRequested = true;
    }

    @Override
    public String getDescription() {
        return "Выход";
    }

    public boolean isExitRequested() {
        return exitRequested;
    }
}