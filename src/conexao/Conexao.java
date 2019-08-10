package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author luiz.pereira
 */
public class Conexao {
    //Declaração das strings que vão compor a url da conexão
    String servername = "ip";
    String usuario = "user";
    String senha = "password";

    public Connection con;
    public Conexao(String myDB) throws SQLException{
        con = DriverManager.getConnection("jdbc:firebirdsql://".concat(servername).concat("/").concat(myDB).concat("?encoding=WIN1252;characterEncoding=UTF-8;autoReconnect=true"), usuario, senha);
    } 
}
