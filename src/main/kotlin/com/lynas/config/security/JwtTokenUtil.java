package com.lynas.config.security;


import com.lynas.model.Organization;
import com.lynas.model.OrganizationInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private String CLAIM_KEY_USERNAME = "sub";
    private String CLAIM_KEY_ORD_ID = "org_id";
    private String CLAIM_KEY_ORD_name = "org_name";
    private String CLAIM_KEY_AUDIENCE = "audience";
    private String CLAIM_KEY_CREATED = "created";
    private String CLAIM_KEY_EXPIRED = "exp";
    private String CLAIM_KEY_ROLE = "rlu";

    private String AUDIENCE_UNKNOWN = "unknown";
    private String AUDIENCE_WEB = "web";
    private String AUDIENCE_MOBILE = "mobile";
    private String AUDIENCE_TABLET = "tablet";


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        if (token == null) {
            return null;
        }
        String username;
        try {
            Claims claimsFromToken = getClaimsFromToken(token);
            username = claimsFromToken.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Organization getOrganizationFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        Long orgId = Long.parseLong("" + claimsFromToken.get(CLAIM_KEY_ORD_ID));
        String orgName = (String) claimsFromToken.get(CLAIM_KEY_ORD_name);
        return new Organization(orgId, orgName, 0, new OrganizationInfo(null, "", ""));
    }

    public String getUserRoleFromToken(String token) {
        String auth;
        try {
            Claims claimsFromToken = getClaimsFromToken(token);
            auth = (String) claimsFromToken.get(CLAIM_KEY_ROLE);
        } catch (Exception e) {
            auth = null;
        }
        return auth;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }


    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }


    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    Boolean isTokenExpired(String token) {
        final Date expirationTime = this.getExpirationDateFromToken(token);
        return expirationTime == null || expirationTime.before(this.generateCurrentDate());
    }

    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }


    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = this.getAudienceFromToken(token);
        return (this.AUDIENCE_TABLET.equals(audience) || this.AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(UserDetails userDetails, Device device, Organization org) {
        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        HashMap<String, Object> claims = new HashMap<>();
        long now = new Date().getTime();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_ORD_ID, org.getId());
        claims.put(CLAIM_KEY_ORD_name, org.getName());
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_EXPIRED, now + 172800000);
        claims.put(CLAIM_KEY_CREATED, now);
        claims.put(CLAIM_KEY_ROLE, String.join(",", authorities));
        return doGenerateToken(claims);
    }


    private String doGenerateToken(Map<String, Object> claims) {
        long now = new Date().getTime();
        Date expirationDate = new Date(now + (expiration * 1000));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = this.getCreatedDateFromToken(token);
        return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
                && (!(this.isTokenExpired(token)) || this.ignoreTokenExpiration(token)));
    }


    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, this.generateCurrentDate());
            refreshedToken = this.doGenerateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

}
























