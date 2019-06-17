package com.winter.videoanalysisdemo.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * 图片处理方法
 * @author Winter
 * @title: imageUtil
 * @projectName videoanalysisdemo
 * @description: TODO
 * @date 2019/6/1317:18
 **/
public class imageUtil {

    /**
     * 中值滤波
     */
    public  static void medianBlur(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("");
        Mat dst = src.clone();
        Imgproc.medianBlur(src,dst,7);
        Imgcodecs.imwrite("",dst);
    }

    /**
     * 图片清晰度评价值
     * @param jpegFile
     */
    public static void blurValue(File jpegFile){
    String path = "E:\\demo\\";
    opencv_core.Mat srcImage = opencv_imgcodecs.imread(jpegFile.getAbsolutePath());
    opencv_core.Mat dstImage = new opencv_core.Mat();
    //转化为灰度图
    opencv_imgproc.cvtColor(srcImage, dstImage, opencv_imgproc.COLOR_BGR2GRAY);
    //在gray目录下生成灰度图片
    opencv_imgcodecs.imwrite(path+"gray-"+jpegFile.getName(), dstImage);

    opencv_core.Mat laplacianDstImage = new opencv_core.Mat();
    //阈值太低会导致正常图片被误断为模糊图片，阈值太高会导致模糊图片被误判为正常图片
    opencv_imgproc.Laplacian(dstImage, laplacianDstImage, opencv_core.CV_64F);
    //在laplacian目录下升成经过拉普拉斯掩模做卷积运算的图片
    opencv_imgcodecs.imwrite(path+"laplacian-"+jpegFile.getName(), laplacianDstImage);
    //矩阵标准差
    opencv_core.Mat stddev = new opencv_core.Mat();
    //求矩阵的均值与标准差
    opencv_core.meanStdDev(laplacianDstImage, new opencv_core.Mat(), stddev);
    // ((全部元素的平方)的和)的平方根
    // double norm = Core.norm(laplacianDstImage);
    // System.out.println("\n矩阵的均值：\n" + mean.dump());
    System.out.println(jpegFile.getName() + "矩阵的标准差：\n" + stddev.createIndexer().getDouble());
    // System.out.println(jpegFile.getName()+"平方根：\n" + norm);
}

    public static void main(String[] args) {
//        imageUtil.medianBlur();
        File file = new File("E:\\demo\\image.jpg");
        imageUtil.blurValue(file);
    }

}
