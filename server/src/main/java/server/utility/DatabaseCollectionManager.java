package server.utility;

import common.data.*;
import common.exceptions.DatabaseHandlingException;
import common.interaction.OrganizationRaw;
import common.interaction.User;
import common.utility.Outputer;
import server.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Operates the database collection itself.
 */
public class DatabaseCollectionManager {
    // ORGANIZATION_TABLE
    private final String SELECT_ALL_ORGANIZATIONS = "SELECT * FROM " + DatabaseCommunication.ORGANIZATION_TABLE;
    private final String SELECT_ORGANIZATION_BY_ID = SELECT_ALL_ORGANIZATIONS + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ORGANIZATION_BY_ID_AND_USER_ID = SELECT_ORGANIZATION_BY_ID + " AND " +
            DatabaseCommunication.ORGANIZATION_TABLE_ACCOUNT_ID_COLUMN + " = ?";
    private final String INSERT_ORGANIZATION = "INSERT INTO " +
            DatabaseCommunication.ORGANIZATION_TABLE + " (" +
            DatabaseCommunication.ORGANIZATION_TABLE_NAME_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_COORDINATES_ID_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_ANNUAL_TURNOVER_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_ORGANIZATION_TYPE_ID_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_ADDRESS_ID_COLUMN + ", " +
            DatabaseCommunication.ORGANIZATION_TABLE_ACCOUNT_ID_COLUMN + ")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_ORGANIZATION_BY_ID = "DELETE FROM " + DatabaseCommunication.ORGANIZATION_TABLE +
            " WHERE " + DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ORGANIZATION_NAME_BY_ID = "UPDATE " + DatabaseCommunication.ORGANIZATION_TABLE + " SET " +
            DatabaseCommunication.ORGANIZATION_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ORGANIZATION_COORDINATES_ID_BY_ID = "UPDATE " + DatabaseCommunication.ORGANIZATION_TABLE + " SET " +
            DatabaseCommunication.ORGANIZATION_TABLE_COORDINATES_ID_COLUMN + " = ?::coordinates" + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ORGANIZATION_ANNUAL_TURNOVER_BY_ID = "UPDATE " + DatabaseCommunication.ORGANIZATION_TABLE + " SET " +
            DatabaseCommunication.ORGANIZATION_TABLE_ANNUAL_TURNOVER_COLUMN + " = ?" + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ORGANIZATION_TYPE_BY_ID = "UPDATE " + DatabaseCommunication.ORGANIZATION_TABLE + " SET " +
            DatabaseCommunication.ORGANIZATION_TABLE_ORGANIZATION_TYPE_ID_COLUMN + " = ?::organizationType" + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ORGANIZATION_ADDRESS_ID_BY_ID = "UPDATE " + DatabaseCommunication.ORGANIZATION_TABLE + " SET " +
            DatabaseCommunication.ORGANIZATION_TABLE_ADDRESS_ID_COLUMN + " = ?::officialAddress" + " WHERE " +
            DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN + " = ?";
    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseCommunication.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_COORDINATES_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseCommunication.COORDINATES_TABLE_ID_COLUMN+ " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseCommunication.COORDINATES_TABLE + " (" +
            DatabaseCommunication.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseCommunication.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseCommunication.COORDINATES_TABLE +
            " WHERE " + DatabaseCommunication.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATES_BY_ORGANIZATION_ID = "UPDATE " +
            DatabaseCommunication.COORDINATES_TABLE + " SET " +
            DatabaseCommunication.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseCommunication.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseCommunication.COORDINATES_TABLE_ID_COLUMN + " = ?";
    // Official address table
    private final String SELECT_ALL_ADDRESS = "SELECT * FROM " + DatabaseCommunication.OFFICIAL_ADDRESS_TABLE;
    private final String SELECT_ADDRESS_BY_ADDRESS_ID = SELECT_ALL_ADDRESS +
            " WHERE " + DatabaseCommunication.ADDRESS_TABLE_ID_COLUMN+ " = ?";
    private final String INSERT_ADDRESS = "INSERT INTO " +
            DatabaseCommunication.OFFICIAL_ADDRESS_TABLE + " (" +
            DatabaseCommunication.ADDRESS_TABLE_STREET_COLUMN+ ", " +
            DatabaseCommunication.ADDRESS_TABLE_ZIPCODE_COLUMN + ") VALUES (?, ?)";
    private final String DELETE_ADDRESS_BY_ID = "DELETE FROM " + DatabaseCommunication.OFFICIAL_ADDRESS_TABLE +
            " WHERE " + DatabaseCommunication.ADDRESS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_ADDRESS_BY_ADDRESS_ID = "UPDATE " +
            DatabaseCommunication.OFFICIAL_ADDRESS_TABLE + " SET " +
            DatabaseCommunication.ADDRESS_TABLE_STREET_COLUMN + " = ?, " +
            DatabaseCommunication.ADDRESS_TABLE_ZIPCODE_COLUMN + " = ?" + " WHERE " +
            DatabaseCommunication.ADDRESS_TABLE_ID_COLUMN + " = ?";

    // Organization type table
    private final String SELECT_ALL_ID = "SELECT organizationTypeID FROM " + DatabaseCommunication.ORGANIZATION_TYPE_TABLE;
    private final String SELECT_ID_BY_TYPE = SELECT_ALL_ID + " WHERE type = ? ";
    private final String SELECT_TYPE_BY_ID = "SELECT * FROM " + DatabaseCommunication.ORGANIZATION_TYPE_TABLE +
            " WHERE organizationTypeId = ? ";

    private DatabaseCommunication databaseCommunication;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseCommunication databaseCommunication, DatabaseUserManager databaseUserManager) {
        this.databaseCommunication = databaseCommunication;
        this.databaseUserManager = databaseUserManager;
    }

