package com.dream.upload.controller;
import com.dream.common.base.BaseController;
import com.dream.upload.util.FileUploadUtils;
import com.dream.upload.util.UploadPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
/**
 * 文件上传
 * @author
 */
@Controller
@RequestMapping("/file")
public class FileUploadController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    public static UploadPropertyUtil propertyUtil = UploadPropertyUtil.getInstance("application-project");

    /**
     * ueditor文件上传
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/ueditorupload",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> uploadimage(HttpServletRequest request, HttpServletResponse response, @Param(value = "param") String param) {
        Map<String, Object> uploadResult = new HashMap<>();
        MultipartHttpServletRequest mReq = null;
        MultipartFile multipartFile = null;
        // 原始文件名   UEDITOR创建页面元素时的alt和title属性
        String originalFileName = "";
        try {
            mReq = (MultipartHttpServletRequest) request;
            // 从config.json中取得上传文件的ID
            multipartFile = mReq.getFile("upfile");
            //获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名
            String ext = FileUploadUtils.getSuffix(multipartFile.getOriginalFilename());
            //获取文件路径
            String filePath = FileUploadUtils.getPath(request, ext, param);
            File file = new File(FileUploadUtils.getProjectRootDirPath() + filePath);
            //如果目录不存在，则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //保存文件
            multipartFile.transferTo(file);
            /*获取参数cutImg 判断是否切图*/
            FileUploadUtils.zoomImageUtil(filePath, request);
            // UEDITOR的规则:不为SUCCESS则显示state的内容
            uploadResult.put("state", "SUCCESS");
            uploadResult.put("url", request.getRequestURL().toString().replace(request.getRequestURI(), "") + filePath);
            uploadResult.put("title", originalFileName);
            uploadResult.put("original", originalFileName);
        } catch (Exception e) {
            logger.info("百度编辑器上传图片失败！",e);
            uploadResult.put("state", "文件上传失败!");
            uploadResult.put("url", "");
            uploadResult.put("title", "");
            uploadResult.put("original", "");
      }
        return uploadResult;
    }

    /**
     * 百度富文本编辑器图片在线管理后台接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/listimage",method = RequestMethod.GET)
    public String listimage(HttpServletRequest request, HttpServletResponse response) {

        return "";
    }
    /**
     * 百度编辑器找到config.json文件
     * @param action
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/config")
    public String config(String action, HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/static/ueditor/jsp/config.json";
    }
}
