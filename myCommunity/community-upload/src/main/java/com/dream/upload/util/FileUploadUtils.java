package com.dream.upload.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
public class FileUploadUtils {

	public static UploadPropertyUtil propertyUtil = UploadPropertyUtil.getInstance("application-project");

	private static String imageType = propertyUtil.getProperty("project.file.image.type");
	private static String image []={"png", "jpg", "jpeg", "gif", "bmp"};
	private static String video []={"flv", "swf", "mkv", "avi", "rm", "rmvb", "mpeg", "mpg"};
	/**
	 * 图片放大缩小
	 * @param oldPath 源图片路径
	 * @param newPath 处理后图片的路径
	 * @param scale 倍数
	 * @param flag true放大  false缩小
	 * 
	 * @return true处理成功 false处理失败
	 */
	public static boolean scaler(String oldPath,String newPath,int scale,boolean flag){
		try { 
			//得到原图片
			BufferedImage bufferedImage=ImageIO.read(new File(oldPath));
			//获取图片高宽
			int width=bufferedImage.getWidth();
			int height=bufferedImage.getHeight();
			float _scale = Float.valueOf(scale)/100f;
			
			//判断是放大还是缩小，放大除以百分比，缩小乘以百分比
			if(flag==true){//放大
				width = (int) (Float.valueOf(width)/_scale);
				height = (int)(Float.valueOf(height)/_scale);
			}else{//缩小
				width = (int) (Float.valueOf(width)*_scale);
				height = (int)(Float.valueOf(height)*_scale);
			}
			//得到缩放后台的图片
			/** width - 将图像缩放到的宽度。 height - 将图像缩放到的高度。 hints - 指示用于图像重新取样的算法类型的标志。  */
			Image image = bufferedImage.getScaledInstance(width, height,Image.SCALE_SMOOTH);
			
			//设置绘制新图片的缓冲对象
			BufferedImage outputImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
            Graphics graphics = outputImage.getGraphics();  
            graphics.drawImage(image, 0, 0, null);// 绘制处理后的图  
            graphics.dispose();
            //写出新的图片
            ImageIO.write(outputImage, getSuffix(newPath), new File(newPath)); 
            //logger.info("处理成功");
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 修改图片大小
	 * @param oldPath 源图片路径
	 * @param newPath 处理后图片的路径
	 * @param newWidth 新的宽度
	 * @param newHeight 新的高度
	 * @return true处理成功 false处理失败
	 */
	public static boolean changeSize(String oldPath,String newPath,int newWidth,int newHeight){
		try { 
			//得到原图片
			BufferedImage bufferedImage=ImageIO.read(new File(oldPath));
			/** width - 将图像缩放到的宽度。 height - 将图像缩放到的高度。 hints - 指示用于图像重新取样的算法类型的标志。  */
			Image image = bufferedImage.getScaledInstance(newWidth, newHeight,Image.SCALE_SMOOTH);
			
			//设置绘制新图片的缓冲对象
			BufferedImage outputImage = new BufferedImage(newWidth, newHeight,BufferedImage.TYPE_INT_RGB);  
            Graphics graphics = outputImage.getGraphics();  
            graphics.drawImage(image, 0, 0, null);// 绘制处理后的图  
            graphics.dispose();
            File file =  new File(newPath);
            if(!file.getParentFile().exists()){
            	file.getParentFile().mkdirs();
            }

            //写出新的图片
            ImageIO.write(outputImage, getSuffix(newPath), new File(newPath)); 
            //logger.info("处理成功");
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 图像裁剪
	 * @param oldPath 源图片路径
	 * @param newPath 处理后图片路径
	 * @param x 切片x坐标起点
	 * @param y 切片y坐标起点
	 * @param width 切片宽度
	 * @param height 切片高度
	 */
	public static void cut(String oldPath, String newPath,int x, int y, int width, int height,int imageWidth,int imageHeight) {
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(oldPath));
			// 源图宽度
			int srcWidth = bi.getWidth();
			// 源图高度
			int srcHeight = bi.getHeight();

			//若原图大小大于切片大小，则进行切割
			if (srcWidth >= width && srcHeight >= height) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);

				int x1 = x*srcWidth/imageWidth;
				int y1 = y*srcHeight/imageHeight;
				int w1 = width*srcWidth/imageWidth;
				int h1 = height*srcHeight/imageHeight;

				cropFilter = new CropImageFilter(x1, y1, w1, h1);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w1, h1,BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				// 绘制缩小后的图
				g.drawImage(img, 0, 0, null);
				g.dispose();
				File file =  new File(newPath);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				// 输出为文件
				ImageIO.write(tag, "JPEG", file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 添加水印
	 * @param pressText 水印文字
	 * @param oldPath 源图片路径
	 * @param newPath 处理后的图片路径
	 * @param fontName 水印的字体名称
	 * @param fontStyle 水印的字体样式
	 * @param color 水印的字体颜色
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public static void pressText(String pressText,String oldPath, String newPath, String fontName,int fontStyle, Color color,
			float alpha) {
	        try {
	            File img = new File(oldPath);
	            Image src = ImageIO.read(img);
	            int width = src.getWidth(null);
	            int height = src.getHeight(null);
	            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	            Graphics2D g = image.createGraphics();
	            g.drawImage(src, 0, 0, width, height, null);
	            g.setColor(color);
	            g.setFont(new Font(fontName, fontStyle, (int)(width*0.11)));
	            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
	            // 在指定坐标绘制水印文字
	            g.drawString(pressText, (int)(width*0.5),(int)(height*0.98));
	            g.dispose();
				// 输出到文件流
	            ImageIO.write(image, getSuffix(newPath), new File(newPath));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
    /**
     * 给图片添加图片水印
     * @param pressImg 水印图片
     * @param oldPath 源图像地址
     * @param newPath 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressImage(String pressImg, String oldPath,String newPath,int x, int y, float alpha) {
        try {
            File img = new File(oldPath);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, getSuffix(newPath), new File(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 图像类型转换
	 * @param oldPath 源图片路径
	 * @param formatName 要转换的格式
	 * @param newPath 处理后的图片路径
	 */
	public static void convert(String oldPath, String formatName, String newPath) {
        try {
            File f = new File(oldPath);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 图片转换成黑白
	 * @param oldPath
	 * @param newPath
	 */
	public static void gray(String oldPath, String newPath) {
        try {
            BufferedImage src = ImageIO.read(new File(oldPath));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, getSuffix(newPath), new File(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 获取后缀
	 * @param str
	 * @return 返回图片后缀
	 */
	public static String getSuffix(String str){
		return str = str.substring(str.lastIndexOf(".")+1);
	}
	/**
	 * 获取参数cutImg 判断是否切图
	 * @param  filePath
	 * @param  request
	 * @throws Exception
	 */
	public static void zoomImageUtil(String filePath, HttpServletRequest request) throws Exception {
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
			//logger.info("创建缩略图发生异常" + e.getMessage());
		}
	}

	/**
	 * 获得项目根目录
	 */
	public static String getProjectRootDirPath() {
		return propertyUtil.getProperty("project.file.root");
	}

	/**
	 * 获取文件保存的路径
	 * @param request
	 * @param ext     文件后缀
	 * @param param   传入参数
	 * @return 返回文件路径
	 */
	public synchronized static String getPath(HttpServletRequest request, String ext, String param) {
		String filePath = "/upload/"+ext+"/";
		if (param != null && param.trim().length() > 0) {
			filePath += param;
		} else {
			filePath += propertyUtil.getProperty("project.projectName");
		}
		filePath += "/" + toString(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis() + randomString(6) + "." + ext;
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
		String baseString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
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
