package com.tid.avisExpress.security;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {
    private final String ENCRYPTION_KEY = "c2fcf73009e0f67e893a63614b10ef16f9c78e153127f73dbf3e4fae8164139f";
    private UtilisateurService utilisateurService;

    public Map<String, String> getJwtToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        return this.generateJwtToken(utilisateur);
    }


    public Key getKey() {
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String ExtractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    private Map<String, String> generateJwtToken(Utilisateur utilisateur) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 18000000;

        Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT,utilisateur.getEmail()
        );

        String bearer = Jwts.builder()
                .setSubject(utilisateur.getNom())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("token", bearer);
    }


    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

}
