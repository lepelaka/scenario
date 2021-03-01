package scenario.kakao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * KAKAO의 API를 이용해 데이터를 읽어와 JSON Parser를 이용해 파싱한다.<br>
 * 연계주기를 이용해 시간 간격을 두고 데이터를 읽는다.<br>
 * 인증키는 developers.kakao.com에서 부여 받아 사용한다.<br>
 * 외부라이브러리 JSONObject클래스는 maven repository에서 탐색해서 적용한다. <br>
 * 
	평가항목<br>
	1.1 외부 및 내부 모듈 간의 데이터 연계 요구사항대로 출력되었는가. 미흡하면 부분별 –1점<br>
	1.2 연계한 데이터에서 이름, 주소, 위도, 경도를 맞게 사용하는지 항목별 1점.<br>
	1.3 외부 및 내부 모듈 간의 연계를 위한 유틸리티함수를 이용, 데이터 표준대로 출력되었는가. 미흡하면 부분별 –1점<br>
	2.1 연계 데이터가 요청이 적절한 get 파라미터 데이터로 송수신이 되었는가. 미흡하면 부분별 –1점<br>
	2.2 개발하고자 하는 응용소프트웨어와 연계 대상 모듈 간의 데이터 연계 요구사항을 고려하여 연계주기를 정의 한 코딩이 있는가. 미흡하면 부분별 –1점<br>
	2.3 개발하고자하는 응용소프트웨어와 연계 대상 내외부 모듈 간의 연계 목적을 고려하여 데이터 연계 실패 시 처리방안을 코딩에 추가하였는가. 미흡하면 부분별 –1점<br>
	2.4 응용소프트웨어와 관련된 내외부 모듈 간의 연계 데이터의 중요성을 고려하여 송수신 시 보안 인증키를 발급받고 사용 하였는가. 미흡하면 부분별 –1점<br>
	3.1 애플리케이션에 필요한 JSON출력 라이브러리를 적용하였는가. 미흡하면 부분별 –1점<br>
	3.2 애플리케이션 구현을 위해 선택한 라이브러리를 Java 언어 특성에 맞게 구성되었는가. 미흡하면 부분별 –1점<br>
	3.3 선택한 라이브러리를 사용하여 애플리케이션 구현되었는가. 미흡하면 부분별 –1점<br>
	3.4 KAKAO 개발자 센터에서 제공하는 테스트페이지결과와 구현된 애플리케이션 결과가 같은지 테스트를 수행한다. 미흡하면 부분별 –1점<br>
	결과 샘플<br>
		https://dapi.kakao.com/v2/local/search/keyword.json?page=3&size=3&query=%EC%B2%9C%EC%95%88+%ED%8E%B8%EC%9D%98%EC%A0%90<br>
		[{"place_url":"http://place.map.kakao.com/12295876","place_name":"GS25 천안불당점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 검은들3길 58","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"041-568-2525","category_group_code":"CS2","x":"127.10972665017752","y":"36.81001838981514","address_name":"충남 천안시 서북구 불당동 731","id":"12295876"},{"place_url":"http://place.map.kakao.com/22450577","place_name":"CU 천안성정시티점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 성정공원6길 36","category_name":"가정,생활 > 편의점 > CU","distance":"","phone":"041-552-3988","category_group_code":"CS2","x":"127.1418654139009","y":"36.82841027318809","address_name":"충남 천안시 서북구 성정동 1347","id":"22450577"},{"place_url":"http://place.map.kakao.com/21305618","place_name":"GS25 천안용암점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 쌍용11길 22","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"","category_group_code":"CS2","x":"127.121145366092","y":"36.8023550690607","address_name":"충남 천안시 서북구 쌍용동 1968","id":"21305618"}]<br>
		박종민 :: 2021-03-02 01:09:13<br>
		이름                주소                                    위도                                    경도<br>                                    
		GS25 천안불당점     충남 천안시 서북구 검은들3길 58         127.10972665017752                      36.81001838981514<br>                       
		CU 천안성정시티점   충남 천안시 서북구 성정공원6길 36       127.1418654139009                       36.82841027318809    <br>                   
		GS25 천안용암점     충남 천안시 서북구 쌍용11길 22          127.121145366092                        36.8023550690607         <br>               
		<br>
		https://dapi.kakao.com/v2/local/search/keyword.json?page=4&size=3&query=%EC%B2%9C%EC%95%88+%ED%8E%B8%EC%9D%98%EC%A0%90<br>
		[{"place_url":"http://place.map.kakao.com/12295895","place_name":"GS25 천안봉명점","category_group_name":"편의점","road_address_name":"충남 천안시 동남구 봉정로 21","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"041-577-9664","category_group_code":"CS2","x":"127.139108701186","y":"36.8040691539078","address_name":"충남 천안시 동남구 봉명동 40-9","id":"12295895"},{"place_url":"http://place.map.kakao.com/823785332","place_name":"GS25 불당골드점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 불당25로 192","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"1644-5425","category_group_code":"CS2","x":"127.10888580172448","y":"36.81847608164706","address_name":"충남 천안시 서북구 불당동 1487","id":"823785332"},{"place_url":"http://place.map.kakao.com/12295744","place_name":"GS25 천안장미점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 미라2길 22-4","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"041-577-3890","category_group_code":"CS2","x":"127.129507131438","y":"36.8011387680046","address_name":"충남 천안시 서북구 쌍용동 1194","id":"12295744"}]<br>
		박종민 :: 2021-03-02 01:09:14<br>
		이름                주소                                    위도                                    경도<br>                                    
		GS25 천안봉명점     충남 천안시 동남구 봉정로 21            127.139108701186                        36.8040691539078<br>                        
		GS25 불당골드점     충남 천안시 서북구 불당25로 192         127.10888580172448                      36.81847608164706   <br>                    
		GS25 천안장미점     충남 천안시 서북구 미라2길 22-4         127.129507131438                        36.8011387680046        <br>                
		
		https://dapi.kakao.com/v2/local/search/keyword.json?page=5&size=3&query=%EC%B2%9C%EC%95%88+%ED%8E%B8%EC%9D%98%EC%A0%90<br>
		[{"place_url":"http://place.map.kakao.com/1341782412","place_name":"이마트24 R천안신라점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 성정공원2길 8","category_name":"가정,생활 > 편의점 > 이마트24","distance":"","phone":"041-566-7244","category_group_code":"CS2","x":"127.143043731953","y":"36.8261182452891","address_name":"충남 천안시 서북구 성정동 1420","id":"1341782412"},{"place_url":"http://place.map.kakao.com/287070497","place_name":"이마트24 천안불당리더스점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 공원로 177","category_name":"가정,생활 > 편의점 > 이마트24","distance":"","phone":"041-531-0802","category_group_code":"CS2","x":"127.099559601705","y":"36.7993455577333","address_name":"충남 천안시 서북구 불당동 1283","id":"287070497"},{"place_url":"http://place.map.kakao.com/12295881","place_name":"GS25 천안두정점","category_group_name":"편의점","road_address_name":"충남 천안시 서북구 성정공원5로 51","category_name":"가정,생활 > 편의점 > GS25","distance":"","phone":"041-566-2389","category_group_code":"CS2","x":"127.14331278268","y":"36.827939961482","address_name":"충남 천안시 서북구 성정동 1395","id":"12295881"}]<br>
		박종민 :: 2021-03-02 01:09:15<br>
		이름                주소                                    위도                                    경도<br>                                    
		이마트24 R천안..    충남 천안시 서북구 성정공원2길 8        127.143043731953                        36.8261182452891<br>                        
		이마트24 천안불..   충남 천안시 서북구 공원로 177           127.099559601705                        36.7993455577333    <br>                    
		GS25 천안두정점     충남 천안시 서북구 성정공원5로 51       127.14331278268                         36.827939961482   <br>
 * 
 * @author 박종민
 * @since JDK1.8
 * @version 1.0
 * 
 */
public class KakaoApi {
	private static final String MAIN_API = "https://dapi.kakao.com/v2/local/search/keyword.json";	// 키워드를 통해 주소받기 URL API 요청 메인 주소
	private static final String SERVICE_KEY = "f7a33033c8715643e3d98a97b5500cab";					// API 사용 서비스 키
	
	private static final String[] MENUS = {"이름", "주소", "위도", "경도"};							// 출력시 사용될 범례
	private static final int[] COUNT_MENU = { 20, 40, 40, 40 };										// 출력시 사용될 범례의 길이
	
	private static final int RANDOM_PAGE = (int)(Math.random() * 5 + 1); 							// 페이지번호 랜덤 생성
	
	
	public static void main(String[] args) {
		// 모든 작성은 main method안에 기술 하되 1초간격으로 3회 동작하도록 제작, 예외 처리는 메서드 내부에서 기술한다.
		// 화면 출력시 사용될 내용은 동봉된 CommonUtils 클래스 내부의 format method를 이용해서 규격화 한다.
	}

	/**
	 * 
	 * 요청 주소 문자열 생성
	 * 
	 * @param api
	 * @param page
	 * @param size
	 * @param query
	 * @return
	 */
	private static String buildRequestUrl(String api, int page, int size, String query) {
		String param = null;
		try {
			param = "?page=" + page + "&size=" + size + "&query=" + URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return api + param;
	}
	
	/**
	 * REST API 호출
	 * 
	 * @param paramUrl
	 *           
	 */
	private JSONObject RestCall(String paramUrl) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(paramUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String auth = "KakaoAK " + SERVICE_KEY;
			conn.setRequestProperty("Authorization", auth);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			System.out.println("RestCall Fail : " + e.getMessage());
		}
		return new JSONObject(sb.toString());
	}
}