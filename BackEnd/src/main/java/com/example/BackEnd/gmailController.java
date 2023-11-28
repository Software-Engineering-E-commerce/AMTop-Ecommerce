package com.example.BackEnd;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class gmailController {

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        try {
            // Decode the token (without verifying its signature - for demonstration purposes only!)
            DecodedJWT jwt = JWT.decode(token);

            String email = jwt.getClaim("email").asString(); // replace with actual claim name
            String firstName = jwt.getClaim("given_name").asString();
            String familyName = jwt.getClaim("family_name").asString();

            return ResponseEntity.ok("Token information extracted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
    public static ResponseEntity<?> authenticateDirectly(String token) {
        return processToken(token);
    }

    // Extracted common logic into a separate method
    private static ResponseEntity<?> processToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String email = jwt.getClaim("email").asString();
            String firstName = jwt.getClaim("given_name").asString();  // Extracting first name
            String familyName = jwt.getClaim("family_name").asString(); // Extracting family name

            System.out.println("Email: " + email);
            System.out.println("First Name: " + firstName);
            System.out.println("Family Name: " + familyName);

            return ResponseEntity.ok("Token information extracted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

    public static void main(String[] args) {
        String sampleToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBlNzJkYTFkZjUwMWNhNmY3NTZiZjEwM2ZkN2M3MjAyOTQ3NzI1MDYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMjg3MDc0ODU3MDYtZTMycTkyNzFvamVjaWdjMWF0a2dldDZtNHIyNGN1dXEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxMjg3MDc0ODU3MDYtZTMycTkyNzFvamVjaWdjMWF0a2dldDZtNHIyNGN1dXEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTgwNzI4OTM3NzY2NTIzMjQyNjAiLCJlbWFpbCI6Im14MjI3MDQ0QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYmYiOjE3MDExNDI3MDAsIm5hbWUiOiJNb2hhbWVkIFgiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSUhHU29aWjFKZS1YV2RmenkyMUJKOGRQUzYxSDJiUUF6LW9FNkc4M0pzPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6Ik1vaGFtZWQiLCJmYW1pbHlfbmFtZSI6IlgiLCJsb2NhbGUiOiJlbiIsImlhdCI6MTcwMTE0MzAwMCwiZXhwIjoxNzAxMTQ2NjAwLCJqdGkiOiIwMTZhOTY2MjA3OGE0NTQ1M2FmMTk4Y2QzNzUzNjJlOWM4MWNiNzM4In0.g1NTnZgmAw6UckLh8Cy219KY5UFmv_zXet0IbzMj5s_bHl2kne_x2NobxqS4PwIsceQbngibwlS8C0MEeQP3JRbOp48R0_37y5OCZtGs8svj7cwfWToIzkV1_iedjI8aLtbovyQtlyQ4Ac29wmunDPeFGEwI_6MbDFKDyO8owC_0R4eoBKGQ8W8bI6Rd1Qt9PIjZsTZIpKpCTYTl54WdomNWzJsEJ1xnhhjjxikOWqo31ScbyuF42zh_KLfNYke1BLvnF1X25om4CuSSMRHoj1GIDJT1ugzzWHUKl3MpMbk0DW4uji4YBEErchiz7s35DDOEIvKvc6WgyZzsyNo4gw";

        ResponseEntity<?> response = authenticateDirectly(sampleToken);
        System.out.println(response.getBody());
    }
}
