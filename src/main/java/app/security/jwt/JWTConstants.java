package app.security.jwt;

public final class JWTConstants
{
    public static final String AUTH_LOGIN_URL = "/api/login";

    public static final String JWT_SECRET = "?D(G+KbPeShVmYq3t6w9y$B&E)H@McQfTjWnZr4u7x!A%C*F-JaNdRgUkXp2s5v8";

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
}