    // *** Get data from database ***

    /**
     * @return List of Organizations.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public ArrayList<Organization> getCollection() throws DatabaseHandlingException {
        ArrayList<Organization> organizationList = new ArrayList<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseCommunication.getPreparedStatement(SELECT_ALL_ORGANIZATIONS, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                organizationList.add(getOrganization(resultSet));
            }
//            System.out.println(organizationList);
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectAllStatement);
        }
        return organizationList;
    }

    /**
     * Create Organization.
     *
     * @param resultSet Result set parameters of Organization.
     * @return New Organization.
     * @throws SQLException When there's exception inside.
     */
    private Organization getOrganization(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(DatabaseCommunication.ORGANIZATION_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseCommunication.ORGANIZATION_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByCoordinatesId(resultSet.getInt(DatabaseCommunication.ORGANIZATION_TABLE_COORDINATES_ID_COLUMN));
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseCommunication.ORGANIZATION_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        long annualTurnover = resultSet.getLong(DatabaseCommunication.ORGANIZATION_TABLE_ANNUAL_TURNOVER_COLUMN);
        OrganizationType organizationType = getTypeByTypeId(resultSet.getInt(DatabaseCommunication.ORGANIZATION_TABLE_ORGANIZATION_TYPE_ID_COLUMN));
        Address address = getAddressByAddressId(resultSet.getLong(DatabaseCommunication.ORGANIZATION_TABLE_ADDRESS_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseCommunication.ORGANIZATION_TABLE_ACCOUNT_ID_COLUMN));
        return new Organization(
                id,
                name,
                coordinates,
                creationDate,
                annualTurnover,
                organizationType,
                address,
                owner
        );

    }

    /**
     * @param organizationId Id of Organization.
     * @return coordinates.
     * @throws SQLException When there's exception inside.
     */
    private Coordinates getCoordinatesByCoordinatesId(long organizationId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByOrganizationIdStatement = null;
        try {
            preparedSelectCoordinatesByOrganizationIdStatement =
                    databaseCommunication.getPreparedStatement(SELECT_COORDINATES_BY_COORDINATES_ID, false);
            preparedSelectCoordinatesByOrganizationIdStatement.setLong(1, organizationId);
            ResultSet resultSet = preparedSelectCoordinatesByOrganizationIdStatement.executeQuery();
            App.logger.info("SELECT_COORDINATES_BY_COORDINATES_ID query completed.");
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseCommunication.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseCommunication.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the SELECT_COORDINATES_BY_ORGANIZATION_ID query!");
            throw new SQLException(exception);
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectCoordinatesByOrganizationIdStatement);
        }
        return coordinates;
    }

