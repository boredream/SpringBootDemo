package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.WxAccessToken;
import com.boredream.springbootdemo.entity.WxMsgSecCheckResult;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.boredream.springbootdemo.service.IWxService;
import com.google.gson.Gson;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WxServiceImpl implements IWxService {

    private static final Logger log = LoggerFactory.getLogger(WxServiceImpl.class);

    private static final String PLATFORM_WX = "wx";
    private static final String WX_APP_ID = "wx0896ca1ddd64114e";
    private static final String WX_APP_SECRET = "068602332cdecb1a9a5ec5c4b00b16f8";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public WxSessionDTO wxLogin(WxLoginDTO dto) {
        // https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", WX_APP_ID);
        requestMap.put("secret", WX_APP_SECRET);
        requestMap.put("code", dto.getCode());
        return new Gson().fromJson(restTemplate.getForObject(url, String.class, requestMap), WxSessionDTO.class);
    }

    @Override
    public String getAccessToken() {
        // https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
        String key = "AccessToken";
        String accessToken = redisTemplate.opsForValue().get(key);
        if (accessToken == null) {
            // 缓存无法获取时，从微信服务器拉取
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("grant_type", "client_credential");
            requestMap.put("appid", WX_APP_ID);
            requestMap.put("secret", WX_APP_SECRET);
            WxAccessToken wat = restTemplate.getForObject(url, WxAccessToken.class, requestMap);
            if (wat != null) {
                accessToken = wat.getAccess_token();
                long duration = wat.getExpires_in() - 5;
                redisTemplate.opsForValue().set(key, accessToken, duration, TimeUnit.SECONDS);
            }
        }
        return accessToken;
    }

    @Override
    public boolean checkMsgSec(String platform, Long curUserId, int scene, String content) {
        if (!PLATFORM_WX.equals(platform)) {
            // 非微信平台的，不用安全检测
            return true;
        }
        // https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/sec-check/security.msgSecCheck.html
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token={access_token}";
        Map<String, String> urlParamsMap = new HashMap<>();
        urlParamsMap.put("access_token", getAccessToken());

        User curUser = userMapper.selectById(curUserId);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("version", 2);
        requestMap.put("openid", curUser.getOpenId());
        requestMap.put("scene", scene);
        requestMap.put("content", StringUtil.isNullOrEmpty(content) ? "content" : content);
        WxMsgSecCheckResult checkResult = restTemplate.postForObject(url, requestMap, WxMsgSecCheckResult.class, urlParamsMap);
        log.info("[checkMsgSec] params = " + new Gson().toJson(requestMap) + ", result = " + new Gson().toJson(checkResult));
        return checkResult != null && checkResult.isPass();
    }
}
