package common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * * The class Address represents an address.
 */
public class Address implements Serializable {
    private String street; // String cannot be empty, Field can be null
    private String zipCode; // Field can be null

    public Address(String street, String zipCode) {
        this.street = street;
        this.zipCode = zipCode;
    }

    /**
     * Get the street name of the address
     *
     * @return The street property of the address object.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Get the zipcode of the address
     *
     * @return The zip code.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * The method returns a string representation of the organization
     *
     * @return The street and zipCode of the organization.
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    /**
     * The  method returns a hash code value for the object
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
        return result;
    }


    /**
     * If the object is the same as the one we're comparing to, return true. If the object is not an instance of the class
     * we're comparing to, return false. If the object is an instance of the class we're comparing to, compare the street
     * and zip code of the two objects
     *
     * @param o The object to compare against.
     * @return The hashcode of the object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (getStreet() != null ? !getStreet().equals(address.getStreet()) : address.getStreet() != null) return false;
        return getZipCode() != null ? getZipCode().equals(address.getZipCode()) : address.getZipCode() == null;
    }

    /**
     * If the street name is longer than the standard, return 1. If the street name is shorter than the standard, return
     * -1. If the street name is the same length as the standard, return 0
     *
     * @param address The address to compare to.
     * @return The length of the street name.
     */
    public int compareTo(Address address) {
        int standard = address.getStreet().length();
        return Integer.compare(street.length(), standard);
    }

}
