package common.interaction;

import common.data.Address;
import common.data.Coordinates;
import common.data.OrganizationType;

import java.io.Serializable;
import java.util.Objects;


/**
 * The OrganizationPack class is a class that contains all the information about the organization
 */
public class OrganizationRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private long annualTurnover;
    private OrganizationType organizationType;
    private Address officialAddress;

    public OrganizationRaw(String name, Coordinates coordinates, long annualTurnover,
                           OrganizationType organizationType, Address officialAddress) {
        this.name = name;
        this.coordinates = coordinates;
        this.annualTurnover = annualTurnover;
        this.organizationType = organizationType;
        this.officialAddress = officialAddress;
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
     * Returns the annual turnover of the organization
     *
     * @return The annual turnover of the organization.
     */
    public Long getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Returns the organizationType of the organization
     *
     * @return The organizationType of the organization.
     */
    public OrganizationType getType() {
        return organizationType;
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



    @Override
    public String toString() {
        return "Organization{\n" +
                ", \n\tname='" + name + '\'' +
                ", \n\tcoordinates=" + coordinates +
                ", \n\tannualTurnover=" + annualTurnover +
                ", \n\torganizationType=" + organizationType +
                ", \n\tofficialAddress=" + officialAddress + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationRaw)) return false;

        OrganizationRaw that = (OrganizationRaw) o;

        if (getAnnualTurnover() != that.getAnnualTurnover()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getCoordinates() != null ? !getCoordinates().equals(that.getCoordinates()) : that.getCoordinates() != null)
            return false;
        if (getOrganizationType() != that.getOrganizationType()) return false;
        return getOfficialAddress() != null ? getOfficialAddress().equals(that.getOfficialAddress()) : that.getOfficialAddress() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),
                getCoordinates(),
                getAnnualTurnover(),
                getOrganizationType(),
                getOfficialAddress());
    }
}
