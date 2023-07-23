package com.example.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * self-defined data object processor
 * a feature offered by mybatis-plus, autofill some data when insert or update
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * insert, automatic fill
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("public fields are automatically filled [insert]...");
        log.info(metaObject.toString());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //set current user by BaseContext (threadLocal)
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     * update, automatic fill
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("public fields automatically filled[update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("threadId is {}",id);
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
