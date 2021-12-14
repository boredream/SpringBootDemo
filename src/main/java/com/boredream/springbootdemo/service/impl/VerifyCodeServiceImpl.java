package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.ShansumaResponse;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.service.IVerifyCodeService;
import com.boredream.springbootdemo.utils.DateUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@EnableTransactionManagement
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    private static final Logger log = LoggerFactory.getLogger(VerifyCodeServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String sendVerifyCode(String phone, long duration, boolean mock) {
        // 限制发送频率，最大频率1次/分钟，5次/小时，10次/天
        checkSendCount(getVerifyCode1dKey(phone), "明天", 10);
        checkSendCount(getVerifyCode1hKey(phone), "1小时后", 5);
        checkSendCount(getVerifyCode1mKey(phone), "1分钟后", 1);

        // 生成验证码
        String code = genVerifyCode();
        // 发送验证码
        if (!mock) {
            // 模拟发送用于测试，不实际发送短信
            try {
                ShansumaResponse response = sendMsm(phone, code);
                if (response.getCode() != 0) {
                    throw new ApiException("短信发送失败 " + response.getMessage());
                }
                if (response.getData() != null && response.getData().getCode() != 0) {
                    throw new ApiException("短信发送失败 " + response.getData().getMessage());
                }
            } catch (IOException e) {
                throw new ApiException("短信发送失败 " + e.getMessage());
            }
        }
        // 服务端保存验证码校验信息，用于后续验证。验证码有效时间5分钟
        redisTemplate.opsForValue().set(getVerifyCodeKey(phone), code, duration, TimeUnit.MILLISECONDS);

        // 记录手机时间段内发送次数
        // 1天，次日00:00点过期
        recordSendCount(getVerifyCode1dKey(phone), DateUtils.getTomorrowStartDate());
        // 1小时
        recordSendCount(getVerifyCode1hKey(phone), DateUtils.nextDate(DateUtils.ONE_HOUR_MILLIONS));
        // 1分钟
        recordSendCount(getVerifyCode1mKey(phone), DateUtils.nextDate(DateUtils.ONE_MINUTE_MILLIONS));

        log.debug("发送验证码 " + phone + " : " + code);
        return code;
    }

    private void recordSendCount(String key, Date expiredDate) {
        Boolean hasSend = redisTemplate.hasKey(key);
        Long increment = redisTemplate.opsForValue().increment(key);
        if (hasSend == null || !hasSend) {
            // 未发送过，设置过期时间
            redisTemplate.expireAt(key, expiredDate);
        }
    }

    private void checkSendCount(String key, String expiredDate, int maxCount) {
        String countStr = redisTemplate.opsForValue().get(key);
        if (countStr != null) {
            try {
                int count = Integer.parseInt(countStr);
                if (count >= maxCount) {
                    throw new ApiException("短信发送过于频繁，请于" + expiredDate + "再重新尝试");
                }
            } catch (NumberFormatException e) {
                //
            }
        }
    }

    private String getVerifyCodeKey(String phone) {
        return "VerifyCode:" + phone;
    }

    private String getVerifyCode1mKey(String phone) {
        return "VerifyCode1m:" + phone;
    }

    private String getVerifyCode1hKey(String phone) {
        return "VerifyCode1h:" + phone;
    }

    private String getVerifyCode1dKey(String phone) {
        return "VerifyCode1d:" + phone;
    }

    @Override
    public boolean checkVerifyCode(String phone, String code) {
        String redisKey = getVerifyCodeKey(phone);
        String realCode = redisTemplate.opsForValue().get(redisKey);
        if (realCode == null) {
            throw new ApiException("短信验证码已过期，请重新发送");
        }
        boolean success = realCode.equals(code);
        if (success) {
            // 成功后删除
            redisTemplate.delete(redisKey);
        }
        return success;
    }

    /**
     * 发送短信
     *
     * @param phone 手机
     * @param code  验证码
     */
    private ShansumaResponse sendMsm(String phone, String code) throws IOException {
        // http://sms.shansuma.com/docs
        Client client = new Client();
        client.setAppId("hw_10730");     //开发者ID，在【设置】-【开发设置】中获取
        client.setSecretKey("2f5f6d8ccd891bc10613cf42f7520968");    //开发者密钥，在【设置】-【开发设置】中获取
        client.setVersion("1.0");

        // json格式可在 bejson.com 进行校验
        Client.Request request = new Client.Request();
        // 这里是json字符串，send_time 为空时可以为null, params 为空时可以为null,短信签名填写审核后的签名本身，不需要填写签名id
        HashMap<String, Object> bizContent = new HashMap<>();
        bizContent.put("mobile", Collections.singletonList(phone));
        bizContent.put("type", 0); // 0. 验证码  1. 行业通知  2. 营销短信 3. 国际短信
        bizContent.put("template_id", "ST_2021121300000001"); //短信模板id
        bizContent.put("sign", "恋爱手册"); // 短信签名
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        bizContent.put("params", params); // 短信签名
        request.setBizContent(new Gson().toJson(bizContent));
        request.setMethod("sms.message.send");
        return new Gson().fromJson(client.execute(request), ShansumaResponse.class);
    }

    private String genVerifyCode() {
        // 4位随机数
        String code = String.valueOf(new Random().nextInt(10000));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4 - code.length(); i++) {
            sb.append("0");
        }
        sb.append(code);
        return sb.toString();
    }

    static class Client {
        private String appId;
        private long timestamp;
        private String secretKey;
        private String version;
        private String signType;

        Client() {
            this.timestamp = System.currentTimeMillis();
            this.version = "1.0";
            this.signType = "md5";
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public static String md5(String s) {
            char str[] = new char[32];
            char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            try {
                MessageDigest md = MessageDigest.getInstance("md5");
                byte[] b = md.digest(s.getBytes());
                int k = 0;
                for (int i = 0; i < b.length; i++) {
                    str[k++] = hex[b[i] >>> 4 & 0xf];
                    str[k++] = hex[b[i] & 0xf];
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new String(str);
        }

        public String createSignature(HashMap<String, Object> data, String secretKey) {
            Object[] array = data.keySet().toArray();
            Arrays.sort(array);
            ArrayList<String> list = new ArrayList<>();
            for (Object key : array) {
                list.add(key + "=" + data.get(key));
            }
            list.add("key=" + secretKey);

            StringBuilder sb = new StringBuilder();
            for (String v : list) {
                sb.append(v);
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return md5(sb.toString()).toUpperCase();
        }

        public String execute(Request request) throws IOException {

            StringBuilder sb = new StringBuilder();

            HashMap<String, Object> post = new HashMap<>();
            post.put("app_id", this.appId);
            post.put("timestamp", this.timestamp);
            post.put("sign_type", this.signType);
            post.put("version", this.version);
            post.put("method", request.getMethod());
            post.put("biz_content", request.getBizContent());
            post.put("sign", createSignature(post, this.secretKey));

            ArrayList<String> list = new ArrayList<>();
            for (String key : post.keySet()) {
                list.add(key + "=" + post.get(key));
            }

            StringBuilder data = new StringBuilder();
            for (String v : list) {
                data.append(v);
                data.append("&");
            }
            data.deleteCharAt(data.length() - 1);

            URL url = new URL("http://api.shansuma.com/gateway.do");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setUseCaches(false);
            http.setConnectTimeout(15 * 1000);
            http.setReadTimeout(60 * 1000);
            http.setRequestMethod("POST");
            http.setRequestProperty("User-Agent", "Mozilla 5.0 Java-SMS-SDK v1.0.0 (Haowei Tech)");
            http.setRequestProperty("Connection", "close");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("Content-Length", "" + data.length());
            http.setDoOutput(true);
            http.setDoInput(true);

            OutputStream ops = http.getOutputStream();
            ops.write(data.toString().getBytes());
            ops.flush();
            ops.close();

            String next;
            InputStreamReader reader = new InputStreamReader(http.getInputStream());
            BufferedReader buffered = new BufferedReader(reader);
            while ((next = buffered.readLine()) != null) {
                sb.append(next);
            }
            return sb.toString();
        }

        public static class Request {
            private String bizContent;
            private String method;

            public void setBizContent(String bizContent) {
                this.bizContent = bizContent;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public String getMethod() {
                return method;
            }

            public String getBizContent() {
                return bizContent;
            }
        }

    }
}
