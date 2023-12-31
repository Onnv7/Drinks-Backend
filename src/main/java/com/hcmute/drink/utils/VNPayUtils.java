package com.hcmute.drink.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.hcmute.drink.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hcmute.drink.constant.VNPayConstant.*;

@Component
@Slf4j
public class VNPayUtils {
    private ObjectMapper objectMapper = new ObjectMapper();
    public Map<String, String> createUrlPayment(HttpServletRequest request, long amount, String orderInfo) throws UnsupportedEncodingException {
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> result = new HashMap<>();
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        result.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_ReturnUrl", "https://sandbox.vnpayment.vn/apis/docs/huong-dan-tich-hop/#code-returnurl");  //"http://localhost:8080/api/test/ok"
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        result.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        result.put("vnp_url", paymentUrl);
        return result;
    }


    public Map<String, Object> getTransactionInfo(String txnref, String transId, HttpServletRequest request) throws IOException {
        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_Version = VNP_VERSION;
        String vnp_Command = QUERY_DR;
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_TxnRef = txnref;//req.getParameter("order_id");
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = "";
        String vnp_TransactionDate = transId ;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(VNP_TIME_ZONE));
        SimpleDateFormat formatter = new SimpleDateFormat(VNP_TIME_FORMAT);
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_IpAddr = VNPayConfig.getIpAddress(request);




        JsonObject vnp_Params = new JsonObject ();

        vnp_Params.addProperty(VNP_REQ_ID_KEY, vnp_RequestId);
        vnp_Params.addProperty(VNP_VERSION_KEY, vnp_Version);
        vnp_Params.addProperty(VNP_COMMAND_KEY, vnp_Command);
        vnp_Params.addProperty(VNP_TMN_CODE_KEY, vnp_TmnCode);

        vnp_Params.addProperty(VNP_TXN_REF_KEY, vnp_TxnRef);

        vnp_Params.addProperty(VNP_ORDER_INFO_KEY, vnp_OrderInfo);

        if(vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
        {
            vnp_Params.addProperty(VNP_TRANSACTION_NO_KEY, "{get value of vnp_TransactionNo}");
        }

        vnp_Params.addProperty(VNP_TRANSACTION_DATE_KEY, vnp_TransactionDate);

        vnp_Params.addProperty(VNP_CREATE_DATE_KEY, vnp_CreateDate);
        vnp_Params.addProperty(VNP_IP_ADDRESS_KEY, vnp_IpAddr);

        String hash_Data = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" + vnp_TmnCode + "|" + vnp_TxnRef + "|" + vnp_TransactionDate + "|" + vnp_CreateDate + "|" + vnp_IpAddr + "|" + vnp_OrderInfo;

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hash_Data.toString());

        vnp_Params.addProperty(VNP_SECURE_HASH_KEY, vnp_SecureHash);

        URL url = new URL(VNPayConfig.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        System.out.println(response.toString());
        Map<String, Object> map = objectMapper.readValue(response.toString(), Map.class);
        return map;
    }

}
// vnp_Amount=6112000&
// vnp_BankCode=NCB&
// vnp_BankTranNo=VNP14129725&
// vnp_CardType=ATM&
// vnp_OrderInfo=6112002sd&
// vnp_PayDate=20231001003145&
// vnp_ResponseCode=00&
// vnp_TmnCode=9ZKT0SW2&
// vnp_TransactionNo=14129725&
// vnp_TransactionStatus=00&
// vnp_TxnRef=35467918&
// vnp_SecureHash=9f25992bbb30e6de7f5a860e5009672e68aa888cee7fad83b68b716f21c27a4e0a7eeb6188c2ebb0e9c94be0efe5f15942173eecefb83c298fd97c80657a6346