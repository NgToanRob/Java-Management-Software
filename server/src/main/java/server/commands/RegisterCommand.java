package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExists;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.User;
import server.utility.database.DatabaseUserManager;
import server.utility.ResponseOutputer;

/**
 * Command 'register'. Allows the user to register.
 */
public class RegisterCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("Register", "", "internal command");
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
            if (databaseUserManager.insertUser(user)) ResponseOutputer.appendln("User " +
                    user.getUsername() + " registered.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("This is an internal command!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.appenderror("User " + user.getUsername() + "already exists!");
        }
        return false;
    }
}
