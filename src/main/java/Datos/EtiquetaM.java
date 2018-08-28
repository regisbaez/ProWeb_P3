package Datos;

import Modelo.Etiqueta;
import org.sql2o.Sql2o;

import java.util.List;

public class EtiquetaM {
    private Sql2o conexion = null;

    public void insertarEtiqueta(Etiqueta etiqueta) {
        String sql = "insert into etiqueta (id, etiqueta, val) values(:id, :etiqueta, :val)";
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        Long id = lastEtiqueta();
        conexion.createQuery(sql)
                .addParameter("id",id)
                .addParameter("etiqueta",etiqueta.getEtiqueta())
                .addParameter("val",true)
                .executeUpdate();

    }
    public int countEtiquetas(){
        String sql = "select count(id) from etiqueta";
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        int count = conexion.createQuery(sql).executeScalar(Integer.class);
        return count;
    }

    public void borrarEtiqueta(Long id){
        String sql = "update etiqueta set val=false where id="+id;
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();
        conexion.createQuery(sql).executeUpdate();
    }

    public Long lastEtiqueta(){
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();

        String lastId = "select top 1 * from etiqueta order by id desc";
        Long id = new Long(0);
        if(countEtiquetas() != 0){
            id = conexion.createQuery(lastId).executeScalar(Long.class)+1;
        }
        return id;
    }

    public List<Etiqueta> getEtiquetas(Long idArticulo){
        Conexion con = new Conexion();
        conexion = con.getConexion();
        conexion.open();

        String sql = "select distinct id_etiqueta,  etiqueta from etiqueta, articulo_etiquetas, articulo where id_articulo = '"+idArticulo+"' and id_etiqueta = etiqueta.id and etiqueta.val = true order by id_etiqueta";
        return conexion.createQuery(sql).executeAndFetch(Etiqueta.class);
    }
}
