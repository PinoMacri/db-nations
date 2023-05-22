package db.nations.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/nazioni_db";
        String user = "root";
        String password = "root";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            // Prepara la query con un segnaposto per il parametro
            String sql = "SELECT c.country_id, c.name AS country_name, r.name AS region_name, c2.name AS continent_name "
                    + "FROM countries c "
                    + "JOIN regions r ON r.region_id = c.region_id "
                    + "JOIN continents c2 ON r.continent_id = c2.continent_id "
                    + "WHERE c.name LIKE ? " // il segnaposto mi permette di lasciare un campo vuoto che sarà poi sostituito dalla stringa dell'utente
                    + "ORDER BY c.name";

            // creo un oggetto preparedstatement per utilizzare i parametri nella query
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                // richiedo all'utente di inserire una stringa di filtro
                Scanner scanner = new Scanner(System.in);
                System.out.print("Inserisci la stringa di filtro: ");
                String filterString = scanner.nextLine();

                // sostituisco la stringa dell'utente col primo segnaposto inserito (1) se avessi scritto 2 andava a sostituire il secondo segnaposto
                statement.setString(1, "%" + filterString + "%");

                // eseguo la query
                ResultSet resultSet = statement.executeQuery();

                // processo i risultati della query
                while (resultSet.next()) {
                    // operazione sui dati restituiti
                    int countryId = resultSet.getInt("country_id");
                    String countryName = resultSet.getString("country_name");
                    String regionName = resultSet.getString("region_name");
                    String continentName = resultSet.getString("continent_name");

                    System.out.println("Country ID: " + countryId);
                    System.out.println("Country Name: " + countryName);
                    System.out.println("Region Name: " + regionName);
                    System.out.println("Continent Name: " + continentName);
                    System.out.println();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

