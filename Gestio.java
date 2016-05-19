import java.sql.*;
import java.util.Properties;
import java.io.*;
import java.util.Scanner;




public class Gestio
{
  private static final String user="guillem";
  private static final String password="guillem";
  private static final String dbClassName = "com.mysql.jdbc.Driver";
  private static final String CONNECTION =
                          "jdbc:mysql://172.30.10.143/gestio_d";

  public static void menu(){
		//System.out.print("\033[H\033[2J");
		System.out.println("MENU \n"+
		"1.- VISUALITZAR CLIENT \n"+
		"2.- MÀXIM CODI CLIENT \n"+
		"3.- NOU CLIENT PERSONA \n"+
		"Q.- SORTIR \n"+
		"");
		}

  public static void main(String[] args)
    throws ClassNotFoundException,SQLException{
    Scanner userInput = new Scanner(System.in);
    String strChoice="";
    while(!strChoice.equals("q") && !strChoice.equals("Q") ){
      menu();
      strChoice = userInput.next();
      switch(strChoice){
        case "1":
          System.out.println("Introduir codi client");
          String strCodi=userInput.next();
          int codi=Integer.parseInt(strCodi);
          visualitzaClient(codi);
          break;
        case "2":
          int maxCodiClient=maxCodiClient();
          System.out.println("MAX CODI CLIENT "+maxCodiClient);
          break;
        case "3":
          nouClientPersona();
          break;
        }
    }
  }

  private static void visualitzaClient(int id_client)
  throws ClassNotFoundException,SQLException {
    // creates a drivermanager class factory
    Class.forName(dbClassName);
    // Properties for user and password. Here the user and password are both 'paulr'
    Properties p = new Properties();
    p.put("user",user);
    p.put("password",password);
    // Now try to connect
    Connection c = DriverManager.getConnection(CONNECTION,p);

    //suposam que el client és una PERSONA_CLI
    String sql = "select C.id_client,PC.nom,PC.llinatge1,PC.llinatge2 FROM CLIENT C inner join PERSONA_CLI PC on PC.id_client=C.id_client WHERE C.id_client=?";

    PreparedStatement preparedStatement = c.prepareStatement(sql);
    preparedStatement.setInt(1, id_client);
    // execute select SQL stetement

   ResultSet rs = preparedStatement.executeQuery();

     if(rs.next()){
      int codi_client=rs.getInt("id_client");
      String nom = rs.getString("nom");
      System.out.println("persona nom: "+nom+
      " llinatge1: "+rs.getString("llinatge1"));
    }else{
      //comprovam si el client és una empresa
      sql="SELECT C.id_client,nom,cif,telefon";
      sql+=" FROM gestio_d.EMPRESA_CLI EC INNER JOIN CLIENT C ";
      sql+=" on C.id_client=EC.id_client where C.id_client=?";
      preparedStatement = c.prepareStatement(sql);
      preparedStatement.setInt(1, id_client);
      // execute select SQL stetement
      ResultSet rs2 = preparedStatement.executeQuery();
      if (rs2.next()){
        String nom=rs2.getString("nom");
        System.out.println("empresa nom: "+nom);
      }else{
        System.out.println("client no trobat");
      }
    }
   c.close();

  }

  private static void nouClientPersona()
    throws ClassNotFoundException,SQLException {
      int codi=maxCodiClient()+1;
      // creates a drivermanager class factory
      Class.forName(dbClassName);
      // Properties for user and password. Here the user and password are both 'paulr'
      Properties p = new Properties();
      p.put("user",user);
      p.put("password",password);
      // Now try to connect
      Connection c = DriverManager.getConnection(CONNECTION,p);
      String sql="insert into CLIENT (id_client) values (?)";
      PreparedStatement ps=c.prepareStatement(sql);
      ps.setInt(1,codi);
      ps.execute();

      Scanner userInput = new Scanner(System.in);
      System.out.println("Introduir nom del client");
      String nom=userInput.next();
      System.out.println("Introduir llinatge1 del client");
      String llinatge1=userInput.next();
      System.out.println("Introduir llinatges2 del client");
      String llinatge2=userInput.next();
      System.out.println("Introduir nif del client");
      String nif=userInput.next();
      System.out.println("Introduir telèfon del client");
      String telefon=userInput.next();

      sql="INSERT INTO PERSONA_CLI SET id_client=?, "+
      "nom=?,llinatge1=?,llinatge2=?,nif=?,telefon=?";
      ps=c.prepareStatement(sql);
      ps.setInt(1,codi);
      ps.setString(2,nom);
      ps.setString(3,llinatge1);
      ps.setString(4,llinatge2);
      ps.setString(5,nif);
      ps.setString(6,telefon);
      ps.execute();



      c.close();

  }

  private static int maxCodiClient()
  throws ClassNotFoundException,SQLException {
    //dona el id_client més alt de la taula CLIENT
    // creates a drivermanager class factory
    Class.forName(dbClassName);
    // Properties for user and password. Here the user and password are both 'paulr'
    Properties p = new Properties();
    p.put("user",user);
    p.put("password",password);
    // Now try to connect
    Connection c = DriverManager.getConnection(CONNECTION,p);
    String sql="select max(id_client) as max from CLIENT";
    PreparedStatement preparedStatement = c.prepareStatement(sql);
    // execute select SQL stetement
    ResultSet rs = preparedStatement.executeQuery();
    rs.next();
    int max=rs.getInt("max");
    c.close();
    return max;

  }
}
