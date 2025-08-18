package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.PersonDTO;
import com.enterprise.platform.inventorymanagement.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Person控制器 - 提供RESTful API接口
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/inventory/persons")
@Validated
@CrossOrigin(origins = "*")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    /**
     * 获取所有人员列表
     * @return 人员列表
     */
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        logger.info("REST请求：获取所有人员列表");
        List<PersonDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    /**
     * 分页获取所有人员
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向（asc/desc）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<PersonDTO>> getPersonsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "personCode") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        logger.info("REST请求：分页获取人员信息，页码：{}，每页大小：{}，排序：{} {}", 
                   page, size, sort, direction);
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<PersonDTO> personsPage = personService.getAllPersons(pageable);
        return ResponseEntity.ok(personsPage);
    }

    /**
     * 根据ID获取人员信息
     * @param personCode 人员代码
     * @return 人员信息
     */
    @GetMapping("/{personCode}")
    public ResponseEntity<PersonDTO> getPersonByCode(
            @PathVariable @NotBlank @Size(max = 20) String personCode) {
        
        logger.info("REST请求：根据代码获取人员信息：{}", personCode);
        Optional<PersonDTO> personDTO = personService.getPersonByCode(personCode);
        
        return personDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据人员名称模糊查询
     * @param personName 人员名称
     * @return 人员列表
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<PersonDTO>> getPersonsByName(
            @RequestParam @NotBlank String personName) {
        
        logger.info("REST请求：根据名称模糊查询人员：{}", personName);
        List<PersonDTO> persons = personService.getPersonsByName(personName);
        return ResponseEntity.ok(persons);
    }

    /**
     * 根据部门代码查询人员
     * @param departmentCode 部门代码
     * @param page 页码
     * @param size 每页大小
     * @return 人员列表或分页结果
     */
    @GetMapping("/department/{departmentCode}")
    public ResponseEntity<?> getPersonsByDepartment(
            @PathVariable @NotBlank @Size(max = 12) String departmentCode,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        logger.info("REST请求：根据部门代码查询人员：{}，分页：{}", departmentCode, page != null);
        
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<PersonDTO> personsPage = personService.getPersonsByDepartment(departmentCode, pageable);
            return ResponseEntity.ok(personsPage);
        } else {
            List<PersonDTO> persons = personService.getPersonsByDepartment(departmentCode);
            return ResponseEntity.ok(persons);
        }
    }

    /**
     * 创建新人员
     * @param personDTO 人员信息
     * @return 创建成功的人员
     */
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        logger.info("REST请求：创建新人员：{} - {}", personDTO.getPersonCode(), personDTO.getPersonName());
        
        try {
            PersonDTO createdPerson = personService.createPerson(personDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
        } catch (IllegalArgumentException e) {
            logger.error("创建人员失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 更新人员信息
     * @param personCode 人员代码
     * @param personDTO 更新的人员信息
     * @return 更新成功的人员
     */
    @PutMapping("/{personCode}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable @NotBlank @Size(max = 20) String personCode,
            @Valid @RequestBody PersonDTO personDTO) {
        
        logger.info("REST请求：更新人员信息：{} - {}", personCode, personDTO.getPersonName());
        
        if (!personCode.equals(personDTO.getPersonCode())) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            PersonDTO updatedPerson = personService.updatePerson(personCode, personDTO);
            return ResponseEntity.ok(updatedPerson);
        } catch (IllegalArgumentException e) {
            logger.error("更新人员失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除人员
     * @param personCode 人员代码
     * @return 删除结果
     */
    @DeleteMapping("/{personCode}")
    public ResponseEntity<Void> deletePerson(
            @PathVariable @NotBlank @Size(max = 20) String personCode) {
        
        logger.info("REST请求：删除人员：{}", personCode);
        
        boolean deleted = personService.deletePerson(personCode);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 条件查询人员
     * @param personName 人员名称（模糊匹配）
     * @param departmentCode 部门代码
     * @param personProperty 人员属性
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PersonDTO>> searchPersons(
            @RequestParam(required = false) String personName,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) String personProperty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        logger.info("REST请求：条件查询人员，名称={}, 部门={}, 属性={}, 页码={}", 
                   personName, departmentCode, personProperty, page);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PersonDTO> personsPage = personService.searchPersons(
                personName, departmentCode, personProperty, pageable);
        
        return ResponseEntity.ok(personsPage);
    }

    /**
     * 获取有效在职人员
     * @return 有效人员列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<PersonDTO>> getActivePersons() {
        logger.info("REST请求：获取有效在职人员");
        List<PersonDTO> activePersons = personService.getActivePersons();
        return ResponseEntity.ok(activePersons);
    }

    /**
     * 检查人员代码是否存在
     * @param personCode 人员代码
     * @return 检查结果
     */
    @GetMapping("/exists/{personCode}")
    public ResponseEntity<Map<String, Boolean>> existsPersonCode(
            @PathVariable @NotBlank @Size(max = 20) String personCode) {
        
        logger.info("REST请求：检查人员代码是否存在：{}", personCode);
        boolean exists = personService.existsPersonCode(personCode);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱地址
     * @param excludePersonCode 排除的人员代码
     * @return 检查结果
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(
            @RequestParam String email,
            @RequestParam(required = false) String excludePersonCode) {
        
        logger.info("REST请求：检查邮箱是否已存在：{}，排除：{}", email, excludePersonCode);
        boolean exists = personService.existsEmail(email, excludePersonCode);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 检查电话是否已存在
     * @param phone 电话号码
     * @param excludePersonCode 排除的人员代码
     * @return 检查结果
     */
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExists(
            @RequestParam String phone,
            @RequestParam(required = false) String excludePersonCode) {
        
        logger.info("REST请求：检查电话是否已存在：{}，排除：{}", phone, excludePersonCode);
        boolean exists = personService.existsPhone(phone, excludePersonCode);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 批量创建人员
     * @param personDTOList 人员列表
     * @return 创建成功的人员列表
     */
    @PostMapping("/batch")
    public ResponseEntity<List<PersonDTO>> batchCreatePersons(
            @Valid @RequestBody List<PersonDTO> personDTOList) {
        
        logger.info("REST请求：批量创建人员，数量：{}", personDTOList.size());
        
        try {
            List<PersonDTO> createdPersons = personService.batchCreatePersons(personDTOList);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersons);
        } catch (IllegalArgumentException e) {
            logger.error("批量创建人员失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 统计部门人数
     * @param departmentCode 部门代码
     * @return 人员数量
     */
    @GetMapping("/count/department/{departmentCode}")
    public ResponseEntity<Map<String, Long>> countPersonsByDepartment(
            @PathVariable @NotBlank @Size(max = 12) String departmentCode) {
        
        logger.info("REST请求：统计部门人数：{}", departmentCode);
        long count = personService.countPersonsByDepartment(departmentCode);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 根据部门代码列表查询人员
     * @param departmentCodes 部门代码列表
     * @return 人员列表
     */
    @GetMapping("/departments")
    public ResponseEntity<List<PersonDTO>> getPersonsByDepartmentCodes(
            @RequestParam List<String> departmentCodes) {
        
        logger.info("REST请求：根据部门代码列表查询人员，部门数量：{}", departmentCodes.size());
        List<PersonDTO> persons = personService.getPersonsByDepartmentCodes(departmentCodes);
        return ResponseEntity.ok(persons);
    }
}