    /**
     * @param addressId ID of Address.
     * @return Address.
     * @throws SQLException When there's exception inside.
     */
    private Address getAddressByAddressId(long addressId) throws SQLException {
        Address address;
        PreparedStatement preparedSelectAddressByAddressIdStatement = null;
        try {
            preparedSelectAddressByAddressIdStatement =
                    databaseCommunication.getPreparedStatement(SELECT_ADDRESS_BY_ADDRESS_ID, false);
            preparedSelectAddressByAddressIdStatement.setLong(1, addressId);
            ResultSet resultSet = preparedSelectAddressByAddressIdStatement.executeQuery();
            App.logger.info("SELECT_ADDRESS_BY_ADDRESS_ID query completed.");
            if (resultSet.next()) {
                address = new Address(
                        resultSet.getString(DatabaseCommunication.ADDRESS_TABLE_STREET_COLUMN),
                        resultSet.getString(DatabaseCommunication.ADDRESS_TABLE_ZIPCODE_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the SELECT_ADDRESS_BY_ADDRESS_ID query!");
            throw new SQLException(exception);
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectAddressByAddressIdStatement);
        }
        return address;
    }

    private OrganizationType getTypeByTypeId(int typeId) {
        OrganizationType organizationType = null;
        PreparedStatement preparedSelectTypeByTypeIdStatement = null;
        try {
            preparedSelectTypeByTypeIdStatement =
                    databaseCommunication.getPreparedStatement(SELECT_TYPE_BY_ID, false);
            preparedSelectTypeByTypeIdStatement.setInt(1, typeId);
            ResultSet resultSet = preparedSelectTypeByTypeIdStatement.executeQuery();
            App.logger.info("SELECT_TYPE_BY_ID query completed.");
            if (resultSet.next()) {
                organizationType = OrganizationType.valueOf(resultSet.getString(DatabaseCommunication.ORGANIZATION_TYPE_TABLE_TYPE_COLUMN));
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the SELECT_TYPE_BY_ID query!");
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectTypeByTypeIdStatement);
        }
        return organizationType;
    }

    // *** Insert data to database
    /**
     * @param organizationRaw Organization raw.
     * @param user User.
     * @return Organization.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public Organization insertOrganization(OrganizationRaw organizationRaw, User user) throws DatabaseHandlingException {
        /*
          TODO: lấy data raw từ client request, xử lý và thêm vào table organization:
           - lấy ID từ hàm getGeneratedKeys() của PreparedStatement
           - phải thêm vào các bảng phụ trước sau đó lấy id để thêm vào bảng chính
           - Có hai cách thêm enum:
                +   Đồng bộ thứ tự enum trong class và bảng ghi
                    Lấy index của element trong enum điền vào bảng luôn
                    =))) có vẻ không hợp lý khi số lượng enum nhiều và khó khăn trong việc không thể thêm mới đc element
                    trong bảng. Hơi chuối nhưng khá nhanh với lượng enum ít
                +   Lấy String từ data raw sau và search trong bảng để lấy index, rồi thêm vào bảng chính
           - (optional) check tồn tại của Address và Coordinates:
                + Nếu tồn tại thì lấy id của cái cũ
                + Nếu không thì tạo mới
         */

        Organization organization;
        PreparedStatement preparedInsertOrganizationStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertAddressStatement = null;
        try {
            databaseCommunication.setCommitMode();
            databaseCommunication.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertOrganizationStatement = databaseCommunication.getPreparedStatement(INSERT_ORGANIZATION, true);
            preparedInsertCoordinatesStatement = databaseCommunication.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertAddressStatement = databaseCommunication.getPreparedStatement(INSERT_ADDRESS, true);
            // Step 1: Insert address
            preparedInsertAddressStatement.setString(1, organizationRaw.getOfficialAddress().getStreet());
            preparedInsertAddressStatement.setString(2, organizationRaw.getOfficialAddress().getZipCode());

            if (preparedInsertAddressStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedAddressKeys = preparedInsertAddressStatement.getGeneratedKeys();
            int addressId; // here
            if (generatedAddressKeys.next()) {
                addressId = generatedAddressKeys.getInt(1);
            } else throw new SQLException();
            App.logger.info("INSERT_ADDRESS query completed.");

            // Step 2: Insert coordinates
            preparedInsertCoordinatesStatement.setInt(1, organizationRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setDouble(2, organizationRaw.getCoordinates().getY());

            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedCoordinatesKeys = preparedInsertCoordinatesStatement.getGeneratedKeys();
            int coordinatesId; // here
            if (generatedCoordinatesKeys.next()) {
                coordinatesId = generatedCoordinatesKeys.getInt(1);
            } else throw new SQLException();
            App.logger.info("INSERT_COORDINATES query completed.");

            // Step 3: Get ID of organization type
            OrganizationType organizationTypeRaw = organizationRaw.getOrganizationType();
            int organizationTypeId = getOrganizationTypeIdByType(organizationTypeRaw.toString());

            // Step: 4 Insert organization to database
            preparedInsertOrganizationStatement.setString(1, organizationRaw.getName());
            preparedInsertOrganizationStatement.setInt(2, coordinatesId);
            preparedInsertOrganizationStatement.setTimestamp(3, Timestamp.valueOf(creationTime));
            preparedInsertOrganizationStatement.setLong(4, organizationRaw.getAnnualTurnover());
            preparedInsertOrganizationStatement.setInt(5, organizationTypeId);
            preparedInsertOrganizationStatement.setInt(6, addressId);
            preparedInsertOrganizationStatement.setLong(7, databaseUserManager.getUserIdByUsername(user));
            System.out.println(preparedInsertOrganizationStatement.toString());

            if (preparedInsertOrganizationStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedOrganizationKeys = preparedInsertOrganizationStatement.getGeneratedKeys();
            long organizationId;
            if (generatedOrganizationKeys.next()) {
                organizationId = generatedOrganizationKeys.getLong(1);
            } else throw new SQLException();
            App.logger.info("INSERT_ORGANIZATION query completed.");

            // create new to add collection
            organization = new Organization(
                    organizationId,
                    organizationRaw.getName(),
                    organizationRaw.getCoordinates(),
                    creationTime,
                    organizationRaw.getAnnualTurnover(),
                    organizationRaw.getOrganizationType(),
                    organizationRaw.getOfficialAddress(),
                    user
            );

            databaseCommunication.commit();
            return organization;
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing a group of requests to add a new object!");
            databaseCommunication.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseCommunication.closePreparedStatement(preparedInsertOrganizationStatement);
            databaseCommunication.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseCommunication.closePreparedStatement(preparedInsertAddressStatement);
            databaseCommunication.setNormalMode();
        }
    }

    private int getOrganizationTypeIdByType(String type) throws SQLException {
        int typeId;
        PreparedStatement preparedSelectOrganizationTypeIdByTypeStatement = null;
        try {
            preparedSelectOrganizationTypeIdByTypeStatement =
                    databaseCommunication.getPreparedStatement(SELECT_ID_BY_TYPE, false);
            preparedSelectOrganizationTypeIdByTypeStatement.setString(1, type);
            ResultSet resultSet = preparedSelectOrganizationTypeIdByTypeStatement.executeQuery();
            App.logger.info("SELECT_ID_BY_TYPE query completed.");
            if (resultSet.next()) {
                typeId = resultSet.getInt(DatabaseCommunication.ORGANIZATION_TYPE_TABLE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the SELECT_ID_BY_TYPE query!");
            throw new SQLException(exception);
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectOrganizationTypeIdByTypeStatement);
        }
        return typeId;
    }

    // *** Update data ***
    /**
     * @param organizationRaw Organization raw.
     * @param organizationId  ID of Organization.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void updateOrganizationById(long organizationId, OrganizationRaw organizationRaw) throws DatabaseHandlingException {
        /* TODO: - Chỉnh sửa thông tin bảng chính không phụ thuộc
                 - Thông qua bảng chính lấy id của bảng phụ
                 - Chỉnh sửa bảng phụ bằng id đã lấy
         */
        PreparedStatement prepareSelectOldOrganizationByIdStatement = null;

        // ***Bảng chính không phụ thuộc***
        PreparedStatement preparedUpdateOrganizationNameByIdStatement = null;
        PreparedStatement preparedUpdateOrganizationAnnualTurnoverByIdStatement = null;
        PreparedStatement preparedUpdateOrganizationTypeIdByIdStatement = null; // in main table update type id

        // ***Bảng phụ***
        PreparedStatement preparedUpdateCoordinatesByCoordinatesIdStatement = null; // in sub table update coordinates
        PreparedStatement preparedUpdateAddressByAddressIdStatement = null; // in sub table update address
        try {
            databaseCommunication.setCommitMode();
            databaseCommunication.setSavepoint();

            //  Attributes do not reference
            preparedUpdateOrganizationNameByIdStatement = databaseCommunication.getPreparedStatement(UPDATE_ORGANIZATION_NAME_BY_ID, false);
            preparedUpdateOrganizationAnnualTurnoverByIdStatement = databaseCommunication.getPreparedStatement(UPDATE_ORGANIZATION_ANNUAL_TURNOVER_BY_ID, false);
            preparedUpdateOrganizationTypeIdByIdStatement = databaseCommunication.getPreparedStatement(UPDATE_ORGANIZATION_TYPE_BY_ID, false);
            //  Attributes reference
            prepareSelectOldOrganizationByIdStatement = databaseCommunication.getPreparedStatement(SELECT_ORGANIZATION_BY_ID, false);
            int coordinatesId = 0;
            int addressId = 0;
            int typeId = 0;

            preparedUpdateCoordinatesByCoordinatesIdStatement = databaseCommunication.getPreparedStatement(UPDATE_COORDINATES_BY_ORGANIZATION_ID, false);
            preparedUpdateAddressByAddressIdStatement = databaseCommunication.getPreparedStatement(UPDATE_ADDRESS_BY_ADDRESS_ID, false);


            if (organizationRaw.getName() != null) {
                preparedUpdateOrganizationNameByIdStatement.setString(1, organizationRaw.getName());
                preparedUpdateOrganizationNameByIdStatement.setLong(2, organizationId);
                if (preparedUpdateOrganizationNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("UPDATE_ORGANIZATION_NAME_BY_ID request was made.");
            }
            if (organizationRaw.getAnnualTurnover() != -1) {
                preparedUpdateOrganizationAnnualTurnoverByIdStatement.setDouble(1, organizationRaw.getAnnualTurnover());
                preparedUpdateOrganizationAnnualTurnoverByIdStatement.setLong(2, organizationId);
                if (preparedUpdateOrganizationAnnualTurnoverByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("An UPDATE_ORGANIZATION_HEALTH_BY_ID request was made.");
            }

            if (organizationId != -1) {
                prepareSelectOldOrganizationByIdStatement.setLong(1, organizationId);
                ResultSet oldOrganization = prepareSelectOldOrganizationByIdStatement.executeQuery();
                while (oldOrganization.next()) {
                    coordinatesId = oldOrganization.getInt(DatabaseCommunication.ORGANIZATION_TABLE_COORDINATES_ID_COLUMN);
                    addressId = oldOrganization.getInt(DatabaseCommunication.ORGANIZATION_TABLE_ADDRESS_ID_COLUMN);
                    typeId = oldOrganization.getInt(DatabaseCommunication.ORGANIZATION_TABLE_ORGANIZATION_TYPE_ID_COLUMN);
                }
            }

            if (organizationRaw.getCoordinates() != null) {
                preparedUpdateCoordinatesByCoordinatesIdStatement.setInt(1, organizationRaw.getCoordinates().getX());
                preparedUpdateCoordinatesByCoordinatesIdStatement.setDouble(2, organizationRaw.getCoordinates().getY());
                preparedUpdateCoordinatesByCoordinatesIdStatement.setInt(3, coordinatesId);
                if (preparedUpdateCoordinatesByCoordinatesIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("An UPDATE_COORDINATES_BY_ORGANIZATION_ID request was made.");
            }
            if (organizationRaw.getOfficialAddress() != null) {
                preparedUpdateAddressByAddressIdStatement.setString(1, organizationRaw.getOfficialAddress().getStreet());
                preparedUpdateAddressByAddressIdStatement.setString(2, organizationRaw.getOfficialAddress().getZipCode());
                preparedUpdateAddressByAddressIdStatement.setInt(2, addressId);
                if (preparedUpdateAddressByAddressIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("An UPDATE_ORGANIZATION_CATEGORY_BY_ID request was made.");
            }
            if (organizationRaw.getOrganizationType() != null) {
                String newType = organizationRaw.getOrganizationType().toString();
                int newTypeId = getOrganizationTypeIdByType(newType);
                preparedUpdateOrganizationTypeIdByIdStatement.setInt(1, newTypeId);
                preparedUpdateOrganizationTypeIdByIdStatement.setLong(2, organizationId);
                if (preparedUpdateOrganizationTypeIdByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("UPDATE_ORGANIZATION_TYPE_BY_ID request completed.");
            }

            databaseCommunication.commit();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing a group of requests to update an object!");
            databaseCommunication.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseCommunication.closePreparedStatement(preparedUpdateOrganizationNameByIdStatement);
            databaseCommunication.closePreparedStatement(preparedUpdateCoordinatesByCoordinatesIdStatement);
            databaseCommunication.closePreparedStatement(preparedUpdateOrganizationAnnualTurnoverByIdStatement);
            databaseCommunication.closePreparedStatement(preparedUpdateOrganizationTypeIdByIdStatement);
            databaseCommunication.closePreparedStatement(preparedUpdateAddressByAddressIdStatement);
            databaseCommunication.setNormalMode();
        }
    }

    /**
     * Delete Organization by id.
     *
     * @param organizationId ID of Organization.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void deleteOrganizationById(long organizationId) throws DatabaseHandlingException {
        // TODO: Если делаем орден уникальным, тут че-то много всего менять
        PreparedStatement preparedDeleteOrganizationByIdStatement = null;
        try {
            preparedDeleteOrganizationByIdStatement = databaseCommunication.getPreparedStatement(DELETE_ORGANIZATION_BY_ID, false);
            preparedDeleteOrganizationByIdStatement.setLong(1, organizationId);
            if (preparedDeleteOrganizationByIdStatement.executeUpdate() == 0) Outputer.println(3);
            App.logger.info("DELETE_ORGANIZATION_BY_ID request was made.");
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the DELETE_ORGANIZATION_BY_ID request!");
            throw new DatabaseHandlingException();
        } finally {
            databaseCommunication.closePreparedStatement(preparedDeleteOrganizationByIdStatement);
        }
    }

    /**
     * Checks Organization user id.
     *
     * @param marineId Id of Organization.
     * @param user Owner of marine.
     * @throws DatabaseHandlingException When there's exception inside.
     * @return Is everything ok.
     */
    public boolean checkOrganizationUserId(long marineId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectOrganizationByIdAndUserIdStatement = null;
        try {
            preparedSelectOrganizationByIdAndUserIdStatement = databaseCommunication.getPreparedStatement(SELECT_ORGANIZATION_BY_ID_AND_USER_ID, false);
            preparedSelectOrganizationByIdAndUserIdStatement.setLong(1, marineId);
            preparedSelectOrganizationByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectOrganizationByIdAndUserIdStatement.executeQuery();
            App.logger.info("SELECT_ORGANIZATION_BY_ID_AND_USER_ID query completed.");
            return resultSet.next();
        } catch (SQLException exception) {
            App.logger.error("An error occurred while executing the SELECT_ORGANIZATION_BY_ID_AND_USER_ID query!");
            throw new DatabaseHandlingException();
        } finally {
            databaseCommunication.closePreparedStatement(preparedSelectOrganizationByIdAndUserIdStatement);
        }
    }

    /**
     * Clear the collection.
     *
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void clearCollection() throws DatabaseHandlingException {
        ArrayList<Organization> organizationList = getCollection();
        for (Organization marine : organizationList) {
            deleteOrganizationById(marine.getId());
        }
    }
}
