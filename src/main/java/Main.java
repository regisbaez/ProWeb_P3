import Datos.ArticuloM;
import Datos.UsuarioM;
import Modelo.Articulo;
import Modelo.Usuario;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        get("/home", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/home.ftl");
            Usuario usuario = req.session(true).attribute("usuario");
            atr.put("usuario",usuario);
            t.process(atr, writer);
            return writer;
        });

        get("/crearPost", (req, res) -> {

            StringWriter writer = new StringWriter();
            Map<String, Object> atr = new HashMap<>();
            Template t = configuration.getTemplate("Templates/crearPost.ftl");
            Usuario usuario = req.session(true).attribute("usuario");
            t.process(null, writer);
            atr.put("usuario",usuario);
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
    }
    }
