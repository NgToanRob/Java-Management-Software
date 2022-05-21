package server.commands;

import common.data.Address;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.util.Collections;

public class CountGreaterThanOfficialAddressCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public CountGreaterThanOfficialAddressCommand(CollectionManager collectionManager) {
        super("count_greater_than_official_address",
                "{element}",
                "print the number of elements whose officialAddress field value is greater than the specified one");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null)
                throw new WrongAmountOfElementsException();
            Address addressPack = (Address) objectArgument;

            ResponseOutputer.appendln("Number of elements whose officialAddress field value is greater than the given one: "
                    + collectionManager.getCountGreaterThanOfficialAddress(addressPack));
            return true; // create Address from street and zipcode

        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + "'");

        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("The collection is empty!");
        }
        return true;
    }
}
