package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.ImageModerationRequest;
import com.aliyun.green20220302.models.ImageModerationResponse;
import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.heima.common.aliyun.config.AlibabaOSSConfig;
import com.heima.common.aliyun.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class GreenImageScan {
    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private AlibabaOSSConfig alibabaOSSConfig;

    private OSS ossClient = null;
    // 置信度阈值
    private static final float riskThreshold = 0.9F;

    public String uploadFile(String filePath, byte[] imageBytes){
        String[] split = filePath.split("\\.");
        String objectName;
        if(split.length>1){
            objectName = UUID.randomUUID()+"."+split[split.length-1];
        }
        else{
            objectName = UUID.randomUUID()+"";
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(alibabaOSSConfig.getBucket(), objectName, new ByteArrayInputStream(imageBytes));
        ossClient.putObject(putObjectRequest);
        return objectName;
    }

    public Map greenImageScan(String filePath, byte[] imageBytes) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        try{
            // 配置客户端
            Config config = new Config()
                    .setAccessKeyId(aliyunConfig.getAccessKeyId())
                    .setAccessKeySecret(aliyunConfig.getSecret())
                    .setRegionId("cn-shanghai")
                    .setEndpoint("green-cip.cn-shanghai.aliyuncs.com")
                    .setReadTimeout(6000)
                    .setConnectTimeout(3000);
            Client client = new Client(config);
            RuntimeOptions runtime = new RuntimeOptions();
            runtime.readTimeout = 10000;
            runtime.connectTimeout = 10000;
            // 配置OSS客户端，上传minio图片到阿里云OSS
            ossClient = new OSSClientBuilder().build(alibabaOSSConfig.getEndpoint(), alibabaOSSConfig.getAccessKey(), alibabaOSSConfig.getAccessSecret());
            String objectName = uploadFile(filePath, imageBytes);
            // 检测参数构造
            Map<String, String> serviceParameters = new HashMap<>();
            serviceParameters.put("ossBucketName", alibabaOSSConfig.getBucket());
            serviceParameters.put("ossObjectName", objectName);
            serviceParameters.put("dataId", UUID.randomUUID().toString());
            serviceParameters.put("ossRegionId", "cn-shanghai");
            // 请求对象
            ImageModerationRequest request = new ImageModerationRequest();
            request.setService("baselineCheck");
            request.setServiceParameters(JSON.toJSONString(serviceParameters));
            // 调用检测服务
            ImageModerationResponse response = client.imageModerationWithOptions(request, runtime);
            // 处理响应
            if(response != null&&response.getStatusCode() == 200){
                ImageModerationResponseBody body = response.getBody();
                if(body != null&&body.getCode() == 200){
                    ImageModerationResponseBody.ImageModerationResponseBodyData data = body.getData();
                    boolean highFlag = false;
                    List<String> reasons = new ArrayList<>();
                    List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> results = data.getResult();
                    for (ImageModerationResponseBody.ImageModerationResponseBodyDataResult result : results) {
                        if("nonLabel".equals(result.getLabel())){
                            resultMap.put("suggestion", "pass");
                            resultMap.put("reason", "内容正常，无风险标签");
                            return resultMap;
                        }
                        else{
                            if(result.getConfidence()>riskThreshold){
                                highFlag = true;
                            }
                            reasons.add(result.getLabel());
                        }
                    }
                    if(highFlag){
                        resultMap.put("suggestion", "block");
                        resultMap.put("reason", String.join(",", reasons));
                    }
                    else{
                        resultMap.put("suggestion", "review");
                        resultMap.put("reason", String.join(",", reasons));
                    }
                }else{
                    resultMap.put("suggestion", "error");
                    resultMap.put("reason", "API调用失败，错误码：" + (body != null ? body.getCode() : "null"));
                }
            }
            else {
                System.out.println("Response failed. Status Code: " + (response != null ? response.getStatusCode() : "null"));
                resultMap.put("suggestion", "error");
                resultMap.put("reason", "服务调用失败");
            }
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}























