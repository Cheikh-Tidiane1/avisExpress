package com.tid.avisExpress.security;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.UtilisateurService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Service
public class JwtService {
    private final String ENCRYPTION_KEY = "c2fcf73009e0f67e893a63614b10ef16f9c78e153127f73dbf3e4fae8164139f";
    private UtilisateurService utilisateurService;

    public Map<String, String> getJwtToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        return this.generateJwtToken(utilisateur);
    }

    private Map<String, String> generateJwtToken(Utilisateur utilisateur) {

        Map<String, String> claims = Map.of(
                "nom", utilisateur.getNom(),
                "email", utilisateur.getEmail()
        );
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 18000000;
        String bearer = Jwts.builder()
                .setSubject(utilisateur.getNom())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("token", bearer);
    }

    public Key getKey() {
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

}
