package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserIsNotFoundException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.User;
import server.utility.database.DatabaseUserManager;
import server.utility.ResponseOutputer;

/**
 * Command 'login'. Allows the user to login.
 */
public class LoginCommand extends AbstractCommand {
    private final DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "", "internal command");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutputer.appendln("User " +
                    user.getUsername() + " authorized.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("This is an internal command!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appenderror("Wrong username or password!");
        }
        return false;
    }
}
