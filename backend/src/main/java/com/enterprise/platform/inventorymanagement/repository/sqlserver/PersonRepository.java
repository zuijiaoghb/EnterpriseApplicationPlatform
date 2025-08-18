package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Person数据访问层 - 继承JpaRepository提供基础CRUD操作
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    /**
     * 根据人员名称模糊查询
     * @param personName 人员名称（模糊匹配）
     * @return 人员列表
     */
    List<Person> findByPersonNameContaining(String personName);

    /**
     * 根据部门代码查询人员
     * @param departmentCode 部门代码
     * @return 人员列表
     */
    List<Person> findByDepartmentCode(String departmentCode);

    /**
     * 根据部门代码分页查询人员
     * @param departmentCode 部门代码
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<Person> findByDepartmentCode(String departmentCode, Pageable pageable);

    /**
     * 根据人员代码查询（忽略大小写）
     * @param personCode 人员代码
     * @return Optional包装的人员对象
     */
    Optional<Person> findByPersonCodeIgnoreCase(String personCode);
    
    /**
     * 根据多个人员代码批量查询
     * @param personCodes 人员代码集合
     * @return 人员列表
     */
    List<Person> findByPersonCodeIn(Collection<String> personCodes);

    /**
     * 根据人员名称和部门代码查询
     * @param personName 人员名称
     * @param departmentCode 部门代码
     * @return 人员列表
     */
    List<Person> findByPersonNameAndDepartmentCode(String personName, String departmentCode);

    /**
     * 根据邮箱查询人员
     * @param email 邮箱地址
     * @return Optional包装的人员对象
     */
    Optional<Person> findByPersonEmail(String email);

    /**
     * 根据电话号码查询人员
     * @param phone 电话号码
     * @return Optional包装的人员对象
     */
    Optional<Person> findByPersonPhone(String phone);

    /**
     * 查询有效的在职人员（validDate <= 当前时间 <= invalidDate）
     * @return 有效人员列表
     */
    @Query("SELECT p FROM Person p WHERE p.validDate <= CURRENT_TIMESTAMP AND (p.invalidDate IS NULL OR p.invalidDate >= CURRENT_TIMESTAMP)")
    List<Person> findActivePersons();

    /**
     * 根据人员属性查询
     * @param personProperty 人员属性
     * @return 人员列表
     */
    List<Person> findByPersonProperty(String personProperty);

    /**
     * 自定义查询：根据多个条件组合查询
     * @param personName 人员名称（模糊匹配）
     * @param departmentCode 部门代码
     * @param personProperty 人员属性
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT p FROM Person p WHERE " +
           "(:personName IS NULL OR p.personName LIKE %:personName%) AND " +
           "(:departmentCode IS NULL OR p.departmentCode = :departmentCode) AND " +
           "(:personProperty IS NULL OR p.personProperty = :personProperty)")
    Page<Person> findByConditions(@Param("personName") String personName,
                                  @Param("departmentCode") String departmentCode,
                                  @Param("personProperty") String personProperty,
                                  Pageable pageable);

    /**
     * 统计部门人数
     * @param departmentCode 部门代码
     * @return 该部门的人员数量
     */
    long countByDepartmentCode(String departmentCode);

    /**
     * 根据部门代码列表查询人员
     * @param departmentCodes 部门代码列表
     * @return 人员列表
     */
    List<Person> findByDepartmentCodeIn(List<String> departmentCodes);

    /**
     * 检查人员代码是否存在
     * @param personCode 人员代码
     * @return 是否存在
     */
    boolean existsByPersonCode(String personCode);

    /**
     * 检查邮箱是否已存在（排除当前人员）
     * @param email 邮箱地址
     * @param personCode 当前人员代码
     * @return 是否存在
     */
    boolean existsByPersonEmailAndPersonCodeNot(String email, String personCode);

    /**
     * 检查电话是否已存在（排除当前人员）
     * @param phone 电话号码
     * @param personCode 当前人员代码
     * @return 是否存在
     */
    boolean existsByPersonPhoneAndPersonCodeNot(String phone, String personCode);
}