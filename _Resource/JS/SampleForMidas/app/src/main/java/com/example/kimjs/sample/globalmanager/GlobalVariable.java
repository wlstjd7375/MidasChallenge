package com.example.kimjs.sample.globalmanager;


public final class GlobalVariable {

	//public static final String WEB_SERVER_IP = "https://192.168.35.75"; //우리집 내부아이피 테스트용
	//public static final String WEB_SERVER_IP = "https://1.227.248.51"; //외부아이피.
	public static final String WEB_SERVER_IP = "https://211.189.20.165"; //맴 아이피

	public static final int HTTP_PORT = 13337;
	public static final int HTTPS_PORT = 13337;

	public static boolean isScheduleDBUpdated = false;
	public static boolean isServerOn = true;
	public static boolean isBluetoothOn = false;

	public static boolean isDebugMode = false;
	//public static boolean isWifiSetting = false;
	public static boolean isRaspberryOn = false;

	public static int SmallVersion = 0;
	public static int BigVersion = 1;


	public static String VersionText = "";

	public static final String BROADCAST_MESSAGE = "com.seven.broadcastreceiver.status";
	public static final String BROADCAST_WIFI = "com.seven.broadcastreceiver.wifi";


	public static final String GLOBAL_FONT = "HMKMMAG.TTF";

	public static final int GLOBAL_DPI_DEFAULT = 320;
	public static int GLOBAL_DPI;

	public static final int SCHEDULE = 1;
	public static final int BUS = 2;
	public static final int SUBWAY = 4;
	public static final int WEATHER = 8;
	public static final int RECORD = 16;


	public final class ShortcutType {
		public static final int
				typeScheduleShortcut  = 1,
				typeBusShortcut = 2,
				typeSubwayShortcut = 3,
				typeWeatherShortcut = 4,
				typeGasAlarmShortcut = 5,
				typeFamilyShortcut= 6,
				typeRefrash = 7,
				typeVoice = 8,
				typeSetting = 9;
	}

	public final class SubwayConstant {

		public static final String
				week_normal = "1", //평일
				week_saturday = "2", //토요일
				week_holiday_sunday = "3"; //휴일/일요일

		public static final String
				up_in_line = "1", //상행/내선
				down_out_line ="2"; //하행/외선

		public static final String
				tpye_I = "I",
				type_K = "K";



	}

	public final class BusConstant {

	}

	public final class GCMConstant {

		public static final String
				PROJECT_REG_ID = "AIzaSyBo7pigCHSXysJD-qxKsE0H9YBGXmIvaVQ";

	}

	public final class WeatherConstant {

		//sky의 경우 하늘 상태를 의미하고

		public final class SKY {
			public static final int
					CLEAR = 1, //맑음
					PARTLY_CLOUDY = 2, //구름조금
					MOSTLY_CLOUDY = 3, //구름 많음
					CLOUDY = 4; //흐림
		}

		// pty의 경우 강수 상태를 의미한다
		// 코드 int 상수 유지 중요! code 매칭
		public final class PTY {
			public static final int
					CLEAR = 0,  //없음
					RAIN = 1,   //비
					SNOW_RAIN = 2,   //비/눈
					RAIN_SNOW = 3,   //눈/비
					SNOW = 4;   //눈
		}

		public final class WFKOR {
			public static final int
					CLEAR = 0,  // 맑음
					PARTLY_CLOUDY = 1,   //구름 조금
					MOSTLY_CLOUDY = 2,   //구름 많음
					CLOUDY = 3,   //흐림
					RAIN = 4,   //비
					SNOW_RAIN = 5, //눈/비
					SNOW = 6; //눈
		}
	}

	public final class UserType {

		public static final int
				me = 1, // 나
				other = 2; //나 제외

		//master type check용
		public static final int
				master = 3, //가족중에서 메인 아이디인 경우
				normal = 4; //가족중에서 서브 아이디인 경우
	}



	public static String hosunDecode(String code) {
		// 케이스문
		String decode = "";

		if(code == null)
		{
			return "";
		}
		switch (code)
		{
			case "I":
				decode = "인천1호선";
				break;
			case "K":
				decode = "경의중앙선";
				break;
			case "B":
				decode = "분당선";
				break;
			case "A":
				decode = "공항철도";
				break;
			case "G":
				decode = "경춘선";
				break;
			case "S":
				decode = "신분당선";
				break;
			case "SU":
				decode = "수인선";
				break;
			case "U":
				decode = "의정부경전철";
				break;
			default:
				decode = code + "호선";
				break;
		}

		return decode;
	}

	public static int wfKorDecode(String code)
	{
		int decode = -1;

		if(code == null)
		{
			return -1;
		}
		switch (code)
		{
			case "맑음":
				decode = WeatherConstant.WFKOR.CLEAR;
				break;
			case "구름 조금":
				decode = WeatherConstant.WFKOR.PARTLY_CLOUDY;
				break;
			case "구름 많음":
				decode = WeatherConstant.WFKOR.MOSTLY_CLOUDY;
				break;
			case "흐림":
				decode = WeatherConstant.WFKOR.CLOUDY;
				break;
			case "비":
				decode = WeatherConstant.WFKOR.RAIN;
				break;
			case "눈/비":
				decode = WeatherConstant.WFKOR.SNOW_RAIN;
				break;
			case "눈":
				decode = WeatherConstant.WFKOR.SNOW;
				break;
			default:
				decode = -1;
				break;
		}
		return decode;
	}





}