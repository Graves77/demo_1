//package com.coding.demo.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.checkerframework.checker.units.qual.C;
//import org.junit.Test;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Calendar;
//import java.util.Date;
//
//
//public class G_JwtUtils {
//    public  static  final long EXPIRE = 1000*60*60*24;
//    public static final String APP_SECRET="ukc8BDbRigUDaY6pZFfWus2jZWLPHO";
//    /**
//     * 根据用户id和昵称生成token
//     * @param id  用户id
//     * @param nickname 用户昵称
//     * @return JWT规则生成的token
//     */
//    public static String getJwtToken(String id,String nickname){
//        String JwtToken = Jwts.builder()
//                .setHeaderParam("typ","JWT")
//                .setHeaderParam("alg","HS256")
//                .setSubject("user")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
//                .claim("id",id)
//                .claim("nickname",nickname)
//                .signWith(SignatureAlgorithm.ES256,APP_SECRET)
//                .compact();
//        return  JwtToken;
//    }
//    /**
//     * 判断token是否存在与有效
//     * @param jwtToken token字符串
//     * @return 如果token有效返回true，否则返回false
//     */
//    public static boolean checkToken(String jwtToken){
//        if(StringUtils.isEmpty(jwtToken))return false;
//        try{
//            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//    /**
//     * 判断token是否存在与有效
//     * @param request Http请求对象
//     * @return 如果token有效返回true，否则返回false
//     */
//    public static boolean checkToken(HttpServletRequest request){
//        try{
//            String jwtToken=request.getHeader("token");
//            if(StringUtils.isEmpty(jwtToken))return false;
//            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//    /**
//     * 根据token获取会员id
//     * @param request Http请求对象
//     * @return 解析token后获得的用户id
//     */
//    public static String getMemberIdJwtToken(HttpServletRequest request){
//        String jwtToken=request.getHeader("token");
//        if(StringUtils.isEmpty(jwtToken))return "";
//        Jws<Claims> claimsJws=Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
//        Claims claims = claimsJws.getBody();
//        return (String) claims.get("id");
//    }
//}
