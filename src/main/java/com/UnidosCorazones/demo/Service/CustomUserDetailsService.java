package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Usuario;
import com.UnidosCorazones.demo.Respository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override // 3. Sobrescribir el metodo
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ahora esto funciona, porque findByCorreo devuelve un Optional
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con el correo: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();

        String rol = "ROLE_" + usuario.getTipo_usuario().toUpperCase();
        authorities.add(new SimpleGrantedAuthority(rol));

        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                authorities
        );
    }
}
