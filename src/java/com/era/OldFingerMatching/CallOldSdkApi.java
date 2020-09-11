package com.era.OldFingerMatching;

import com.era.model.MatchFlagWithScore;
import era.data.LoginModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author roman
 */
public class CallOldSdkApi {

    MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();

    public LoginModel oldFingerMatch(String reqJsonParam) throws IOException {
        LoginModel model = new LoginModel();
        String jsonResponse = null;
        String errorFlag = null;
        String errorMsg = null;
        
        IpPortInfo ipPortInfo = new IpPortInfo();
        ipPortInfo = IpPortRead.getIpPortInfo();
        
        String ip = ipPortInfo.getIp();
        String port = ipPortInfo.getPort();
        String url = "http://"+ip+":"+port+"/FingerEnrollVerify/FingerVerification";

        try {// API Calling For Old Fingerprint
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            //RequestBody body = RequestBody.create(mediaType, "{\"appuser\":\"ferdous87\",\"amount\":\"0\",\"custno\":\"2090785\",\"temp\":\"Rk1SACAyMAAAAAGwAAABQAHgAMUAxQEAAAA8Q4EgAQ+9AIEHALjRAIDeAG\\/kAEDWARmwAIDyAN\\/AAIDNAEPyAED7AIbhAICSAFQHAEBHALg1AICgAQWsAIDoAEt1AEEZAYgQAIDrAD\\/rAECfAH0DAIB4APWoAIEvAK1aAIEJAO7AAIC5AInkAICMAWcaAICOADCUAICVADsJAEBAAU0hAIDbAW4hAEDuAaoBAEC5AKvOAEDaATKoAIBBAXUQAIEpAMvSAIDnAYMQAECGAIEQAIDiASY3AIBIAGqsAEB+APWsAEA6AVEVAICCARkuAEBXAWEVAECdAXQSAEB8ABaVAICMANuwAEBtAQYoAIDNAWSaAED7AZ4GAEB2AM6vAIEYAOrJAEDmAUMvAEDoAQC3AIDFAW4UAECYAXQaAIAvAXcVAEDrABVzAEBLAKuzAEBlAKwlAICjATIrAID8AXEaAEEtAMXSAIDQATI3AIA\\/AL21AIAsAPU1AEC6APysAEBKAHsrAIEdAV4kAEEJAY4SAID+ACZpAIBoAYQLAECZATCmAIDuAUeuAICQAS8pAAAA\",\"pOperationType\":\"LOGON\",\"cust_type\":\"SAG\",\"pDeviceId\":\"01000410\",\"name\":\"2090785\",\"pSessionId\":\"6524201104460\",\"pLogId\":\"0000\"}\r\n");
            RequestBody body = RequestBody.create(mediaType, reqJsonParam);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            jsonResponse = response.body().string();
            System.out.println("Response : " + jsonResponse);
        } catch (Exception e) {
            System.out.println("FingerEnrollVerify (Old FP) api calling error.Error is " + e.toString());
        }

        // JSON Parse    
        JSONObject obj;
        try {
            obj = new JSONObject(jsonResponse);
            JSONArray jsonArray = obj.getJSONArray("verifyNodes");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            errorFlag = jsonObject.getString("outCode");
            errorMsg = jsonObject.getString("outMessage");
            //System.out.println("flag : "+errorFlag+"\n msg : "+errorMsg);
        } catch (JSONException ex) {
            System.out.println("Json parsing error. Which response from FingerEnrollVerify (Old SDK API).");
            Logger.getLogger(CallOldSdkApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.setErrorFlag(errorFlag);
        model.setErrorMessage(errorMsg);

        return model;
    }

    public static void main(String[] args) throws IOException {
        CallOldSdkApi callOldSdkApi = new CallOldSdkApi();
        String reqJsonParam = "{\"appuser\":\"roman007\",\"amount\":\"0\",\"custno\":\"2090785\",\"temp\":\"Rk1SACAyMAAAAAGwAAABQAHgAMUAxQEAAABkQ4AgAJekAIC2AJ7lAID\\/AHThAIBXAKUNAIEMATfQAIEBAPrQAICzAUg1AEBrAIMDAEDxARPHAECqADd9AIBEARylAID1ATa6AICpAHT1AIDGALrhAIB8AEYDAEDbAOTSAIBPATorAEClAT2uAICPALLkAIC3ASGzAIEpAW1hAEByAKT5AIEIAI\\/hAECJANPOAIDgARXAAEA3ASclAIEEAU3GAIDMAZUXAIC2AaYOAIBxAGcHAIE1AOTYAIEJAN9YAIDtAEfmAEDoAasJAEEEAa\\/5AEDUAG9wAIDEAH11AEBdAP+rAICqAZAfAIE1APpXAIDGAQbAAEAjAYIaAIAdAKgkAEC3AWQuAIBvAVMrAEBbAYkaAEDEAHToAIEiAXiJAIDUAQlGAIAwANIlAIBdAU4pAIEzAVbbAICbAYmVAIDxAYQlAIAlAMYlAEDtAQvNAECKASCoAIBtASapAIEpATDYAEBpAZUSAEALATUrAICrABF5AEBlAVGkAEEwASxSAIECAYsLAEBPAO6rAIEgAO\\/aAAAA\",\"pOperationType\":\"LOGON\",\"cust_type\":\"SAG\",\"pDeviceId\":\"01000410\",\"name\":\"2090785\",\"pSessionId\":\"16326550455524\",\"pLogId\":\"0000\"}";
        LoginModel model = callOldSdkApi.oldFingerMatch(reqJsonParam);
        System.out.println("flag : " + model.getErrorFlag() + "\n msg : " + model.getErrorMessage());
    }

}
