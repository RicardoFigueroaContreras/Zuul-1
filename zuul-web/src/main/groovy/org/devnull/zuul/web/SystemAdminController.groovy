package org.devnull.zuul.web

import org.devnull.security.service.SecurityService
import org.devnull.zuul.service.ZuulService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.devnull.zuul.data.model.EncryptionKey
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import static org.devnull.zuul.web.config.ZuulWebConstants.FLASH_ALERT_MESSAGE
import static org.devnull.zuul.web.config.ZuulWebConstants.FLASH_ALERT_TYPE

@Controller
class SystemAdminController {

    @Autowired
    SecurityService securityService

    @Autowired
    ZuulService zuulService

    @RequestMapping("/system/users")
    ModelAndView listUsers() {
        def model = [:]
        model.users = securityService.listUsers()
        model.roles = securityService.listRoles()
        return new ModelAndView("/system/users", model)
    }

    @RequestMapping("/system/keys")
    ModelAndView listKeys() {
        def model = [:]
        model.keys = zuulService.listEncryptionKeys()
        return new ModelAndView("/system/keys", model)
    }

    @RequestMapping(value = "/system/keys/create", method = RequestMethod.GET)
    String displayCreateKeyForm() {
        return "/system/createKey"
    }

    @RequestMapping(value = "/system/keys/create", method = RequestMethod.POST)
    String createKey(EncryptionKey key, RedirectAttributes redirectAttrs) {
        zuulService.saveKey(key)
        redirectAttrs.addFlashAttribute(FLASH_ALERT_MESSAGE, "Key ${key.name} Created")
        redirectAttrs.addFlashAttribute(FLASH_ALERT_TYPE, "success")
        return "redirect:/system/keys"
    }

}
