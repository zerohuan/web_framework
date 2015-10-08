package com.yjh.base.site.action;

import com.yjh.base.site.service.DefaultBFileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * File download and upload
 *
 * Created by yjh on 15-9-20.
 */
@Controller
public class FileController {
    private static Logger logger = LogManager.getLogger();
    @Inject
    private DefaultBFileService fileService;

    @RequestMapping("showUpload")
    public String showUpload() {
        return "upload";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = "text/html")
    @ResponseBody
    public String upload(String username,
                         @RequestParam(value = "attachment") MultipartFile attachment) {

        


        return "success " +
                "name:" + username +
                "size:" + attachment.getSize();
    }

    public static class Form
    {
        private String subject;
        private String body;
        private List<MultipartFile> attachments;

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }

        public List<MultipartFile> getAttachments()
        {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments)
        {
            this.attachments = attachments;
        }
    }

    @RequestMapping(value = "listFile", method = RequestMethod.GET)
    public String fileList(Map<String, Object> model) {
        model.put("fileList", fileService.getBFiles());
        return "entities";
    }


}
