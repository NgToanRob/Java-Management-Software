package server.commands;

import common.data.Organization;
import common.exceptions.*;
import common.interaction.OrganizationRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.database.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'remove_greater'. Removes elements greater than user entered.
 */
public class RemoveLowerCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater", "{element}", "remove from the collection all elements lower than the given");
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
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;
            Organization organizationToFind = new Organization(
                    0L,
                    organizationRaw.getName(),
                    organizationRaw.getCoordinates(),
                    LocalDateTime.now(),
                    organizationRaw.getAnnualTurnover(),
                    organizationRaw.getOrganizationType(),
                    organizationRaw.getOfficialAddress(),
                    user
            );
            Organization organizationFromCollection = collectionManager.getByValue(organizationToFind);
            if (organizationFromCollection == null) throw new OrganizationNotFoundException();
            for (Organization organization : collectionManager.getLower(organizationFromCollection)) {
                if (!organization.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkOrganizationUserId(organization.getId(), user)) throw new ManualDatabaseEditException();
            }
            for (Organization marine : collectionManager.getLower(organizationFromCollection)) {
                databaseCollectionManager.deleteOrganizationById(marine.getId());
                collectionManager.removeFromCollection(marine);
            }
            ResponseOutputer.appendln("Organizations successfully removed!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("There is no organization with such characteristics in the collection!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Insufficient rights to execute this command!");
            ResponseOutputer.appendln("Objects owned by other users are read-only.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("A direct database change has occurred!");
            ResponseOutputer.appendln("Restart the client to avoid possible errors.");
        }
        return false;
    }
}
