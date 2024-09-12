package br.com.fcamara.controleveiculos.config.jwt.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fcamara.controleveiculos.config.jwt.model.User;
import br.com.fcamara.controleveiculos.config.jwt.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    	Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            return new org.springframework.security.core.userdetails.User(
                    foundUser.getUsername(),
                    foundUser.getPassword(),
                    foundUser.getRoles().stream()
                              .map(SimpleGrantedAuthority::new)
                              .collect(Collectors.toList())
            );
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o nome: " + username);
        }
    }
}