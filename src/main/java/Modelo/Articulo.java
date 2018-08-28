package Modelo;

import java.util.Date;
import java.util.List;

public class Articulo {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String titulo;
    private String cuerpo;
    private Long id_autor;
    private Date fecha;
    private List<Comentario> listaComentarios;

    public Long getId_autor() {
        return id_autor;
    }

    public void setId_autor(Long id_autor) {
        this.id_autor = id_autor;
    }

    public boolean isActivo() {
        return val;
    }

    public void setActivo(boolean val) {
        this.val = val;
    }


    private boolean val;


    public Articulo(String titulo, String cuerpo, Long autor, Date fecha, List<Comentario> listaComentarios, List<Etiqueta> listaEtiqueta) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.id_autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiqueta = listaEtiqueta;
    }

    public List<Comentario> getListaComentarios() {

        return listaComentarios;
    }

    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public List<Etiqueta> getListaEtiqueta() {
        return listaEtiqueta;
    }

    public void setListaEtiqueta(List<Etiqueta> listaEtiqueta) {
        this.listaEtiqueta = listaEtiqueta;
    }

    private List<Etiqueta> listaEtiqueta;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Long getAutor() {
        return id_autor;
    }

    public void setAutor(Long autor) {
        this.id_autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
