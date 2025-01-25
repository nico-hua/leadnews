package com.heima.common.tess4j;

import com.heima.common.tess4j.config.Tess4jConfig;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.concurrent.*;

@Component
public class Tess4jClient {
    @Autowired
    private Tess4jConfig tess4jConfig;

    public String doOCR(BufferedImage image) {
        // 创建线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tess4jConfig.getDataPath());
            tesseract.setLanguage(tess4jConfig.getLanguage());
            String result = tesseract.doOCR(image);
            return result.replaceAll("\\r|\\n", "-").replaceAll(" ", "");
        };

        Future<String> future = executor.submit(task);

        try {
            // 设置超时时间为 3 秒
            return future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.err.println("OCR 超时，返回空字符串！");
            future.cancel(true); // 强制取消任务
            return ""; // 返回空字符串
        } catch (ExecutionException e) {
            System.err.println("OCR 执行异常：" + e.getMessage());
            return ""; // 返回空字符串
        } catch (InterruptedException e) {
            System.err.println("OCR 任务被中断：" + e.getMessage());
            return ""; // 返回空字符串
        } finally {
            executor.shutdown(); // 释放线程池资源
        }
    }

}












