package com.depcue.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.depcue.model.Usuario;
import com.depcue.repository.IUsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private IUsuarioRepository repusu;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> userOpt = repusu.findByEstadoAndUsername("A", username);
		if (!userOpt.isPresent()) {
			throw new UsernameNotFoundException(String.format("No se ha encontrado usuario con username '%s'.", username));
		}
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(userOpt.get().getRole()));
		UserDetails ud = new User(userOpt.get().getUsername(), userOpt.get().getPassword(), roles);
		return ud;
	}

}
