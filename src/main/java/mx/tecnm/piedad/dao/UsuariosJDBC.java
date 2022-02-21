package mx.tecnm.piedad.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuariosJDBC {
	
	@Autowired
	JdbcTemplate conexion;
	
	public boolean login(String email, String contrasena) {
		String sql = "SELECT COUNT(*) \r\n" + 
				"FROM cuentas \r\n" + 
				"WHERE email = ? AND `password` = ? AND activa = 1";
		return conexion.queryForObject(sql, Integer.class, email, contrasena) == 1;
	}
}
