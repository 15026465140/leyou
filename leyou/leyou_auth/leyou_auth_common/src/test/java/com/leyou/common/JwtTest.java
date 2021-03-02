package com.leyou.common;

import com.leyou.auth.common.untils.JwtUtils;
import com.leyou.auth.common.untils.RsaUtils;
import com.leyou.auth.common.untils.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\project\\Tools\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\project\\Tools\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @org.junit.jupiter.api.Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYxNDIyMTEyN30.ZsSuAHiul9LEHRDzVxNumBRtT4u_MFKRklmFXv4blUgWAS_AZOWXIJpZ6F-0-P_QidquqhMQN53Wn4YtGHfqICsmYCF6o8sEeiH9MO_0oy5aQKUTJx9NEl9MFwyalDE1eExvsX7iVat37M1uhLjeFSUC-rwKlbz-SYSrbm9ij9o";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}