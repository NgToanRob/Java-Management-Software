package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.OrganizationRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.database.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "add a new element to the collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertOrganization(organizationRaw, user));
            ResponseOutputer.appendln("Organization added successfully!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        }
        return false;
    }
}
