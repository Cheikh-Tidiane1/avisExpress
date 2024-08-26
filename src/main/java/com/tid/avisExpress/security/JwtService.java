package com.tid.avisExpress.security;
import com.tid.avisExpress.model.Jwt;
import com.tid.avisExpress.model.RefreshToken;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.JwtRepository;
import com.tid.avisExpress.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
@Transactional
@AllArgsConstructor
@Slf4j
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    private final String ENCRYPTION_KEY = "c2fcf73009e0f67e893a63614b10ef16f9c78e153127f73dbf3e4fae8164139f";
    private JwtRepository jwtRepository ;
    private UtilisateurService utilisateurService;

    public Map<String, String> getJwtToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
        Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwtToken(utilisateur));
        RefreshToken refreshToken = RefreshToken.builder()
                .value(UUID.randomUUID().toString())
                .expired(false)
                .createAt(Instant.now())
                .expiration(Instant.now().plusMillis(30 *60 * 1000))
                .build();
        Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get(BEARER))
                .desactive(false)
                .expired(false)
                .utilisateur(utilisateur)
                .refreshToken(refreshToken)
                .build();
        this.jwtRepository.save(jwt);
        jwtMap.put(REFRESH, refreshToken.getValue());
        return jwtMap;
    }

    public Jwt checkTokenValue(String token) {
        return this.jwtRepository.findByValeurAndDesactiveAndExpired(
                token,
                false,
                false
                ).orElseThrow(() -> new RuntimeException("Token not found"));
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

    public void deconnexion() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUtilisateurValidToken(utilisateur.getEmail(),
                        false,
                        false)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        jwt.setDesactive(true);
        jwt.setExpired(true);
        this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "@Daily")
    @Scheduled(cron = "0 10 * * * *")
    public void removeUselessToken() {
        log.info("Suppression des tokens à {}: ", Instant.now());
        this.jwtRepository.deleteAllByExpiredAndDesactive(true,true);
    }

    private Map<String, String> generateJwtToken(Utilisateur utilisateur) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 60 * 1000;
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
        return Map.of(BEARER, bearer);
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

    private void disableTokens(Utilisateur utilisateur) {
        final List<Jwt> jwtList = this.jwtRepository.findByUtilisateur(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setExpired(true);
                    jwt.setDesactive(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }

    public Map<String, String>  refreshToken(Map<String, String> refreshTokenRequest) {
        Jwt jwt = this.jwtRepository.findByRefreshToken(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException("Invalid token"));
        if(jwt.getRefreshToken().getExpired() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException("Invalid token");
        }
        this.disableTokens(jwt.getUtilisateur());
        return this.getJwtToken(jwt.getUtilisateur().getEmail());
    }
}
