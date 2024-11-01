package com.ling.service;

import com.ling.dto.DishDTO;
import com.ling.dto.DishPageQueryDTO;
import com.ling.entity.Dish;
import com.ling.result.PageResult;
import com.ling.vo.DishVO;

import java.util.List;

public interface DishService {
//   新增菜品和对应的口味
   public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

 /**
  * 根据id查询菜品数据
  * @param id
  * @return
  */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 根据id修改菜品基本信息
     * @param dishDTO
     */
 void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
