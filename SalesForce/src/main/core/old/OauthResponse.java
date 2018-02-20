package main.core.old;

import org.json.simple.JSONObject;

/**
 * Used to set the details received in the Response into global variables
 *
 * @author Saranya
 */
public class OauthResponse {

  public static void SetGlobalVariablesFromResponse(JSONObject json) {
    try {
      Constants.USER_ID = json.get("id").toString();
      Constants.ISSUED_AT = json.get("issued_at").toString();
      Constants.INSTANCE_URL = json.get("instance_url").toString();
      Constants.SIGNATURE = json.get("signature").toString();
      Constants.ACCESS_TOKEN = json.get("access_token").toString();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
