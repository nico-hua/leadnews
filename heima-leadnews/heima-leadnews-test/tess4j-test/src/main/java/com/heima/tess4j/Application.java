package com.heima.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.concurrent.*;

public class Application {

    /**
     * 识别图片中的文字
     * */
    public static void main(String[] args) {
        // 创建实例
        ITesseract tesseract = new Tesseract();
        // 设置字体库路径
        tesseract.setDatapath("D:\\Desktop\\Code\\tessdata");
        // 设置语言
        tesseract.setLanguage("chi_sim");

        // 要处理的图片文件
        File file = new File("D:\\Desktop\\Download\\pexels-samandgos-709552.jpg");

        // 创建线程池和 Callable 任务
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> {
            try {
                System.out.println("开始 OCR 处理...");
                return tesseract.doOCR(file);
            } catch (TesseractException e) {
                throw new RuntimeException("OCR 处理失败", e);
            }
        };

        Future<String> future = executor.submit(task);

        try {
            // 设置超时时间为 3 秒
            String result = future.get(3, TimeUnit.SECONDS);
            System.out.println("OCR 结果：" + result.replaceAll("\\r|\\n", "-"));
        } catch (TimeoutException e) {
            System.err.println("OCR 超时，任务取消！");
            future.cancel(true); // 强制取消任务
        } catch (ExecutionException e) {
            System.err.println("OCR 任务执行异常：" + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("OCR 任务被中断：" + e.getMessage());
        } finally {
            executor.shutdown(); // 关闭线程池
        }
        System.out.println("OCR 处理完成！");
    }
}











































