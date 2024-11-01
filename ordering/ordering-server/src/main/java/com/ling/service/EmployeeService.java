package com.ling.service;

import com.ling.dto.EmployeeDTO;
import com.ling.dto.EmployeeLoginDTO;
import com.ling.dto.EmployeePageQueryDTO;
import com.ling.entity.Employee;
import com.ling.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    /**
     * 员工添加
     * @param employeeLoginDTO
     * @return
     */
    void save(EmployeeLoginDTO employeeLoginDTO);

//    员工分页查询
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    //启用/禁用员工账号
     void startOrStop(Integer status, Long id);
//根据id查询员工
    Employee getById(Long id);

//编辑员工信息
    void update(EmployeeDTO employeeDTO);
}
