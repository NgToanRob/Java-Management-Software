package common.data;

import common.interaction.User;

import javax.xml.crypto.URIDereferencer;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An organization is a organizationType of entity that has a name, coordinates, creation date, annual turnover and
 * a organizationType.
 */
public class Organization implements Comparable<Organization> {

    public static final int MIN_X = -396;
    public static int MAX_Y = 969;
    public static double MIN_ANNUAL_TURNOVER = 0;

    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private long annualTurnover;
    private OrganizationType organizationType;
    private Address officialAddress;
    private User owner;

    public Organization(long id, String name, Coordinates coordinates, LocalDateTime creationDate, long annualTurnover,
                        OrganizationType organizationType, Address officialAddress, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.organizationType = organizationType;
        this.officialAddress = officialAddress;
        this.owner = owner;
    }

    public void setId(long id){
        this.id = id;
    }

    /**
     * Get the ID of the organization
     *
     * @return The id of the organization.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the organization
     *
     * @return The name of the organization.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the coordinates of the organization
     *
     * @return The coordinates of the organization.
     */

    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Get the creation date of the organization
     *
     * @return The creation date of the organization.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the annual turnover of the organization
     *
     * @return The annual turnover of the organization.
     */
    public Long getAnnualTurnover() {
        return annualTurnover;
    }


    /**
     *  Returns the organization type of the organization
     *
     * @return The organization type of the organization.
     */
    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    /**
     * Returns the official address of the organization
     *
     * @return The official address of the organization.
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    /**
     * This function returns the owner of the current object.
     *
     * @return The owner of the pet.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * The method compares the annual turnover of the current organization with the annual turnover of the
     * organization passed as a parameter
     *
     * @param organizationToCompare The Organization object to compare to.
     * @return The difference between the annualTurnover of the current object and the annualTurnover of the object being
     * compared to.
     */
    @Override
    public int compareTo(Organization organizationToCompare) {
        return ((Long) annualTurnover).compareTo(organizationToCompare.getAnnualTurnover());
    }


//    @Override
//    public String toString() {
//        String info = "";
//        info += "Organization ID: " + id;
//        info += " (added " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
//        info += "\n Name: " + name;
//        info += "\n Coordinates: " + coordinates;
//        info += "\n Annual turnover: " + annualTurnover;
//        info += "\n Organization Type: " + organizationType;
//        info += "\n Official address: " + officialAddress;
//        return info;
//    }


    @Override
    public String toString() {
        return "Organization{\n" +
                "\tid=" + id +
                ", \n\tname='" + name + '\'' +
                ", \n\tcoordinates=" + coordinates +
                ", \n\tcreationDate=" + creationDate +
                ", \n\tannualTurnover=" + annualTurnover +
                ", \n\torganizationType=" + organizationType +
                ", \n\tofficialAddress=" + officialAddress +
                '}';
    }

    @Override
    public int hashCode() {
        return name.hashCode() + creationDate.hashCode() + coordinates.hashCode() + ((Long) annualTurnover).hashCode()
                + organizationType.hashCode() + officialAddress.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;

        Organization that = (Organization) o;

        if (getId() != that.getId()) return false;
        if (getAnnualTurnover() != that.getAnnualTurnover()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getCoordinates() != null ? !getCoordinates().equals(that.getCoordinates()) : that.getCoordinates() != null)
            return false;
        if (getCreationDate() != null ? !getCreationDate().equals(that.getCreationDate()) : that.getCreationDate() != null)
            return false;
        if (getOrganizationType() != that.getOrganizationType()) return false;
        if (getOfficialAddress() != null ? !getOfficialAddress().equals(that.getOfficialAddress()) : that.getOfficialAddress() != null)
            return false;
        return getOwner() != null ? getOwner().equals(that.getOwner()) : that.getOwner() == null;
    }
}
