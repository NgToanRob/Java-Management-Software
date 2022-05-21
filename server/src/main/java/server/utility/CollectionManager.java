package server.utility;

import common.data.Address;
import common.data.Organization;
import common.data.OrganizationType;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.utility.Outputer;
import server.App;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private ArrayList<Organization> organizationCollection;
    private LocalDateTime lastInitTime;
    private final DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        try {
            organizationCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Outputer.println("The collection is loaded.");
            App.logger.info("The collection is loaded.");
        } catch (DatabaseHandlingException exception) {
            organizationCollection = new ArrayList<>();
            Outputer.printerror("The collection could not be loaded!");
            App.logger.error("The collection could not be loaded!");
        }
    }

    /**
     * Get the collection of organizations
     *
     * @return An ArrayList of Organization objects.
     */
    public ArrayList<Organization> getCollection() {
        return organizationCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return organizationCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return organizationCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public Organization getFirst() {
        return organizationCollection.stream().findFirst().orElse(null);
    }

    /**
     * @param id ID of the organization.
     * @return An organization by his ID or null if organization isn't found.
     */
    public Organization getById(Long id) {
        return organizationCollection.stream()
                .filter(organization ->
                        ((Long)organization.getId()).equals(id)).findFirst().orElse(null);
    }

    /**
     * @param organizationToFind An organization whose value will be found.
     * @return An organization by his value or null if organization isn't found.
     */
    public Organization getByValue(Organization organizationToFind) {
        return organizationCollection.stream()
                .filter(organization -> organization.equals(organizationToFind)).findFirst().orElse(null);
    }

    /**
     * @return Sum of all organizations' health or 0 if collection is empty.
     */
    public double getAverageOfAnnualTurnover() throws CollectionIsEmptyException {
        int totalOrganizations = collectionSize();
        if (totalOrganizations == 0) throw new CollectionIsEmptyException();
        return organizationCollection.stream().mapToDouble(Organization::getAnnualTurnover).average()
                .getAsDouble();
    }

    /**
     * @return Collection content or corresponding string if collection is empty.
     */
    public String showCollection() {
        if (organizationCollection.isEmpty()) return "The collection is empty!";
        return organizationCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * @return Marine, who has max melee weapon.
     * @throws CollectionIsEmptyException If collection is empty.
     */
//    public String maxByMeleeWeapon() throws CollectionIsEmptyException {
//        if (organizationCollection.isEmpty()) throw new CollectionIsEmptyException();
//
//        MeleeWeapon maxMeleeWeapon = organizationCollection.stream().map(Organization::getMeleeWeapon)
//                .max(Enum::compareTo).get();
//        return organizationCollection.stream()
//                .filter(organization -> organization.getMeleeWeapon().equals(maxMeleeWeapon)).findFirst().get().toString();
//    }

    /**
     * @param organizationToFilter Organization to filter by.
     * @return Information about valid organizations or empty string, if there's no such organizations.
     */
    public String organizationTypeFilteredInfo(OrganizationType organizationToFilter) {
        return organizationCollection.stream()
                .filter(organization -> organization.getOrganizationType().compareTo(organizationToFilter) > 0)
                .reduce(
                        "",
                        (sum, m) -> sum += m + "\n\n",
                        (sum1, sum2) -> sum1 + sum2)
                .trim();
    }

    /**
     * Remove organizations greater than the selected one.
     *
     * @param organizationToCompare A organization to compare with.
     * @return Greater organizations list.
     */
    public ArrayList<Organization> getLower(Organization organizationToCompare) {
        return organizationCollection.stream()
                .filter(organization -> organization.compareTo(organizationToCompare) < 0)
                .collect(
                ArrayList::new,
                ArrayList::add,
                ArrayList::addAll
                );
    }

    /**
     * Adds a new organization to collection.
     *
     * @param organization An organization to add.
     */
    public void addToCollection(Organization organization) {
        organizationCollection.add(organization);
    }

    /**
     * Removes a new organization to collection.
     *
     * @param organization An organization to remove.
     */
    public void removeFromCollection(Organization organization) {
        organizationCollection.remove(organization);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        organizationCollection.clear();
    }

    public int getCountGreaterThanOfficialAddress(Address address) throws CollectionIsEmptyException {
        if (organizationCollection.isEmpty())
            throw new CollectionIsEmptyException();
        int count = 0;
        for (Organization organization : organizationCollection) {
            if (organization.getOfficialAddress().getStreet() == null)
                continue;
            if (organization.getOfficialAddress().compareTo(address) > 0) {
                count += 1;
            }
        }
        return count;
    }
}
