package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.PersonDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Person;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PersonRepository;
import com.enterprise.platform.inventorymanagement.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Person服务实现类 - 实现人员相关业务逻辑
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getAllPersons() {
        logger.info("获取所有人员信息");
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> getAllPersons(Pageable pageable) {
        logger.info("分页获取所有人员信息，页码：{}，每页大小：{}", 
                   pageable.getPageNumber(), pageable.getPageSize());
        return personRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> getPersonByCode(String personCode) {
        logger.info("根据人员代码获取人员信息：{}", personCode);
        return personRepository.findById(personCode)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByName(String personName) {
        logger.info("根据人员名称模糊查询：{}", personName);
        return personRepository.findByPersonNameContaining(personName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByDepartment(String departmentCode) {
        logger.info("根据部门代码查询人员：{}", departmentCode);
        return personRepository.findByDepartmentCode(departmentCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> getPersonsByDepartment(String departmentCode, Pageable pageable) {
        logger.info("分页查询部门人员，部门代码：{}，页码：{}", departmentCode, pageable.getPageNumber());
        return personRepository.findByDepartmentCode(departmentCode, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        logger.info("创建新人员：{} - {}", personDTO.getPersonCode(), personDTO.getPersonName());
        
        // 检查人员代码是否已存在
        if (existsPersonCode(personDTO.getPersonCode())) {
            throw new IllegalArgumentException("人员代码已存在：" + personDTO.getPersonCode());
        }
        
        // 检查邮箱是否已存在
        if (personDTO.getPersonEmail() != null && existsEmail(personDTO.getPersonEmail(), null)) {
            throw new IllegalArgumentException("邮箱地址已存在：" + personDTO.getPersonEmail());
        }
        
        // 检查电话是否已存在
        if (personDTO.getPersonPhone() != null && existsPhone(personDTO.getPersonPhone(), null)) {
            throw new IllegalArgumentException("电话号码已存在：" + personDTO.getPersonPhone());
        }
        
        Person person = convertToEntity(personDTO);
        Person savedPerson = personRepository.save(person);
        logger.info("人员创建成功：{} - {}", savedPerson.getPersonCode(), savedPerson.getPersonName());
        
        return convertToDTO(savedPerson);
    }

    @Override
    public PersonDTO updatePerson(String personCode, PersonDTO personDTO) {
        logger.info("更新人员信息：{} - {}", personCode, personDTO.getPersonName());
        
        Person existingPerson = personRepository.findById(personCode)
                .orElseThrow(() -> new IllegalArgumentException("人员不存在：" + personCode));
        
        // 检查邮箱是否被其他人员使用
        if (personDTO.getPersonEmail() != null && 
            existsEmail(personDTO.getPersonEmail(), personCode)) {
            throw new IllegalArgumentException("邮箱地址已被其他人员使用：" + personDTO.getPersonEmail());
        }
        
        // 检查电话是否被其他人员使用
        if (personDTO.getPersonPhone() != null && 
            existsPhone(personDTO.getPersonPhone(), personCode)) {
            throw new IllegalArgumentException("电话号码已被其他人员使用：" + personDTO.getPersonPhone());
        }
        
        // 更新字段
        BeanUtils.copyProperties(personDTO, existingPerson, "personCode", "timestamp");
        
        Person updatedPerson = personRepository.save(existingPerson);
        logger.info("人员更新成功：{} - {}", updatedPerson.getPersonCode(), updatedPerson.getPersonName());
        
        return convertToDTO(updatedPerson);
    }

    @Override
    public boolean deletePerson(String personCode) {
        logger.info("删除人员：{}", personCode);
        
        if (!personRepository.existsById(personCode)) {
            logger.warn("尝试删除不存在的人员：{}", personCode);
            return false;
        }
        
        personRepository.deleteById(personCode);
        logger.info("人员删除成功：{}", personCode);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> searchPersons(String personName, String departmentCode, 
                                        String personProperty, Pageable pageable) {
        logger.info("条件查询人员：名称={}, 部门={}, 属性={}, 页码={}", 
                   personName, departmentCode, personProperty, pageable.getPageNumber());
        return personRepository.findByConditions(personName, departmentCode, personProperty, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getActivePersons() {
        logger.info("获取有效在职人员");
        return personRepository.findActivePersons().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPersonCode(String personCode) {
        return personRepository.existsByPersonCode(personCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEmail(String email, String excludePersonCode) {
        if (excludePersonCode == null) {
            return personRepository.findByPersonEmail(email).isPresent();
        } else {
            return personRepository.existsByPersonEmailAndPersonCodeNot(email, excludePersonCode);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPhone(String phone, String excludePersonCode) {
        if (excludePersonCode == null) {
            return personRepository.findByPersonPhone(phone).isPresent();
        } else {
            return personRepository.existsByPersonPhoneAndPersonCodeNot(phone, excludePersonCode);
        }
    }

    @Override
    public List<PersonDTO> batchCreatePersons(List<PersonDTO> personDTOList) {
        logger.info("批量创建人员，数量：{}", personDTOList.size());
        
        List<Person> persons = personDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        
        List<Person> savedPersons = personRepository.saveAll(persons);
        
        return savedPersons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countPersonsByDepartment(String departmentCode) {
        return personRepository.countByDepartmentCode(departmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByDepartmentCodes(List<String> departmentCodes) {
        logger.info("根据部门代码列表查询人员，部门数量：{}", departmentCodes.size());
        return personRepository.findByDepartmentCodeIn(departmentCodes).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将实体类转换为DTO
     * @param person 实体对象
     * @return DTO对象
     */
    private PersonDTO convertToDTO(Person person) {
        if (person == null) {
            return null;
        }
        
        PersonDTO personDTO = new PersonDTO();
        BeanUtils.copyProperties(person, personDTO, "timestamp");
        
        // 处理字段名映射
        personDTO.setPersonCode(person.getPersonCode());
        personDTO.setPersonName(person.getPersonName());
        personDTO.setDepartmentCode(person.getDepartmentCode());
        personDTO.setPersonProperty(person.getPersonProperty());
        personDTO.setCreditQuantity(person.getCreditQuantity());
        personDTO.setCreateDate(person.getCreateDate());
        personDTO.setCreateGrade(person.getCreateGrade());
        personDTO.setLowRate(person.getLowRate());
        personDTO.setOfferGrade(person.getOfferGrade());
        personDTO.setOfferRate(person.getOfferRate());
        personDTO.setPersonEmail(person.getPersonEmail());
        personDTO.setPersonPhone(person.getPersonPhone());
        personDTO.setValidDate(person.getValidDate());
        personDTO.setInvalidDate(person.getInvalidDate());
        
        return personDTO;
    }

    /**
     * 将DTO转换为实体类
     * @param personDTO DTO对象
     * @return 实体对象
     */
    private Person convertToEntity(PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }
        
        Person person = new Person();
        BeanUtils.copyProperties(personDTO, person);
        
        // 处理字段名映射
        person.setPersonCode(personDTO.getPersonCode());
        person.setPersonName(personDTO.getPersonName());
        person.setDepartmentCode(personDTO.getDepartmentCode());
        person.setPersonProperty(personDTO.getPersonProperty());
        person.setCreditQuantity(personDTO.getCreditQuantity());
        person.setCreateDate(personDTO.getCreateDate());
        person.setCreateGrade(personDTO.getCreateGrade());
        person.setLowRate(personDTO.getLowRate());
        person.setOfferGrade(personDTO.getOfferGrade());
        person.setOfferRate(personDTO.getOfferRate());
        person.setPersonEmail(personDTO.getPersonEmail());
        person.setPersonPhone(personDTO.getPersonPhone());
        person.setValidDate(personDTO.getValidDate());
        person.setInvalidDate(personDTO.getInvalidDate());
        
        return person;
    }
}