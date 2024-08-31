package com.tid.avisExpress;

import com.tid.avisExpress.model.Role;
import com.tid.avisExpress.model.TypeDeRole;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.UtilisateurRepository;
import com.tid.avisExpress.services.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@EnableScheduling
@SpringBootApplication
public class AvisExpressApplication implements CommandLineRunner {

	private UtilisateurRepository utilisateurRepository;
	PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(AvisExpressApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Utilisateur admin = Utilisateur.builder()
				.nom("Admin")
				.actif(true)
				.email("admin@admin.com")
				.password(passwordEncoder.encode("admin"))
				.role(
						Role.builder()
								.libelle(TypeDeRole.ADMINISTRATEUR)
								.build()
				)
				.build();
		admin = this.utilisateurRepository.findByEmail("admin@admin.com")
				.orElse(admin);
		this.utilisateurRepository.save(admin);

		Utilisateur manager = Utilisateur.builder()
				.nom("Manager")
				.actif(true)
				.email("Manager@Manager.com")
				.password(passwordEncoder.encode("manager"))
				.role(
						Role.builder()
								.libelle(TypeDeRole.MANAGER)
								.build()
				)
				.build();
		manager = this.utilisateurRepository.findByEmail("Manager@Manager.com")
				.orElse(manager);
		this.utilisateurRepository.save(manager);
	}

}
