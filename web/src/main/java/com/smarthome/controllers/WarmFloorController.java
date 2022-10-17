package com.smarthome.controllers;


import com.smarthome.dtos.WarmFloorConfigData;
import com.smarthome.dtos.WarmFloorData;
import com.smarthome.dtos.WarmFloorThresholdData;
import com.smarthome.mappers.WarmFloorConfigDataMapper;
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
@RequestMapping("/api/warm-floor")
public class WarmFloorController {

    @Autowired
    public WarmFloorService warmFloorService;
    @Autowired
    private WarmFloorConfigDataMapper warmFloorConfigDataMapper;

    @GetMapping
    public List<WarmFloorData> getAllWarmFloors() {
        return warmFloorService.getAllWarmFloors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public WarmFloorData createWarmFloorConfiguration(@RequestBody WarmFloorConfigData configuration) {
        WarmFloorConfig config = warmFloorConfigDataMapper.map(configuration);
        return warmFloorService.createWarmFloorConfiguration(config);
    }

    @PutMapping("/{id}/toggle")
    @ResponseStatus(HttpStatus.OK)
    public void toggleWarmFloor(@PathVariable long id) {
        warmFloorService.toggle(id);
    }

    @PutMapping("/{id}/threshold")
    @ResponseStatus(HttpStatus.OK)
    public void setNewThreshold(@PathVariable long id, @RequestBody WarmFloorThresholdData thresholdData) {
        warmFloorService.setNewThreshold(id, thresholdData.getNewThreshold());
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        warmFloorService.remove(id);
    }
}
