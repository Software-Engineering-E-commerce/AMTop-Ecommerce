package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.*;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Services.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/Home")
public class HomePageController {
    private final HomeService homeService;
    //extracting the token from header
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }
    //setup the home page information
    @GetMapping("/startup")
    public ResponseEntity<HomeInfo> startup(@RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = extractToken(authorizationHeader);
            HomeInfo homeInfo = homeService.getHomeInfo(token);
            return ResponseEntity.status(HttpStatus.OK).body(homeInfo);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

    }
//    @GetMapping("/getOffers")
//    public ResponseEntity<List<Offer>> getOffers(@RequestHeader("Authorization") String authorizationHeader){
//        try{
//            String token = extractToken(authorizationHeader);
//            List<Offer>  Offers=//;
//            return ResponseEntity.status(HttpStatus.OK).body(Offers);
//        } catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }
//    //get the categories for either admin or customer
//    @GetMapping("/getCats")
//    public ResponseEntity<List<Category>> getCategories(@RequestHeader("Authorization") String authorizationHeader,boolean isAdmin){
//        try{
//            String token = extractToken(authorizationHeader);
//            List<Category>  Categories=//;
//            return ResponseEntity.status(HttpStatus.OK).body(Categories,isAdmin);//
//        } catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }
//    @GetMapping("/getPopular")
//    public ResponseEntity<List<PopularProd>> getPopular(@RequestHeader("Authorization") String authorizationHeader){
//
//        try{
//            String token = extractToken(authorizationHeader);
//            List<Category>  PopularProducts=//;
//            return ResponseEntity.status(HttpStatus.OK).body(PopularProducts);
//        } catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }
//    @GetMapping("/getLatest")
//    public ResponseEntity<List<LatestProd>> getLatest(@RequestHeader("Authorization") String authorizationHeader){
//        try{
//            String token = extractToken(authorizationHeader);
//            List<Category>  LatestProducts=//;
//            return ResponseEntity.status(HttpStatus.OK).body(LatestProducts);
//        } catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }
}
