package br.com.fcamara.controleveiculos;

import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fcamara.controleveiculos.model.User;
import br.com.fcamara.controleveiculos.repository.UserRepository;

@SpringBootApplication
public class ControleveiculosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleveiculosApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	
	@Bean
	ApplicationRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		return args -> {
			// Verifica se o usuário padrão já existe
			if (userRepository.findByUsername("username").isEmpty()) {
				// Criptografa a senha
				String encryptedPassword = passwordEncoder.encode("password");
				
				// Cria o usuário padrão
				User adminUser = User.builder()
						.username("username")
						.password(encryptedPassword)
						.build();
				
				// Salva o usuário no banco de dados
				userRepository.save(adminUser);
				
				System.out.println("Usuário admin criado com sucesso!");
			} else {
				System.out.println("Usuário admin já existe.");
			}
		};
	}

}
