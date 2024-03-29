package com.tutorial.userservice.controller;

import com.tutorial.userservice.entity.Userx;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Userx>> getAll(){
        List<Userx> users = userService.getAll();
        if(users.isEmpty())
            return ResponseEntity.noContent().build();
        return  ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Userx> getById(@PathVariable("id") int id){
        Userx user = userService.getUserById(id);
        if(user == null)
            return ResponseEntity.notFound().build();
        return  ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Userx> save(@RequestBody Userx user){
        Userx userNew = userService.save(user);
        return  ResponseEntity.ok(userNew);
    }

    // Rest Template
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId){
        Userx user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable("userId") int userId){
        Userx user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Bike> bikes = userService.getBikes(userId);
        return ResponseEntity.ok(bikes);
    }

    // End Rest Template

    // Feign Client

    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        if(userService.getUserById(userId) == null)
                return ResponseEntity.notFound().build();
        Car carNew = userService.saveCar(userId,car);
        return ResponseEntity.ok(car);
    }

    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId) == null)
            return ResponseEntity.notFound().build();
        Bike bikeNew = userService.saveBike(userId,bike);
        return ResponseEntity.ok(bikeNew);
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable("userId") int userId){
        Map<String,Object> result = userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }

    // End Feign Client
}
