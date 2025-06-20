package com.enterprise.platform.inventorymanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.service.HYBarCodeMainService;

import java.util.Optional;

@RestController
@RequestMapping("/api/hy-barcode-main")
public class HYBarCodeMainController {
    private static final Logger logger = LoggerFactory.getLogger(HYBarCodeMainController.class);

    @Autowired
    private HYBarCodeMainService service;

    @GetMapping
    public ResponseEntity<Page<HYBarCodeMain>> getAllBarCodeMains(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        // 添加日志记录实际接收到的参数
        logger.info("Received pagination params - page: {}, size: {}", page, size);

        Page<HYBarCodeMain> barCodeMains = service.getAllBarCodeMains(PageRequest.of(page, size));
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

}