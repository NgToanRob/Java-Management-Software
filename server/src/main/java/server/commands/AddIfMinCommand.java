package server.commands;

import common.data.Organization;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.OrganizationRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.database.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'add_if_min'. Adds a new element to collection if it's less than the minimal one.
 */
public class AddIfMinCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add_if_min","{element}" ,"update the value of the collection element whose id is equal to the given one");
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
            Organization organizationToAdd = databaseCollectionManager.insertOrganization(organizationRaw, user);
            if (collectionManager.collectionSize() == 0 || organizationToAdd.compareTo(collectionManager.getFirst()) < 0) {
                collectionManager.addToCollection(organizationToAdd);
                ResponseOutputer.appendln("Organization added successfully!");
                return true;
            } else ResponseOutputer.appenderror("he value of an Organization is greater than the value of the smallest of the soldiers!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        }
        return false;
    }
}
