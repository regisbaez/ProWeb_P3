package Modelo;

public class Etiqueta {
    public Long getId_etiqueta() {
        return id_etiqueta;
    }

    public void setId_etiqueta(Long id_etiqueta) {
        this.id_etiqueta = id_etiqueta;
    }

    private Long id_etiqueta;
    private String etiqueta;

    public boolean isval() {
        return val;
    }

    public void setval(boolean val) {
        this.val = val;
    }

    private boolean val;

    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
