import Datos.ArticuloM;
import Datos.ComentarioM;
import Datos.EtiquetaM;
import Datos.UsuarioM;
import Modelo.Articulo;
import Modelo.Comentario;
import Modelo.Etiqueta;
import Modelo.Usuario;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jasypt.util.text.BasicTextEncryptor;
import spark.Session;

import java.io.StringWriter;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {


    public static void main(String[] args) {
        staticFiles.location("/Templates");
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(Main.class, "/");

        UsuarioM usuarioDatos = new UsuarioM();
        ArticuloM articuloDatos = new ArticuloM();
        EtiquetaM etiquetaDatos = new EtiquetaM();
        ComentarioM comentarioDatos = new ComentarioM();

        usuarioDatos.crearBD();

        if(usuarioDatos.contarUsuarios()==0){
            Usuario u = new Usuario("admin","admin", "password",true,true);
            usuarioDatos.crearUsuario(u);
        }

        get("/", (req, res) -> {

            Usuario usuario = req.session(true).attribute("usuario");
            if(usuario == null && req.cookie("username") != null){
                BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                textEncryptor.setPasswordCharArray("1234".toCharArray());
                req.session(true);
                req.session().attribute("usuario", usuarioDatos.getUsuario(textEncryptor.decrypt(req.cookie("username"))));
            }

            res.redirect("/home");
            return "";
        });
        get("/signupPage", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/SignUp.ftl");
            t.process(null, writer);
            return writer;
        });

        get("/log-in", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/Login.ftl");
            t.process(null, writer);
            return writer;
        });

        get("/logout", (req, res) -> {
            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();

            Session ses = req.session(true);
            ses.invalidate();
            res.removeCookie("username");
            res.redirect("/");
            return writer;
        });

        get("/home", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/home.ftl");
            Usuario usuario = req.session(true).attribute("usuario");

            List<Articulo> articuloList = articuloDatos.listarArticulos();
            for(int i = 0; i < articuloList.size(); i++){
                articuloList.get(i).setListaEtiqueta(etiquetaDatos.getEtiquetas(articuloList.get(i).getId()));
            }


            atr.put("usuario",usuario);
            atr.put("articulos",articuloList);
            t.process(atr, writer);
            return writer;
        });

        get("/crearPost", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/crearPost.ftl");
            t.process(atr,writer);
            return writer;
        });

        get("/articulo/:id", (req, res) -> {

            Usuario usuario = req.session(true).attribute("usuario");
            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/articulo.ftl");
            String id = req.params("id");
            Articulo a = articuloDatos.getArticuloId(Long.parseLong(id));
            a.setListaEtiqueta(etiquetaDatos.getEtiquetas(a.getId()));
            a.setListaComentarios(comentarioDatos.getComentario(a.getId()));
            ArrayList<String> usuarios = new ArrayList<>();

            atr.put("articulo",a);
            atr.put("autor",usuarioDatos.getUsuarioId(a.getAutor()));
            atr.put("usuarioList",usuarios);
            atr.put("usuario",usuario);
            t.process(atr,writer);
            return writer;
        });

        post("/crearPost", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/crearPost.ftl");
            Usuario usuario = req.session(true).attribute("usuario");

            String etiquetas = req.queryParams("listaEtiqueta");
            String titulo = req.queryParams("titulo");
            String cuerpo = req.queryParams("cuerpo");
            List<String> listaEtiquetas = Arrays.asList(etiquetas.split(","));

            Long idArticulo = 0L;
            if(articuloDatos.countArticulos() != 0){
                idArticulo = articuloDatos.lastArticulo();

            }
            System.out.println(idArticulo);
            Long countEtiqueta = 0L;
            if(etiquetaDatos.countEtiquetas() != 0){
                countEtiqueta =  etiquetaDatos.lastEtiqueta();
            }
            ArrayList<Etiqueta> et =  new ArrayList<>();
            for(int i = 0; i < listaEtiquetas.size(); i++){
                Etiqueta e = new Etiqueta(listaEtiquetas.get(i));
                et.add(e);
                etiquetaDatos.insertarEtiqueta(e);
            }

            Date d = new Date(System.currentTimeMillis());

            Articulo a =  new Articulo(titulo,cuerpo, usuario.getId(),d,null,et);
            articuloDatos.insertarArticulo(a);

            for(int i = 0; i < listaEtiquetas.size(); i++){
                articuloDatos.insertarArticuloEtiqueta(idArticulo,countEtiqueta);
                countEtiqueta++;
            }

            res.redirect("/");
            return writer;
        });

        post("/login", (req, res) -> {

            String username = req.queryParams("username");
            String password = req.queryParams("password");
            Usuario usuario = usuarioDatos.login(username, password);
            String recuerdame = req.queryParams("recuerdame");
            if (usuario != null) {
                req.session(true);
                req.session().attribute("usuario", usuario);
                if(recuerdame!=null){
                    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                    textEncryptor.setPasswordCharArray("1234".toCharArray());
                    res.cookie("/", "username",
                            textEncryptor.encrypt(username), (60*60*24*7), false, true);
                }
                res.redirect("/");
            } else {
                res.redirect("/log-in");
            }
            return "";
        });

        post("/singup", (req, res) -> {

            String username = req.queryParams("username");
            String password = req.queryParams("password");
            String nombre = req.queryParams("name");
            usuarioDatos.crearUsuario(new Usuario(username,nombre,password,false,false));


            return "";
        });

        post("/insertararticulo", (req, res) -> {
            Date d = new Date(System.currentTimeMillis());
            Usuario usuario = req.session(true).attribute("usuario");
            String fecha = req.queryParams("fecha");
            String titulo = req.queryParams("titulo");
            String cuerpo = req.queryParams("cuerpo");
            articuloDatos.insertarArticulo(new Articulo(titulo,cuerpo,usuario.getId(),d,null,null));


            return "";
        });
        post("articulo/:id/comentar", (req, res) -> {
            Long idArticulo = Long.parseLong(req.params("id"));
            String comentario = req.queryParams("comentario");
            Usuario autor = req.session(true).attribute("usuario");
            Comentario c = new Comentario(comentario,autor.getId());

            Long countComentario = 0L;
            if(comentarioDatos.countComentario() != 0){
                countComentario =  comentarioDatos.lastComentario();
            }
            comentarioDatos.insertarComentario(c);
            articuloDatos.insertarArticuloComentario(idArticulo,countComentario);
            res.redirect("/articulo/" + idArticulo);
            return null;
        });

        get("/eliminar/:id", (req, res) -> {
            Usuario autor = req.session(true).attribute("usuario");

            if (autor.isAdministrator() || autor.isAutor()) {
                StringWriter writer = new StringWriter();
                Map<String, Object> atributos = new HashMap<>();
                Template template = configuration.getTemplate("templates/eliminarArticulo.ftl");

                Articulo articulo = articuloDatos.getArticuloId(Long.parseLong(req.params("id")));

                atributos.put("articulo", articulo);
                template.process(atributos, writer);

                return writer;
            }
            res.redirect("/");
            return null;
        });


        get("/editar/:id", (req, res) -> {
            StringWriter writer = new StringWriter();
            Map<String, Object> atributos = new HashMap<>();
            Template template = configuration.getTemplate("templates/editarArticulo.ftl");
            Usuario autor = req.session(true).attribute("usuario");


            Articulo articulo = articuloDatos.getArticuloId(Long.parseLong(req.params("id")));

            atributos.put("articulo", articulo);
            atributos.put("autor", autor);
            template.process(atributos, writer);

            return writer;
        });


    post("/eliminar/:id", (req, res) -> {
        Usuario autor = req.session(true).attribute("usuario");

        if (autor.isAdministrator() || autor.isAutor()) {
            articuloDatos.borrarArticulo(Long.valueOf(req.params("id")));
        }
        res.redirect("/");
        return null;
    });


    post("/editar/:id", (req, res) -> {
        long id = Integer.parseInt(req.params("id"));
        String titulo = req.queryParams("titulo");
        String cuerpo = req.queryParams("cuerpo");

        articuloDatos.editarArticulo(id,titulo,cuerpo);

        res.redirect("/");

        return null;
    });
    }

    }
