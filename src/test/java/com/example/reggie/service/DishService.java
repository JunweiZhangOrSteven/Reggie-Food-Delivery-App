package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //To add a new dish and insert the corresponding flavor data of the dish,
    //we need to operate two tables：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //Query dish and flavor information by the dish id
    public DishDto getByIdWithFlavor(Long id);

    //Update the dish information and
    //also update the corresponding flavor information
    public void updateWithFlavor(DishDto dishDto);

}
