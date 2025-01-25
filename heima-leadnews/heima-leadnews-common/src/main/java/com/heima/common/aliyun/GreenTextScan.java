package com.heima.common.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.green20220302.models.TextModerationResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.heima.common.aliyun.config.AliyunConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GreenTextScan {

    @Autowired
    private AliyunConfig aliyunConfig;

    public Map greeTextScan(String content) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        try {
            // 配置客户端
            Config config = new Config()
                    .setAccessKeyId(aliyunConfig.getAccessKeyId())
                    .setAccessKeySecret(aliyunConfig.getSecret())
                    .setRegionId("cn-shanghai")
                    .setEndpoint("green-cip.cn-shanghai.aliyuncs.com")
                    .setReadTimeout(6000)
                    .setConnectTimeout(3000);
            Client client = new Client(config);
            // 设置 Runtime 参数
            RuntimeOptions runtime = new RuntimeOptions();
            runtime.readTimeout = 10000;
            runtime.connectTimeout = 10000;
            // 检测参数构造
            JSONObject serviceParameters = new JSONObject();
            serviceParameters.put("content", content);
            serviceParameters.put("dataId", UUID.randomUUID().toString());
            // 请求对象
            TextModerationRequest textModerationRequest = new TextModerationRequest();
            textModerationRequest.setService("pgc_detection");
            textModerationRequest.setServiceParameters(serviceParameters.toJSONString());
            // 调用检测服务
            TextModerationResponse response = client.textModerationWithOptions(textModerationRequest, runtime);
            // 处理响应
            if(response != null&&response.getStatusCode() == 200){
                TextModerationResponseBody body = response.getBody();
                if(body!=null&&body.getCode()==200){
                    TextModerationResponseBody.TextModerationResponseBodyData data = body.getData();
                    String labels = data.getLabels();
                    String reason = data.getReason();
                    if (labels == null || labels.trim().isEmpty()) {
                        // labels 为空 -> 通过
                        resultMap.put("suggestion", "pass");
                        resultMap.put("reason", "内容正常，无风险标签");
                    }
                    else{
                        // 解析 reason 字段，获取 riskLevel 和 riskTips
                        JSONObject reasonJson = JSONObject.parseObject(reason);
                        String riskLevel = reasonJson.getString("riskLevel");
                        String riskTips = reasonJson.getString("riskTips");
                        if ("low".equalsIgnoreCase(riskLevel)) {
                            // riskLevel 为 low -> 人工审核
                            resultMap.put("suggestion", "review");
                            resultMap.put("reason", riskTips);
                        } else {
                            // 其他情况 -> 拦截
                            resultMap.put("suggestion", "block");
                            resultMap.put("reason", riskTips);
                        }
                    }
                }
                else {
                    System.out.println("Response Code: " + (body!=null?body.getCode():null));
                    resultMap.put("suggestion", "error");
                    resultMap.put("reason", "API调用失败，错误码：" + (body!=null?body.getCode():null));
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





















