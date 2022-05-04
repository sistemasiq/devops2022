package mx.tecnm.piedad.controllers;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import mx.tecnm.piedad.dao.UsuariosJDBC;

//AQUI ESTUVO EL BENJA
// comentario para ejemplificar cambios en Git

@RestController 
@RequestMapping("/api/mensajes")
public class Mensaje {
	@Autowired
	UsuariosJDBC repo;
	
	@GetMapping("/hola")
	public String saludar() {
		return "¡Hola Devops!";
	}
	
	@GetMapping("/eco")
	public String eco(@RequestParam String mensaje) {
		return mensaje + " " + mensaje +" " + mensaje;
	}
	
	@GetMapping("/saludo")
	public String saludarUsuario(@RequestParam String usuario, @RequestParam String mensaje) {
		return usuario + " " + mensaje;
	}
	
	@GetMapping("/mensaje/{numero}")
	public String elegirMensaje(@PathVariable int numero) {
		String mensajes[] = new String[] {"Hoy depositan","Arriba el América", "Ya es viernes"};
		try {
			return mensajes[numero];
		} catch (Exception e) {
			return "Suerte para la próxima siga participando";
		}
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> autenticar(@RequestParam String email, @RequestParam String contrasena) {
		String token="";
		if (repo.login(email, contrasena))
		{
			token = generarJWTToken(email);
			return new ResponseEntity<String>(token, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>( HttpStatus.NO_CONTENT);
		}
		
	}
	
	private String generarJWTToken(String usuario) {
		String secretKey = "abracadabra";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("itlpJWT")
				.setSubject(usuario)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

	

}
