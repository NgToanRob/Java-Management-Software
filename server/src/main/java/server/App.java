package server;

import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import common.utility.Outputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.commands.*;
import server.utility.*;

/**
 * Main server class. Creates all server instances.
 *
 * @author Toan Nguyen.
 */
public class App {
    // TODO: Add a where query to delete + 1 more condition so that the user is checked during change commands (it can be changed in the database itself)
    private static final int MAX_CLIENTS = 1000;
    public static Logger logger = LoggerFactory.getLogger("ServerLogger");
    private static final String databaseUsername = "mrtoan";
    private static int port;
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    public static void main(String[] args) {
        if (!initialize(args)) return;
        DatabaseCommunication databaseCommunication = new DatabaseCommunication(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseCommunication);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseCommunication, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveByIdCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new RemoveLowerCommand(collectionManager, databaseCollectionManager),
                new HistoryCommand(),
                new AverageOfAnnualTurnoverCommand(collectionManager),
                new CountGreaterThanOfficialAddressCommand(collectionManager),
                new FilterGreaterThanTypeCommand(collectionManager),
                new ServerExitCommand(),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();
        databaseCommunication.closeConnection();
    }

    /**
     * It initializes the port, database host and database password
     *
     * @return The method returns a boolean value of initialization status.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 3) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/OrganizationDB";
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("To use: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("The port must be represented by a number!");
            App.logger.error("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("The port cannot be negative!");
            App.logger.error("The port cannot be negative!");
        }
        App.logger.error("Launch port initialization error!");
        return false;
    }
}
