package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAO {

  public Customer doRetrieveById(int id) {
    try (Connection con = ConPool.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement("SELECT id, firstName, lastName, balance FROM customer WHERE id=?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Customer p = new Customer();
        p.setId(rs.getInt(1));
        p.setFirstName(rs.getString(2));
        p.setLastName(rs.getString(3));
        p.setBalance(rs.getDouble(4));
        return p;
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void doSave(Customer customer) {
    try (Connection con = ConPool.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              "INSERT INTO customer (firstName, lastName, balance) VALUES(?,?,?)",
              Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, customer.getFirstName());
      ps.setString(2, customer.getLastName());
      ps.setDouble(3, customer.getBalance());
      if (ps.executeUpdate() != 1) {
        throw new RuntimeException("INSERT error.");
      }
      ResultSet rs = ps.getGeneratedKeys();
      rs.next();
      int id = rs.getInt(1);
      customer.setId(id);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Customer> doRetrieveAll() {
    try (Connection con = ConPool.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement("SELECT id, firstName, lastName, balance FROM customer");
      ResultSet rs = ps.executeQuery();
      ArrayList<Customer> customers = new ArrayList<Customer>();
      while (rs.next()) {
        Customer p = new Customer();
        p.setId(rs.getInt(1));
        p.setFirstName(rs.getString(2));
        p.setLastName(rs.getString(3));
        p.setBalance(rs.getDouble(4));
        customers.add(p);
      }
      return customers;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void doUpdate(Customer c) {
    try (Connection con = ConPool.getConnection()) {
      Statement st = con.createStatement();
      String query =
          "UPDATE Customer SET firstName='"
              + c.getFirstName()
              + "', lastName='"
              + c.getLastName()
              + "', balance="
              + c.getBalance()
              + " WHERE id="
              + c.getId()
              + ";";
      st.executeUpdate(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Customer> doRetrievePrefs() {
    try (Connection con = ConPool.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              "SELECT id, firstName, lastName, balance FROM customer WHERE bookmarked=true ");
      ResultSet rs = ps.executeQuery();
      ArrayList<Customer> customers = new ArrayList<Customer>();
      while (rs.next()) {
        Customer p = new Customer();
        p.setId(rs.getInt(1));
        p.setFirstName(rs.getString(2));
        p.setLastName(rs.getString(3));
        p.setBalance(rs.getDouble(4));
        customers.add(p);
      }
      return customers;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
