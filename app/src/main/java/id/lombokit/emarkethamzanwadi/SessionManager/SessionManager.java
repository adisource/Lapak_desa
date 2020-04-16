package id.lombokit.emarkethamzanwadi.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    public  static  final String SP_WALI_APP="spwaliapp";
    public  static  final String SP_NIKWALI = "spnikwali";
    public  static  final String SP_IDUSER= "spiduser";
    public  static  final String SP_NAMADESA = "spnamadesa";
    public  static  final String SP_NOINDUK = "spnomerinduk";
    public  static  final String SP_LOGINED = "splogined";

    SharedPreferences sp;
    SharedPreferences.Editor speditor;

    public SessionManager(Context context) {
        sp = context.getSharedPreferences(SP_WALI_APP, Context.MODE_PRIVATE);
        speditor = sp.edit();
    }
    public void saveString(String keySp, String value){
        speditor.putString(keySp,value);
        speditor.commit();
    }
    public  void saveInt(String keySp, int value){
        speditor.putInt(keySp,value);
        speditor.commit();
    }
    public  void saveBoolean(String keySp, boolean value){
        speditor.putBoolean(keySp,value);
        speditor.commit();
    }
    public String getSpIduser() {
        return sp.getString(SP_IDUSER,"");
    }

    public String getSpNikwali() {
        return sp.getString(SP_NIKWALI,"");
    }

    public String getSpNoinduk() {
        return sp.getString(SP_NOINDUK, "");
    }

    public String getSpNamadesa() {
        return sp.getString(SP_NAMADESA,"");
    }

    public Boolean getSpLogined() {
        return sp.getBoolean(SP_LOGINED,false);
    }
}
