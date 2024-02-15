package io.kenji.seckill.common.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.kenji.seckill.common.constants.SeckillConstants;

import java.util.Date;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/26
 **/
public class JwtUtils {

    /**
     * Verify token if it is correct
     *
     * @param token
     * @param secret
     * @return
     */
    public static boolean verify(String token, String secret) {
        try {
            //Generate JWT verifier according secret
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(SeckillConstants.TOKEN_CLAM, getUserId(token))
                    .build();
            // verify token
            verifier.verify(token);
            return true;
        } catch (JWTDecodeException exception) {
            return false;
        }
    }

    /**
     * Get token info with secret
     *
     * @param token userId
     * @return
     */
    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SeckillConstants.TOKEN_CLAM).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * Sign token
     *
     * @param userId userId
     * @param secret sign token secret
     * @return encode token
     */
    public static String sign(Long userId, String secret) {
        Date date = new Date(System.currentTimeMillis() + SeckillConstants.TOKEN_EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim(SeckillConstants.TOKEN_CLAM, userId)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * Sign token
     *
     * @param userId userId
     * @return encode token
     */
    public static String sign(Long userId) {
        return sign(userId, SeckillConstants.JWT_SECRET);
    }

}
