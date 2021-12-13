package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.VerifyCode;
import com.boredream.springbootdemo.service.IVerifyCodeService;
import com.boredream.springbootdemo.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

@Service
@EnableTransactionManagement
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public VerifyCode sendVerifyCode(String phone, Integer verifyType) {
        // 生成验证码
        String code = genVerifyCode();
        // 验证码有效时间，单位分
        long validDuration = 5 * DateUtils.ONE_MINUTE_MILLIONS;
        // 服务端保存验证码校验信息，用于后续验证
        // TODO: chunyang 2021/12/13  
        // 发送验证码
        try {
            sendMsm(phone, code);
        } catch (IOException e) {
            // TODO: chunyang 2021/12/13 短信发送失败
            e.printStackTrace();
        }
        return null;
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

    /**
     * 发送短信
     * @param phone 手机
     * @param code 验证码
     */
    private void sendMsm(String phone, String code) throws IOException {
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
        bizContent.put("template_id", "ST_2020101100000007"); //短信模板id
        bizContent.put("sign", "恋爱手册"); // 短信签名
        bizContent.put("params", "{\"code\":" + code + "}"); // 短信签名
        request.setBizContent(new ObjectMapper().writeValueAsString(bizContent));
        request.setMethod("sms.message.send");
        System.out.println(client.execute(request));
    }

    @Override
    public boolean checkVerifyCode(VerifyCode verifyCode) {
        return false;
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
