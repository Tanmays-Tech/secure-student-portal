package com.example.security;
import java.sql.*;
public final class Database {
  private static final String URL = "jdbc:h2:mem:portal;DB_CLOSE_DELAY=-1";
  static { try (Connection c=connect(); Statement s=c.createStatement()) {
    s.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY PRIMARY KEY, username VARCHAR(40) UNIQUE NOT NULL, password_hash VARCHAR(100) NOT NULL)");
  } catch(SQLException e){throw new ExceptionInInitializerError(e);} }
  private Database(){}
  public static Connection connect() throws SQLException { return DriverManager.getConnection(URL,"sa",""); }
}