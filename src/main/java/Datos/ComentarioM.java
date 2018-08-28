package Datos;

import Modelo.Comentario;
import Modelo.Etiqueta;
import org.sql2o.Sql2o;

import java.util.List;

public class ComentarioM {

    private Sql2o conexion = null;

    public void insertarComentario(Comentario comentario) {
        String sql = "insert into comentario (id, comentario, id_autor, val) values(:id, :comentario, :id_autor, :val)";
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        String lastId = "select top 1 * from comentario order by id desc";
        Long id = lastComentario();

        conexion.createQuery(sql)
                .addParameter("id",id)
                .addParameter("comentario",comentario.getComentario())
                .addParameter("id_autor",comentario.getAutor())
                .addParameter("val",true)
                .executeUpdate();

    }
    public int countComentario(){
        String sql = "select count(id) from comentario";
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        int count = conexion.createQuery(sql).executeScalar(Integer.class);
        return count;
    }

    public void borrarComentario(Long id){
        String sql = "update comentario set val=false where id="+id;
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        conexion.createQuery(sql).executeUpdate();
    }

    public List<Comentario> getComentario(Long idArticulo){
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();

        String sql = "select distinct id_comentario,  comentario from comentario, articulo_comentarios, articulo where articulo_comentarios.id_articulo = '"+idArticulo+"' and id_comentario = comentario.id and comentario.val = true order by id_comentario";
        return conexion.createQuery(sql).executeAndFetch(Comentario.class);
    }


    public Long lastComentario(){
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();

        String lastId = "select top 1 * from comentario order by id desc";
        Long id = new Long(0);
        if(countComentario() != 0){
            id = conexion.createQuery(lastId).executeScalar(Long.class)+1;
        }
        return id;
    }

    public List<Etiqueta> getEtiquetas(Long idArticulo){
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();

        String sql = "select distinct id_comentario,  comentario from comentario, articulo_comentarios, articulo where id_articulo = '"+idArticulo+"' and id_comentario = comentario.id and comentario.val = true order by id_comentario";
        return conexion.createQuery(sql).executeAndFetch(Etiqueta.class);
    }
}
