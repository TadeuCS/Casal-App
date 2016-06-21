/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Tadeu
 */
public class Manager {

    PreparedStatement ps = null;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    public Manager() {
        this.con = getConnection();
    }

    public String getDiretorio(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        return arquivo.getAbsolutePath();
    }

    public Connection getConnection() {
//        String Url = "jdbc:firebirdsql://localhost:3050/C:/CasalApp/src/Banco/Casal.FDB";
        String diretorio = getDiretorio("src/Banco/Casal.FDB"); //caminho pra testes na IDE
//        String diretorio=getDiretorio("Banco/Casal.FDB"); //caminho pra execução do aplicativo
        String Url = "jdbc:firebirdsql://localhost:3050/" + diretorio;
        String Password = "masterkey";
        String User = "SYSDBA";
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            con = DriverManager.getConnection(Url, User, Password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!" + e.getMessage());
        }
        return con;
    }

    public ResultSet consulta(String query) {
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao efetuar consulta!");
        }
        return rs;
    }

    public String insere(String query) throws SQLException {
        String resultado = null;
        ps = con.prepareStatement(query);
        ps.execute();
        ps.close();
        return resultado;
    }
}
