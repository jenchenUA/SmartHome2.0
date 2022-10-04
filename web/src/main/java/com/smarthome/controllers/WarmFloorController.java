package com.smarthome.controllers;


import com.smarthome.dtos.WarmFloorData;
import com.smarthome.services.WarmFloorService;
import com.smarthome.warmfloor.WarmFloorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warm-floor")
public class WarmFloorController {

    @Autowired
    public WarmFloorService warmFloorService;

    @GetMapping
    public List<WarmFloorData> getAllWarmFloors() {
        return warmFloorService.getAllWarmFloors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createWarmFloorConfiguration(@RequestBody WarmFloorConfig configuration) {
        warmFloorService.createWarmFloorConfiguration(configuration);
    }

    @PutMapping("/{id}/toggle")
    @ResponseStatus(HttpStatus.OK)
    public void toggleWarmFloor(@PathVariable long id) {
        warmFloorService.toggle(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        warmFloorService.remove(id);
    }
}
