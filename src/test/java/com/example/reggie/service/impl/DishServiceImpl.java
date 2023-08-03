package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import com.example.reggie.mapper.DishMapper;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * the operation related with two tables,
     * so we add @Transactional annotation.
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //save the basic dish information
        this.save(dishDto);

        Long dishId = dishDto.getId();//get the dish id

        //set the dish id to the flavor list
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //save the flavors data to the table dishFlavor
        dishFlavorService.saveBatch(flavors);
    }


    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //Update dish table
        this.updateById(dishDto);

        //Clear the flavor data corresponding to the current dish
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //delete operation to the table dish_flavor
        dishFlavorService.remove(queryWrapper);

        //Add the current submitted flavor data
        //dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
