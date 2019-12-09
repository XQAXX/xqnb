package com.community.upload.controller;
import com.community.common.util.PropertyUtil;
import com.community.upload.util.FileUploadUtils;
import com.community.upload.util.UploadPropertyUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.community.upload.util.FileUploadUtils.checkFileExt;

/**
 * 图片上传
 * @author
 */
@Controller
@RequestMapping("/image")
public class ImageUploadController {

    private static final int BUF_SIZE = 2 * 1024;

    private static Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    public static UploadPropertyUtil propertyUtil = UploadPropertyUtil.getInstance("application-project");

    /**
     * ueditor上传图片
     *
     * @param request
     * @return UEDITOR 需要的json格式数据
     */
    @RequestMapping(value = "/ueditorupload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object ueditorupload(HttpServletRequest request, HttpServletResponse response, @Param(value = "param") String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        MultipartHttpServletRequest mReq = null;
        MultipartFile multipartFile = null;
        InputStream is = null;
        String fileName = "";
        // 原始文件名   UEDITOR创建页面元素时的alt和title属性
        String originalFileName = "";

        try {
            mReq = (MultipartHttpServletRequest) request;
            // 从config.json中取得上传文件的ID
            multipartFile = mReq.getFile("upfile");
            // 取得文件的原始文件名称
            fileName = multipartFile.getOriginalFilename();

			/*originalFileName = fileName;

			if(!StringUtils.isEmpty(fileName)){
				is = file.getInputStream();
				fileName = FileUtils.reName(fileName);
				filePath = FileUtils.saveFile(fileName, is, fileuploadPath);
			} else {
				throw new IOException("文件名为空!");
			}*/


            //最大文件大小
			/*long maxSize = 4096000;
			logger.info(file.getSize());
			//检查文件大小
			if(file.getSize() > maxSize){
				return responseErrorData(response,1,"上传的图片大小不能超过4M。");
			}*/
            //获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名
            String ext = FileUploadUtils.getSuffix(multipartFile.getOriginalFilename());
            if (!checkFileExt(ext)) {
                result.put("state", "文件格式错误，上传失败!");
                result.put("url", "");
                result.put("title", "");
                result.put("original", "");
                return result;
            }
            //获取文件路径
            String filePath = getPath(request, ext, param);
            File file = new File(getProjectRootDirPath() + filePath);

            //如果目录不存在，则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //保存文件
            multipartFile.transferTo(file);

            /*获取参数cutImg 判断是否切图*/
            zoomImageUtil(filePath, request);
            // UEDITOR的规则:不为SUCCESS则显示state的内容
            result.put("state", "SUCCESS");
            result.put("url", request.getRequestURL().toString().replace(request.getRequestURI(), "") + filePath);
            result.put("title", originalFileName);
            result.put("original", originalFileName);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("state", "文件上传失败!");
            result.put("url", "");
            result.put("title", "");
            result.put("original", "");
        }
        return result;
    }
    /**
     * 获取参数cutImg 判断是否切图
     *
     * @param filePath
     * @param request
     * @throws Exception
     */
    private static void zoomImageUtil(String filePath, HttpServletRequest request) throws Exception {
        /*获取参数cutImg 判断是否切图*/
        String cutImg = request.getParameter("cutImg");
        if ("true".equals(cutImg)) {
            /*获取传过来的图片宽高*/
            String width = request.getParameter("width");
            String height = request.getParameter("height");
            /*如果图片尺寸不为空调用图片拉伸方法*/
            if ((null != width && width.trim().length() != 0) && (null != height && height.trim().length() != 0) && !"undefined".equals(width) && !"undefined".equals(height)) {
                zoomImage(filePath, Integer.parseInt(width), Integer.parseInt(height));
            }
        }
    }
    /**
     * 图片缩放,w，h为缩放的目标宽度和高度
     * src为源文件目录
     */
    public static void zoomImage(String src, int newWidth, int newHeight) throws Exception {
        if (newWidth == 0 || newHeight == 0 || src == null || src.trim().length() == 0) {
            return;
        }
        String dest = getProjectRootDirPath();

        BufferedImage result = null;
        try {
            File f2 = new File(dest + src);

            BufferedImage bi2 = ImageIO.read(f2);
            int originalh = bi2.getHeight();
            int originalw = bi2.getWidth();

            if (originalh != newHeight || originalw != newWidth) {
                BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = to.createGraphics();

                to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,

                        Transparency.TRANSLUCENT);

                g2d.dispose();

                g2d = to.createGraphics();

                Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
                g2d.drawImage(from, 0, 0, null);
                g2d.dispose();
                ImageIO.write(to, "png", new File(dest + src));
            }
        } catch (Exception e) {
            logger.info("创建缩略图发生异常" + e.getMessage());
        }
    }
    /**
     * 获得项目根目录
     */
    private static String getProjectRootDirPath() {

        return propertyUtil.getProperty("project.file.root");
    }
    /**
     * 获取文件保存的路径
     *
     * @param request
     * @param ext     文件后缀
     * @param param   传入参数
     * @return 返回文件路径
     */
    private synchronized String getPath(HttpServletRequest request, String ext, String param) {
        String filePath = "/images/upload/";
        if (param != null && param.trim().length() > 0) {
            filePath += param;
        } else {
            filePath += propertyUtil.getProperty("project.projectName");
        }
        filePath += "/" + toString(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis()+randomString(6) + "." + ext;
        return filePath;
    }

    public static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            if (pattern == null) {
                pattern = "yyyy-MM-dd";
            }

            String dateString = "";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            try {
                dateString = sdf.format(date);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return dateString;
        }
    }
    /**
     * 获得一个随机的字符串
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        String baseString="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

}