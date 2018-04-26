package com.VendorAccounts.Limit;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LimitModel extends AbstractModel {

    public LimitModel() throws Exception {}

    private String checkLimitSQL =
        "SELECT " +
            "   COUNT(*), " +
            "   CASE " +
            "       WHEN " +
            "           COUNT(*) >= (" +
            "               SELECT " +
            "                   <%column%> " +
            "               FROM " +
            "                   vendors " +
            "               WHERE " +
            "                   id =? " +
            "           ) " +
            "       THEN 1 " +
            "       ELSE 0 " +
            "   END " +
            "FROM " +
            "   <%table%> " +
            "WHERE " +
            "   vendor_id = ?";

    public void checkLimit(
            int vendor_id,
            String table,
            String column
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        this.initLimitDAO();
        try {
            this.checkLimitSQL = this.checkLimitSQL.replace("<%column%>", column);
            this.checkLimitSQL = this.checkLimitSQL.replace("<%table%>", table);
            preparedStatement = this.LimitDAO.prepareStatement(this.checkLimitSQL);
            preparedStatement.setInt(1, vendor_id);
            preparedStatement.setInt(2, vendor_id);
            resultSet = preparedStatement.executeQuery();
            int value = -1;
            while (resultSet.next()) {
                value = resultSet.getInt("case");
            }
            if (value == -1) {
                throw new Exception("Unable to check limits."); // Don't know why...
            }
            if (value == 1) {
                throw new LimitException("Sorry! You've reached the maximum " + column + " for this account. Please consider upgrading your account to allow more items.");
            }
        } catch (LimitException ex) {
            throw new LimitException(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unknown error checking limits."); // Don't know why.
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.LimitDAO != null) {
                this.LimitDAO.close();
            }
        }
    }

    private String checkImageLimitSQL =
            "SELECT " +
                    "   COUNT(*), " +
                    "   CASE " +
                    "       WHEN " +
                    "           COUNT(*) >= (" +
                    "               SELECT " +
                    "                   <%column%> " +
                    "               FROM " +
                    "                   vendors " +
                    "               WHERE " +
                    "                   id = ? " +
                    "           ) " +
                    "       THEN 1 " +
                    "       ELSE 0 " +
                    "   END " +
                    "FROM " +
                    "   <%table%> " +
                    "WHERE " +
                    "   <%resource%>_id = ?";

    public void checkImageLimit(
            int vendor_id,
            String table,
            String column,
            String resource,
            int resource_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        this.initLimitDAO();
        try {
            this.checkImageLimitSQL= this.checkImageLimitSQL.replace("<%column%>", column);
            this.checkImageLimitSQL = this.checkImageLimitSQL.replace("<%table%>", table);
            this.checkImageLimitSQL = this.checkImageLimitSQL.replace("<%resource%>", resource);
            preparedStatement = this.LimitDAO.prepareStatement(this.checkImageLimitSQL);
            preparedStatement.setInt(1, vendor_id);
            preparedStatement.setInt(2, resource_id);
            resultSet = preparedStatement.executeQuery();
            int value = -1;
            while (resultSet.next()) {
                value = resultSet.getInt("case");
            }
            if (value == -1) {
                throw new Exception("Unable to check limits."); // Don't know why...
            }
            if (value == 1) {
                throw new LimitException("Sorry! You've reached the maximum " + column + " for this account. Please consider upgrading your account to allow more items.");
            }
        } catch (LimitException ex) {
            throw new LimitException(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unknown error checking limits."); // Don't know why.
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.LimitDAO != null) {
                this.LimitDAO.close();
            }
        }
    }
}
