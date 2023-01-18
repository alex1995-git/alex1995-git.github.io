package com.depcue;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.depcue.model.Usuario;
import com.depcue.repository.IUsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoWebApplicationTest {

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private IUsuarioRepository repousu;
	
	@Test
	public void crearUsuarioTest() {
		Usuario u = new Usuario();
		u.setUsername("chris");
		u.setPassword(encoder.encode("1234"));
		Usuario usuarionuevo=repousu.save(u);
		
		assertTrue(usuarionuevo.getUsername().equalsIgnoreCase(u.getPassword()));
	}

}
