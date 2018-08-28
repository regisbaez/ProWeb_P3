package Datos;

import Modelo.Usuario;
import org.sql2o.Sql2o;
import sun.rmi.server.InactiveGroupException;

import java.util.List;

public class UsuarioM {

    private Sql2o conex = null;


    public void crearUsuario(Usuario usuario) {
        String sql = "insert into usuario (id, username, nombre, password,administrator,autor,val) values(:id,:username,:nombre,:password,:administrator,:autor,:val)";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        String lastId = "select top 1 * from usuario order by id desc";
        Long id = new Long(0);
        if(contarUsuarios() != 0){
            id = conex.createQuery(lastId).executeScalar(Long.class)+1;
        }



        conex.createQuery(sql)
                .addParameter("id",id)
                .addParameter("username",usuario.getUsername())
                .addParameter("nombre", usuario.getNombre())
                .addParameter("password",usuario.getPassword())
                .addParameter("administrator",usuario.isAdministrator())
                .addParameter("autor",usuario.isAutor())
                .addParameter("val",true)
                .executeUpdate();



    }

    public Usuario getUsuario(String nombre){
        String sql = "select username from usuario where username = '" + nombre +"' and val = true";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        return conex.createQuery(sql).executeAndFetchFirst(Usuario.class);
    }

    public List<Usuario> getAllUsuario(){
        String sql = "select * from usuario where val = true";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        return conex.createQuery(sql).executeAndFetch(Usuario.class);
    }

    public Usuario getUsuarioId(Long id){
        String sql = "select username from usuario where id = '" + id +"' and val = true";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        return conex.createQuery(sql).executeAndFetchFirst(Usuario.class);
    }

    public void crearBD(){
        String sql = "CREATE TABLE IF NOT EXISTS USUARIO (\n" +
                "  ID            BIGINT PRIMARY KEY,\n" +
                "  USERNAME      VARCHAR2(30),\n" +
                "  NOMBRE        VARCHAR2(30),\n" +
                "  PASSWORD      VARCHAR2(30),\n" +
                "  ADMINISTRATOR BOOLEAN,\n" +
                "  AUTOR         BOOLEAN,\n" +
                "  COOKIES       VARCHAR2(255),\n" +
                "  VAL        BOOLEAN\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS ETIQUETA (\n" +
                "  ID       BIGINT PRIMARY KEY,\n" +
                "  ETIQUETA VARCHAR2(200),\n" +
                " VAL    BOOLEAN\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS ARTICULO (\n" +
                "  ID       BIGINT PRIMARY KEY,\n" +
                "  TITULO   VARCHAR2(70),\n" +
                "  CUERPO   VARCHAR2(900000),\n" +
                "  ID_AUTOR BIGINT REFERENCES USUARIO (ID) ON UPDATE CASCADE,\n" +
                "  FECHA    DATE,\n" +
                "  VAL   BOOLEAN\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS COMENTARIO (\n" +
                "  ID          BIGINT PRIMARY KEY,\n" +
                "  COMENTARIO  VARCHAR2(1000),\n" +
                "  ID_AUTOR    BIGINT REFERENCES USUARIO (ID) ON UPDATE CASCADE,\n" +
                "  ID_ARTICULO BIGINT REFERENCES ARTICULO (ID) ON UPDATE CASCADE,\n" +
                "  VAL      BOOLEAN\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS ARTICULO_COMENTARIOS (\n" +
                "  ID_ARTICULO   BIGINT REFERENCES ARTICULO (ID) ON UPDATE CASCADE,\n" +
                "  ID_COMENTARIO BIGINT REFERENCES COMENTARIO (ID) ON UPDATE CASCADE,\n" +
                "  VAL        BOOLEAN\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS ARTICULO_ETIQUETAS (\n" +
                "  ID_ARTICULO BIGINT REFERENCES ARTICULO (ID) ON UPDATE CASCADE,\n" +
                "  ID_ETIQUETA BIGINT REFERENCES ETIQUETA (ID) ON UPDATE CASCADE,\n" +
                "  VAL      BOOLEAN\n" +
                ");\n";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        conex.createQuery(sql).executeUpdate();
    }

    public Usuario login(String usuario, String contra){
        String q = "select * from usuario where username = '"+usuario+"' and password = '"+contra+"'";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();

        return conex.createQuery(q).executeAndFetchFirst(Usuario.class);
    }
    public int contarUsuarios(){
        String q = "select count(id) from usuario";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        return conex.createQuery(q).executeScalar(Integer.class);

    }
}
