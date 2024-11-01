package com.ling.controller.admin;


import com.ling.constant.JwtClaimsConstant;
import com.ling.dto.EmployeeDTO;
import com.ling.dto.EmployeeLoginDTO;
import com.ling.dto.EmployeePageQueryDTO;
import com.ling.entity.Employee;
import com.ling.properties.JwtProperties;
import com.ling.result.PageResult;
import com.ling.result.Result;
import com.ling.service.EmployeeService;
import com.ling.utils.JwtUtil;
import com.ling.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping
    public Result save(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("新增员工：{}",employeeLoginDTO);
        employeeService.save(employeeLoginDTO);
        return Result.success();
    }
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }
    @ApiOperation("启用/禁用账号")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
       log.info("启用/禁用账号:{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }
    @ApiOperation("根据id查询员工")
    @GetMapping("/{id}")
    public  Result<Employee> getById(@PathVariable Long id){
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }


}
