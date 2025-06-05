package com.enterprise.platform.inventorymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.service.HYBarCodeMainService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hy-barcode-main")
public class HYBarCodeMainController {
    @Autowired
    private HYBarCodeMainService service;

    @GetMapping
    public ResponseEntity<List<HYBarCodeMain>> getAllBarCodeMains() {
        List<HYBarCodeMain> barCodeMains = service.getAllBarCodeMains();
        return new ResponseEntity<>(barCodeMains, HttpStatus.OK);
    }

    @GetMapping("/{barCode}")
    public ResponseEntity<HYBarCodeMain> getBarCodeMainById(@PathVariable String barCode) {
        Optional<HYBarCodeMain> barCodeMain = service.getBarCodeMainById(barCode);
        return barCodeMain.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<HYBarCodeMain> saveBarCodeMain(@RequestBody HYBarCodeMain barCodeMain) {
        HYBarCodeMain savedBarCodeMain = service.saveBarCodeMain(barCodeMain);
        return new ResponseEntity<>(savedBarCodeMain, HttpStatus.CREATED);
    }

    @DeleteMapping("/{barCode}")
    public ResponseEntity<Void> deleteBarCodeMain(@PathVariable String barCode) {
        service.deleteBarCodeMain(barCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}