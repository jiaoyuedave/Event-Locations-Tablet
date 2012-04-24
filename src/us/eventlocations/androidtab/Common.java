package us.eventlocations.androidtab;



public final class Common {
	
	private Common() { }
//http://eventlocations.us/admina/services/iphone.asmx?op=GetBridalShows -PlanIt Expo
	public static final int SEARCH_BY_COUNTY=1;
	public static final int SEARCH_BY_SITE_NAME=2;
	public static final int SEARCH_SERVICES=3;
	public static final int SEARCH_CATERERS=4;
	public static final int FIND_BRIDAL_SHOW=5;
	public static final int HONEYMOON=6;
	public static final String GetBridalShows="GetBridalShows";
	public static final String GetAccountsByCounty="GetAccountsByCounty";
	public static final String SendAccountEmail="SendAccountEmail";
	public static final String SendWinaHoneyMoon="SubmitWinAHoneymoon";
	public static final String GetMobileEnabledHoneymoonAccounts="GetMobileEnabledHoneymoonAccounts";
	//public static final String urlWinHoneyMoon="http://www.locationsmagazine.com/admina/services/iphone.asmx?op=SubmitWinAHoneymoon";
	public static final String urlWinHoneyMoon="http://eventlocations.us/admina/services/iphone.asmx?op=SubmitWinAHoneymoon";
	public static final String urlAbout="http://eventlocations.us/admina/pages/fixes/iphone/text_data_iphone.php?accid=7909&what=about";
	public static final String urlAdditional="http://eventlocations.us/admina/pages/fixes/iphone/text_data_iphone.php?accid=7909&what=additional";
	public static final int RESOLUTION_HDPI_HEIGHT=800;
	public static final int RESOLUTION_HDPI_HEIGHT2=752;
	public static final int RESOLUTION_HDPI_WIDTH=1280;
	public  static String body2_aboutus="Locate a venue for your wedding, corporate event, bar/bat mitzvah, birthday or anniversary.\nFIND an upcoming Bridal Show. NY TIMES: Featured on the Front Cover of the NY Times ''Weddings'' with major editorial.\n\nOur 2011 magazine contains 204 pages with color photos & detailed room info. Available at Barnes & Noble and Border Books in the ''Wedding'' section, at Bridal Show, plus newsstands. Call 212-288-4745 for store near you.";
	public  static String email="info@locationsmagazine.com";
	public  static String emailBody="Locations Magazine Android (v.1.0.0)";
	public static final String HONEYMOON_TITLE="Honeymoon & Wedding";
	public static final String WEBPAGE_WIN_HONEYMOON="http://www.EventLocations.us/win/";
	public static final String KEY_ROW_STATIONLIST = "STATIONLIST";
	public static boolean IsEmail(String email)
    {
        Boolean result = false;

        String regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]+[a-z0-9-]*[a-z0-9]+";
        //String regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

        if (email.matches(regex))
            result = true;
        else
            result = false;

        return result;
    }
}


