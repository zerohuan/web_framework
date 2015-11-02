package com.yjh.cg.site.controller;

import com.yjh.base.exception.BEnumError;
import com.yjh.base.exception.BSystemException;
import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * User logic controller
 *
 * Created by yjh on 15-9-20.
 */
@Controller
@RequestMapping("user")
public class UserController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @RequestMapping(value="sample/{userId}", produces = "text/html")
    @ResponseBody
    public String sample(@PathVariable("userId") long userId,
                         @RequestParam(value = "name", defaultValue = "YJH", required = false) String name) {

        return "success<br />" + userId + " " + "name: " + name;
    }

    @RequestMapping(value="info/{id}", method = RequestMethod.GET)
    public String useInfo(@PathVariable(value = "id") BUserEntity userEntity,
                               Map<String, Object> model) {
        if(userEntity == null)
            throw new RuntimeException();
        model.put("user", userEntity);
        return "user/info";
    }

    @RequestMapping(value="info/{id}", method = RequestMethod.POST)
    public String useInfo(@PathVariable(value = "id") long id,
                               BUserEntity userEntity, Map<String, Object> model) {
        logger.debug(userEntity.getUsername());
        if(this.userService.updateNotNull(userEntity) > 0)
            model.put("user", userEntity);
        else {
            throw new BSystemException(BEnumError.DATA_UPDATE_ERROR.toString());
        }
        return "user/info";
    }


}
