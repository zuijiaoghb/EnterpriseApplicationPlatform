package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Person服务接口 - 定义人员相关业务逻辑
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
public interface PersonService {

    /**
     * 获取所有人员列表
     * @return 人员列表
     */
    List<PersonDTO> getAllPersons();

    /**
     * 分页获取所有人员
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PersonDTO> getAllPersons(Pageable pageable);

    /**
     * 根据ID获取人员信息
     * @param personCode 人员代码
     * @return 人员DTO对象
     */
    Optional<PersonDTO> getPersonByCode(String personCode);

    /**
     * 根据人员名称模糊查询
     * @param personName 人员名称
     * @return 人员列表
     */
    List<PersonDTO> getPersonsByName(String personName);

    /**
     * 根据部门代码查询人员
     * @param departmentCode 部门代码
     * @return 人员列表
     */
    List<PersonDTO> getPersonsByDepartment(String departmentCode);

    /**
     * 根据部门代码分页查询人员
     * @param departmentCode 部门代码
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PersonDTO> getPersonsByDepartment(String departmentCode, Pageable pageable);

    /**
     * 创建新人员
     * @param personDTO 人员DTO对象
     * @return 创建成功的人员DTO
     */
    PersonDTO createPerson(PersonDTO personDTO);

    /**
     * 更新人员信息
     * @param personCode 人员代码
     * @param personDTO 更新的人员DTO对象
     * @return 更新成功的人员DTO
     */
    PersonDTO updatePerson(String personCode, PersonDTO personDTO);

    /**
     * 删除人员
     * @param personCode 人员代码
     * @return 是否删除成功
     */
    boolean deletePerson(String personCode);

    /**
     * 根据条件查询人员
     * @param personName 人员名称（模糊匹配）
     * @param departmentCode 部门代码
     * @param personProperty 人员属性
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PersonDTO> searchPersons(String personName, String departmentCode, String personProperty, Pageable pageable);

    /**
     * 获取有效在职人员
     * @return 有效人员列表
     */
    List<PersonDTO> getActivePersons();

    /**
     * 检查人员代码是否存在
     * @param personCode 人员代码
     * @return 是否存在
     */
    boolean existsPersonCode(String personCode);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱地址
     * @param excludePersonCode 排除的人员代码
     * @return 是否存在
     */
    boolean existsEmail(String email, String excludePersonCode);

    /**
     * 检查电话是否已存在
     * @param phone 电话号码
     * @param excludePersonCode 排除的人员代码
     * @return 是否存在
     */
    boolean existsPhone(String phone, String excludePersonCode);

    /**
     * 批量创建人员
     * @param personDTOList 人员DTO列表
     * @return 创建成功的人员列表
     */
    List<PersonDTO> batchCreatePersons(List<PersonDTO> personDTOList);

    /**
     * 统计部门人数
     * @param departmentCode 部门代码
     * @return 该部门的人员数量
     */
    long countPersonsByDepartment(String departmentCode);

    /**
     * 根据部门代码列表查询人员
     * @param departmentCodes 部门代码列表
     * @return 人员列表
     */
    List<PersonDTO> getPersonsByDepartmentCodes(List<String> departmentCodes);
}