package Datos;

import Modelo.Articulo;
import org.sql2o.Sql2o;
import Modelo.Usuario;
import java.util.List;

public class ArticuloM {

    private Sql2o conex = null;

    public void insertarArticulo(Articulo articulo) {
        String sql = "insert into articulo (id, titulo, cuerpo, id_autor, fecha,activo) values(:id,:titulo,:cuerpo,:id_autor,:fecha,:val)";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();

        Long id = lastArticulo();

        conex.createQuery(sql)
                .addParameter("id",id)
                .addParameter("titulo",articulo.getTitulo())
                .addParameter("cuerpo", articulo.getCuerpo())
                .addParameter("id_autor",articulo.getAutor())
                .addParameter("fecha",articulo.getFecha())
                .addParameter("val",true)
                .executeUpdate();



    }

    public int countArticulos(){
        String sql = "select count(id) from articulo";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        int count = conex.createQuery(sql).executeScalar(Integer.class);
        return count;
    }

    public void borrarArticulo(Long id){
        String sql = "update articulo set activo=false where id="+id;
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        conex.createQuery(sql).executeUpdate();
    }

    public List<Articulo> listarArticulos(){
        String sql = "SELECT * from articulo where activo = true order by id desc";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        return conex.createQuery(sql).executeAndFetch(Articulo.class);
    }

    public Long lastArticulo(){
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();

        String lastId = "select top 1 * from articulo order by id desc";
        Long id = new Long(0);
        if(countArticulos() != 0){
            id = conex.createQuery(lastId).executeScalar(Long.class)+1;
        }
        return id;
    }

    public void insertarArticuloEtiqueta(Long id_articulo,Long id_etiqueta){
        String sql = "insert into articulo_etiquetas (id_articulo, id_etiqueta, activo) values(:id_articulo,:id_etiqueta,:activo)";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();


        conex.createQuery(sql)
                .addParameter("id_articulo",id_articulo)
                .addParameter("id_etiqueta",id_etiqueta)
                .addParameter("activo",true)
                .executeUpdate();

    }

    public void insertarArticuloComentario(Long id_articulo,Long id_comentario){
        String sql = "insert into articulo_comentarios (id_articulo, id_comentario, activo) values(:id_articulo,:id_comentario,:activo)";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();


        conex.createQuery(sql)
                .addParameter("id_articulo",id_articulo)
                .addParameter("id_comentario",id_comentario)
                .addParameter("activo",true)
                .executeUpdate();

    }

    public Articulo getArticuloId(Long id){
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        String sql = "select * from articulo where id= '"+id+"' and activo = true";

        return conex.createQuery(sql).executeAndFetchFirst(Articulo.class);
    }

    public void editarArticulo(Long id, String titulo, String cuerpo){
        String sql = "update articulo set titulo='"+ titulo +"', cuerpo='"+cuerpo+"' where id='"+id+"'";
        Conexion con = new Conexion();
        conex = con.getConexion();
        conex.open();
        conex.createQuery(sql).executeUpdate();
    }

}
