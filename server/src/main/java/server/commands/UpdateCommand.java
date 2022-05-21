package server.commands;

import common.data.*;
import common.exceptions.*;
import common.interaction.OrganizationRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update <ID> ", "","update the value of the collection element whose id is equal to the given one");
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
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            long id = Long.parseLong(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            Organization oldOrganization = collectionManager.getById(id);
            if (oldOrganization == null) throw new OrganizationNotFoundException();
            if (!oldOrganization.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkOrganizationUserId(oldOrganization.getId(), user)) throw new ManualDatabaseEditException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;

            databaseCollectionManager.updateOrganizationById(id, organizationRaw);

            String name = organizationRaw.getName() == null ? oldOrganization.getName() : organizationRaw.getName();
            Coordinates coordinates = organizationRaw.getCoordinates() == null ? oldOrganization.getCoordinates() : organizationRaw.getCoordinates();
            LocalDateTime creationDate = oldOrganization.getCreationDate();
            long annualTurnover = organizationRaw.getAnnualTurnover() == -1 ? oldOrganization.getAnnualTurnover() : organizationRaw.getAnnualTurnover();
            OrganizationType organizationType = organizationRaw.getOrganizationType() == null ? oldOrganization.getOrganizationType() : organizationRaw.getOrganizationType();
            Address officialAddress = organizationRaw.getOfficialAddress() == null ? oldOrganization.getOfficialAddress() : organizationRaw.getOfficialAddress();

            collectionManager.removeFromCollection(oldOrganization);
            collectionManager.addToCollection(new Organization(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    annualTurnover,
                    organizationType,
                    officialAddress,
                    user
            ));
            ResponseOutputer.appendln("Organization successfully changed!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID must be represented as a positive number!");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("There is no soldier with this ID in the collection!");
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
