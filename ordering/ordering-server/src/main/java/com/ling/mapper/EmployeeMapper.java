package com.ling.mapper;

import com.github.pagehelper.Page;
import com.ling.annotation.AutoFill;
import com.ling.enumeration.OperationType;
import com.ling.dto.EmployeePageQueryDTO;
import com.ling.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    /**
 * 根据用户名查询员工
 * @param username
 * @return
 */
    @Select("select *from employee where username=#{username}")
    Employee getByUsername(String username);

    /*
    新增员工
    */
    @Insert("insert into  employee(name,username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status)" +
            "values(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser},#{status})")
    @AutoFill(value = OperationType.INSERT)//公共字段填充
    void insert(Employee employee);

  /* 员工分页查询
   * */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    Employee getById(Long id);
}