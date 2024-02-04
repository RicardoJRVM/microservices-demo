package com.tutorial.userservice.controller;

import com.tutorial.userservice.entity.Userx;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId){
        Userx user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallbackGetBikes")
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

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackSaveCar")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        if(userService.getUserById(userId) == null)
                return ResponseEntity.notFound().build();
        Car carNew = userService.saveCar(userId,car);
        return ResponseEntity.ok(car);
    }

    @CircuitBreaker(name = "bikeCB", fallbackMethod = "fallbackSaveBike")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId) == null)
            return ResponseEntity.notFound().build();
        Bike bikeNew = userService.saveBike(userId,bike);
        return ResponseEntity.ok(bikeNew);
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallbackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable("userId") int userId){
        Map<String,Object> result = userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }

    // End Feign Client

    // Circuit brakers
    private ResponseEntity<List<Car>> fallbackGetCars(@PathVariable("userId") int userId , RuntimeException e){
       return new ResponseEntity("El usuario "+userId+ " tiene los coches en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Car> fallbackSaveCar(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e){
        return new ResponseEntity("El usuario "+userId+ " no tiene dinero para coches", HttpStatus.OK);
    }

    private ResponseEntity<List<Bike>> fallbackGetBikes(@PathVariable("userId") int userId , RuntimeException e){
        return new ResponseEntity("El usuario "+userId+ " tiene las motos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Bike> fallbackSaveBike(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException e){
        return new ResponseEntity("El usuario "+userId+ " no tiene dinero para motos", HttpStatus.OK);
    }

    private ResponseEntity<Map<String,Object>> fallbackGetAll(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("El usuario "+userId+ " tiene los vehiculos en el taller", HttpStatus.OK);
    }

}
