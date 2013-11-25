package ws;

import entity.Usuarios;
import entity.UsuariosPK;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.sql.DataSource;

@WebService(serviceName = "UsuarioWS")
public class UsuarioWS {
    
    @Resource(name = "meubanco")
    private DataSource        banco;
    private Connection        conn;
    private PreparedStatement sttm;
    private ResultSet         res;
    private Usuarios          usuario;
    private UsuariosPK        usuarioPK;

    /**
     * Operação de Web service
     * @return int
     */
    @WebMethod(operationName = "count")
    public int count() {
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("SELECT COUNT(*) AS total FROM usuarios");
            this.res = sttm.executeQuery();
            res.next();
            return res.getInt("total");
        
        } catch (SQLException ex) {
            
            return ex.getErrorCode();
        
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
        
    }

    /**
     * Operação de Web service
     * @return List<Usuarios>
     */
    @WebMethod(operationName = "list")
    public List<Usuarios> list() {
        
        List<Usuarios> usuarios = new ArrayList<>();
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("SELECT * FROM usuarios ORDER BY nome ASC");
            this.res = sttm.executeQuery();
            
            while( res.next() ) {
        
                this.usuario   = new Usuarios();
                this.usuarioPK = new UsuariosPK();
            
                usuarioPK.setIdusuario( res.getInt("idusuario") );
                usuarioPK.setLogin( res.getString("login") );
                usuario.setNome( res.getString("nome") );
                usuario.setSenha( res.getString("senha") );
                usuario.setUsuariosPK(usuarioPK);
            
                usuarios.add(usuario);
                
            }
            
            return usuarios;
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return null;
            
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
        
    }

    /**
     * Operação de Web service
     * @param idusuario
     * @return Usuarios
     */
    @WebMethod(operationName = "usuario")
    public Usuarios usuario(@WebParam(name = "idusuario") int idusuario) {
        
        if (idusuario <= 0) { return null; }
        
        this.usuario   = new Usuarios();
        this.usuarioPK = new UsuariosPK();
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("SELECT * FROM usuarios WHERE idusuario = ?");
            this.sttm.setInt(1, idusuario);
            this.res = sttm.executeQuery();
            res.next();
            
            usuarioPK.setIdusuario( res.getInt("idusuario") );
            usuarioPK.setLogin( res.getString("login") );
            usuario.setNome( res.getString("nome") );
            usuario.setSenha( res.getString("senha") );
            usuario.setUsuariosPK(usuarioPK);
            
            return usuario;
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return null;
            
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
    
    }

    /**
     * Operação de Web service
     * @param idusuario
     * @return int
     */
    @WebMethod(operationName = "remove")
    public int remove(@WebParam(name = "idusuario") int idusuario) {
        
        if (idusuario <= 0) { return 0; }
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("DELETE FROM usuarios WHERE idusuario = ?");
            this.sttm.setInt(1, idusuario);
            sttm.execute();
            
            return 1;
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 0;
            
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
        
    }

    /**
     * Operação de Web service
     * @param nome
     * @param login
     * @param senha
     * @return int
     */
    @WebMethod(operationName = "add")
    public int add(@WebParam(name = "nome") String nome, @WebParam(name = "login") String login, @WebParam(name = "senha") String senha) {
        
        if ( nome.trim().equals("") || login.trim().equals("") || senha.trim().equals("") ) { return 0; }
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("INSERT INTO usuarios VALUES (NULL, ?, ?, ?)");
            this.sttm.setString(1, nome);
            this.sttm.setString(2, login);
            this.sttm.setString(3, senha);
            sttm.execute();
            
            return 1;
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 0;
            
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
        
    }

    /**
     * Operação de Web service
     * @param idusuario
     * @param nome
     * @param senha
     * @return int
     */
    @WebMethod(operationName = "update")
    public int update(@WebParam(name = "idusuario") int idusuario, @WebParam(name = "nome") String nome, @WebParam(name = "senha") String senha) {
        
        if (idusuario <= 0 ||  nome.trim().equals("") || senha.trim().equals("") ) { return 0; }
        
        try {
            
            this.conn = this.banco.getConnection();
            this.sttm = conn.prepareStatement("UPDATE usuarios SET nome = ?, senha = ? WHERE idusuario = ?");
            this.sttm.setString(1, nome);
            this.sttm.setString(2, senha);
            this.sttm.setInt(3, idusuario);
            sttm.execute();
            
            return 1;
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 0;
            
        } finally {
            try {
                this.conn.close();
                this.sttm.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }            
        }
        
    }
    
}
