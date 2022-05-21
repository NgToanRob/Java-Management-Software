package common.data;


/**
  * An enum class with a list of organization types.
  */
public enum OrganizationType {
    PUBLIC,
    GOVERNMENT,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    /**
     * Generates a beautiful list of enum string values.
     *
     * @return String with all enum values splitted by comma.
     */
    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (OrganizationType organizationType : values()) {
            nameList.append(organizationType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }



}
