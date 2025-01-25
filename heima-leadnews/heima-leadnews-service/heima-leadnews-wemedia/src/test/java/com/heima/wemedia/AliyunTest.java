package com.heima.wemedia;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("中国政府严禁冰毒交易！");
        System.out.println(map);
    }

    @Test
    public void testScanImage() throws Exception {
        String filePath = "http://127.0.0.1:9000/leadnews/2025/01/11/1a8ae25f8933403490fee494c77e154f.jpg";
        byte[] bytes = fileStorageService.downLoadFile(filePath);
        Map map = greenImageScan.greenImageScan(filePath, bytes);
        System.out.println(map);
    }

}