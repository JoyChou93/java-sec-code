package org.joychou.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtils {

    private static final long EXPIRE = 1440 * 60 * 1000;  // 1440 Minutes, 1 DAY
    private static final String SECRET = "123456";
    private static final String B64_SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Generate JWT Token by jjwt (last update time: Jul 05, 2018)
     *
     * @author JoyChou 2022-09-20
     * @param userId userid
     * @return token
     */
    public static String generateTokenByJjwt(String userId) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")   // header
                .setHeaderParam("alg", "HS256")     // header
                .setIssuedAt(new Date())    // token发布时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   // token过期时间
                .claim("userid", userId)
                // secret在signWith会base64解码，但网上很多代码示例并没对secret做base64编码，所以在爆破key的时候可以注意下。
                .signWith(SignatureAlgorithm.HS256, B64_SECRET)
                .compact();
    }

    public static String getUserIdFromJjwtToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(B64_SECRET).parseClaimsJws(token).getBody();
            return (String)claims.get("userid");
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Generate jwt token by java-jwt.
     *
     * @author JoyChou 2022-09-20
     * @param nickname nickname
     * @return jwt token
     */
    public static String generateTokenByJavaJwt(String nickname) {
        return JWT.create()
                .withClaim("nickname", nickname)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(SECRET));
    }


    /**
     * Verify JWT Token
     * @param token token
     * @return Valid token returns true. Invalid token returns false.
     */
    public static Boolean verifyTokenByJavaJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            log.error(exception.toString());
            return false;
        }
    }


    public static String getNicknameByJavaJwt(String token) {
        // If the signature is not verified, there will be security issues.
        if (!verifyTokenByJavaJwt(token)) {
            log.error("token is invalid");
            return null;
        }
        return JWT.decode(token).getClaim("nickname").asString();
    }


    public static void main(String[] args) {
        String jjwtToken = generateTokenByJjwt("10000");
        System.out.println(jjwtToken);
        System.out.println(getUserIdFromJjwtToken(jjwtToken));

        String token = generateTokenByJavaJwt("JoyChou");
        System.out.println(token);
        System.out.println(getNicknameByJavaJwt(token));
    }
}
