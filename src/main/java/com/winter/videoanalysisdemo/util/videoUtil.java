package com.winter.videoanalysisdemo.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 视频处理方法
 * @author Winter
 * @title: videoUtil
 * @projectName videoanalysisdemo
 * @description: TODO
 * @date 2019/6/1314:48
 **/
public class videoUtil {
    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param file      源视频文件
     * @param framefile 截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchPic(File file, String framefile, int second) throws Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
        ff.start();
        int length = ff.getLengthInAudioFrames();
        System.out.println(length);
        //获取帧率
        System.out.println(ff.getFrameRate());

        int i = 0;
        Frame frame = null;
        second = 0;
        while (i < length) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            frame = ff.grabImage();
//            if (i>=(int) (ff.getFrameRate()*second)&&frame.image != null) {
            System.out.print(i + ",");
            if (i == 50) {
                int a = 1;
            }
            if (frame != null && frame.image != null) {
                System.out.println(i);
                writeToFile(frame, i);
            }
//                second++;
//            }
            i++;
        }
        ff.stop();
    }

    public static void writeToFile(Frame frame, int second) {
        File targetFile = new File("G:/videoanalysis/" + second + ".jpg");
        String imgSuffix = "jpg";

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取视频时长，单位为秒
     *
     * @param file
     * @return 时长（s）
     */
    public static Long getVideoTime(File file) {
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            ff.start();
            times = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

//    public void getBySecond() {
//        opencv_videoio.CvCapture capture = opencv_highgui.cvC("D:/085402.crf");
//
//
//
//        //帧率
//        int fps = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FPS);
//        System.out.println("帧率:"+fps);
//
//        opencv_core.IplImage frame = null;
//        double pos1 = 0;
//
//        int rootCount = 0;
//
//        while (true) {
//
//            //读取关键帧
//            frame = opencv_highgui.cvQueryFrame(capture);
//
//            rootCount = fps;
//            while(rootCount > 0 ){
//                //这一段的目的是跳过每一秒钟的帧数,也就是说fps是帧率(一秒钟有多少帧),在读取一帧后,跳过fps数量的帧就相当于跳过了1秒钟。
//                frame = opencv_highgui.cvQueryFrame(capture);
//                rootCount--;
//            }
//
//            //获取当前帧的位置
//            pos1 = opencv_highgui.cvGetCaptureProperty(capture,opencv_highgui.CV_CAP_PROP_POS_FRAMES);
//            System.out.println(pos1);
//
//            if (null == frame)
//                break;
//
//            opencv_highgui.cvSaveImage("d:/img/" + pos1 + ".jpg",frame);
//
//        }
//
//        opencv_highgui.cvReleaseCapture(capture);
//    }

    public static void main(String[] args) {
        try {
            File file = new File("G:/demo.mp4");
            videoUtil.fetchPic(file, "G:/demo1.jpg", 5);
            System.out.println(videoUtil.getVideoTime(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
