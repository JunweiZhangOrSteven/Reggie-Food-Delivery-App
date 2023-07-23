package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.reggie.common.CustomException;
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //Query if the current category is associated with dish
        LambdaQueryWrapper<Dish> dishLanmdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLanmdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLanmdaQueryWrapper);
        if(count1 > 0){
            throw new CustomException("the current category is associated with dish, and can't be delete");
        }
        //Query if the current category is associated with setmeal
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if(count2 > 0){
            throw new CustomException("current category is associated with dish, and can't be delete");
        }
        //delete category
        super.removeById(id);
    }
}
