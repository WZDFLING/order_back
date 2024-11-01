package com.ling.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ling.constant.MessageConstant;
import com.ling.constant.StatusConstant;
import com.ling.service.DishService;
import com.ling.dto.DishDTO;
import com.ling.dto.DishPageQueryDTO;
import com.ling.entity.Dish;
import com.ling.entity.DishFlavor;
import com.ling.exception.DeletionNotAllowedException;
import com.ling.mapper.DishFlavorMapper;
import com.ling.mapper.DishMapper;
import com.ling.mapper.SetmealDishMapper;
import com.ling.result.PageResult;
import com.ling.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

//        想菜品表中插入一行数据
        dishMapper.insert(dish);
//        获取insert语句生成的主键值
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors !=null &&flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 菜品的批量删除
     * @param ids
     */
    //添加事务注解
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能被删除----是否存在起售中的菜品
        for (Long id:ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //状态值=1，处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否能够删除---是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据
      /*for (Long id : ids){
          dishMapper.deleteById(id);
          //删除菜品关联的口味数据
          dishFlavorMapper.deleteByDishId(id);
      }*/
        /**
         *批量删除id集合--批量删除菜品数据
         */
        dishMapper.deleteByIds(ids);
//        删除口味数据
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查询菜品和对应的口味数据
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
//        根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
//        根据菜品id查询口味数据
        List<DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;

    }

    /**
     * 根据id修改菜品基本信息和对应口味信息
     * @param dishDTO
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
//        修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.update(dish);
        //先删除原来的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
    // 再重新插入口味信息到数据库表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors !=null &&flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品信息
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}
