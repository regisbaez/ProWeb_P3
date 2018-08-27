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
