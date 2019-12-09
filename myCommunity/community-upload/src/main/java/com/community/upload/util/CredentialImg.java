package com.community.upload.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 证书工具类
 * 作者: liangxin
 * 创建时间: 2019 - 1 - 4  10:24
 */
public class CredentialImg {

    // 图片距离右侧边距
    private static int imgRight = 105;
    // 图片距离底部边距
    private static int imgButtom = 80;
    // 文字距离左侧边距
    private static int fontLeft = 90;
    // 文字距离顶部边距
    private static int fontTop = 300;
    // 文字大小
    private static int fontSize = 25;
    // 文字首行缩进距离
    private static int fontRetract = fontSize * 2;
    // 文字样式
    private static String fontStyle = "微软雅黑";
    // 文字宽度样式
    private static int fontWidth = Font.PLAIN;
    // 文字行高
    private static int fontRowledge = 0;
    // 文字颜色样式
    private static Color fontColor = Color.black;//new Color(0, 0, 255);

    // 签名大小
    private static int signSize = 20;
    // 签名宽度
    private static int signWidth = Font.BOLD;
    // 签名样式
    private static String signStyle = fontStyle;
    // 签名颜色
    private static Color signColor = fontColor;
    // 签名距顶部距离 // 基于小图片的高度
    private static int signTop = 0;


    /**
     * 绘制班级证书
     *
     * @param credentialPath 生成路径
     * @param bgPath         背景图路径
     * @param cpPath         图章图片路径
     * @param content        证书内容
     * @param signStr        签名信息
     * @return true: 生成成功
     * false: 生成失败
     * @throws Exception
     */
    public static boolean create(String credentialPath, String bgPath, String cpPath, String content, String signStr) throws Exception {
        boolean flag = false;
        try {
            File bgImg = new File(bgPath);
            File cpImg = new File(cpPath);
            //主图片
            InputStream is = new FileInputStream(bgImg);
//            //通过JPEG图象流创建JPEG数据流解码器
//            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
//            //解码当前JPEG数据流，返回BufferedImage对象
//            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            BufferedImage buffImg = ImageIO.read(is);
            //得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            //获取背景图 宽,高
            int bg_width = buffImg.getWidth();
            int bg_height = buffImg.getHeight();

            // 绘制图章和签名
            drawImg(g, bg_width, bg_height, cpImg, signStr);
            // 绘制文字
            drawString(g, bg_width, bg_height, content);

            OutputStream os = new FileOutputStream(credentialPath);
//            //创键编码器，用于编码内存中的图象数据。
//            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
//            en.encode(buffImg);
            ImageIO.write(buffImg, "jpg", os);
            is.close();
            os.close();
            flag = true;
        } catch (Exception e) {
            System.out.print("创建失败....");
            throw e;
        }

        return flag;
    }


    /**
     * 往图片里 绘制小图片
     *
     * @param g         画笔对象
     * @param bg_width  背景图宽度
     * @param bg_height 背景图高度
     * @param image     小图片对象
     * @param signStr   签名内容
     */
    private static void drawImg(Graphics2D g, int bg_width, int bg_height, File image, String signStr) {
        //创建你要附加的图象。
        //小图片的路径
        ImageIcon imgIcon = new ImageIcon(image.getPath());
        int img_width = imgIcon.getIconWidth();
        int img_height = imgIcon.getIconHeight();
        // 计算签名距离背景图顶部的距离
        signTop = bg_height - img_height - imgButtom;
        // 先绘制签名,再绘制图章
        drawSign(g, bg_width, signStr);
        //得到Image对象。
        Image img = imgIcon.getImage();
        //将小图片绘到大图片上。
        g.drawImage(img, bg_width - img_width - imgRight, bg_height - img_height - imgButtom, null);
    }

    /**
     * 往图片内绘制文字
     *
     * @param g
     * @param bg_width
     * @param bg_height
     * @param content
     */
    private static void drawString(Graphics2D g, int bg_width, int bg_height, String content) {

        Font f = new Font(fontStyle, fontWidth, fontSize);
        g.setColor(fontColor);
        g.setFont(f);
        // 处理文字锯齿问题...
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 获取字符串在图中的矩形对象
        FontRenderContext frc = new FontRenderContext(null, true, true);

        if (content != null) {

            if (getWidth(frc, f, content).get(0) > bg_width - (fontLeft * 2 + fontRetract)) {
                // 如果当前文字的宽度大于缩进后的最大文字宽度,则进入换行模式
                char[] charArray = content.toCharArray();
                StringBuffer strBuf = new StringBuffer();
                //新一行的内容
                String newStr = "";
                // 当前文字的高度
                int str_hegith = 0;
                // 总行数
                int row_num = 0;
                for (int i = 0; i < charArray.length; i++) {
                    //当前文字内容
                    String oldStr = strBuf.toString();
                    // 拼接新的文字内容
                    newStr = strBuf.append(charArray[i]).toString();
                    // 拼接后文字在图片里的宽高
                    List<Integer> list = getWidth(frc, f, newStr);
                    // 当前文字的限制宽度
                    int font_width = bg_width - fontLeft * 2;
                    if (row_num == 0)
                        font_width -= fontRetract;
                    str_hegith = list.get(1);
                    if (list.get(0) > font_width) {
                        i--;
                        int x = fontLeft + (row_num == 0 ? fontRetract : 0);
                        int y = fontTop + (fontRowledge + str_hegith) * row_num;
                        strBuf = new StringBuffer();
                        g.drawString(oldStr, x, y);
                        row_num++;
                    }
                }
                // 将最后的文字写入图片
                int x = fontLeft + (row_num == 0 ? fontRetract : 0);
                int y = fontTop + (fontRowledge + str_hegith) * row_num;
                g.drawString(newStr, x, y);


            } else {
                g.drawString(content, fontLeft + fontRetract, fontTop);
            }
        }

    }


    /**
     * 写入签名
     *
     * @param g        画笔对象
     * @param bg_width 背景图宽度
     * @param signStr  签名内容
     */
    private static void drawSign(Graphics2D g, int bg_width, String signStr) {
        // 字体样式
        Font f = new Font(signStyle, signWidth, signSize);
        g.setColor(signColor);
        g.setFont(f);
        // 处理文字锯齿问题...
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 获取字符串在图中的矩形对象
        FontRenderContext frc = new FontRenderContext(null, true, true);

        List<Integer> list = getWidth(frc, f, signStr);
        signTop += list.get(1) * 2;
        g.drawString(signStr, bg_width - imgRight - list.get(0), signTop);

        //写入当前时间
        String timeStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        g.drawString(timeStr, bg_width - imgRight - getWidth(frc, f, timeStr).get(0), signTop + list.get(1) * 2);
    }


    /**
     * 获取指定字符串在图片中的实际宽高
     *
     * @param frc     字体
     * @param font    字体样式
     * @param content 字符串内容
     * @return List 元素0:字符串宽度  元素1:字符串高度
     */
    private static List<Integer> getWidth(FontRenderContext frc, Font font, String content) {
        Rectangle2D r2D = font.getStringBounds(content, frc);
        // 获取字符串实际宽度
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        List<Integer> list = new ArrayList<>();
        list.add(rWidth);
        list.add(rHeight);
        return list;
    }


}
