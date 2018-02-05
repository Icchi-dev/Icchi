package leapfrog_inc.icchi.Function;

/**
 * Created by Leapfrog-Software on 2018/01/25.
 */

public class Constants {
    public static String ServerRootUrl = "http://lfrogs.sakura.ne.jp/icchi/srv.php";
//    public static String ServerRootUrl = "http://192.168.150.34/icchi/srv.php";
    public static int HttpConnectTimeout = 10000;
    public static int HttpReadTimeout = 10000;

    public static class SharedPreferenceKey {
        public static String Key = "Icchi";
        public static String UserId = "UserId";
        public static String IsInitialized = "IsInitialized";
    }
}