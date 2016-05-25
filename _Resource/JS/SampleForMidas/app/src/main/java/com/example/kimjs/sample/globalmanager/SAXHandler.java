package com.example.kimjs.sample.globalmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;

import org.xml.sax.helpers.DefaultHandler;

//@SuppressWarnings({ "rawtypes", "unchecked" })
public class SAXHandler extends DefaultHandler{

	private String[] dset = null;								// 데이터셋 명칭 배열
	private Map dsetInfo = new HashMap();		// 데이터셋 정보를 저장하는 Map
	private boolean record = false;							// XML TAG가 레코드인지 여부
	private boolean dataset = false;						// XML TAG가 데이터셋인지 여부

	private String column = null;								// 컬럼명

	private StringBuffer value = new StringBuffer();		// 일반적인 값 저장

	private StringBuffer resultCode = new StringBuffer();	// 성공/실패 여부 [00:정상, 그외]

	private StringBuffer resultMsg = new StringBuffer();	// 성공/실패 여부 메세지

	private String tagName = "";							// TAG명칭

	private String serviceId = "";

	private boolean service = false;						// 서비스 시작.

	private Map row = null;

	private List ds = null;

	private Map fixRow = null;						// FIX부 처리용 데이터셋

	private List fixDs = null;

	/**
	 * XML전문을 파싱하여 처리하는 XML 핸들러
	 * @param dataSetKey	- 데이터셋의 명칭 fcSingleSelect의 key값에 해당하는 내용
	 * @param service TODO
	 */

	public SAXHandler(String[] dataSetKey, Map resultInfo, String service) {
		// 출력값가져갈 맵
		dsetInfo = resultInfo;
		// 데이터셋을 구성할 헤더
		dset = dataSetKey;
		// 응답부 아이디를 찾음:전문시작부
		serviceId = service;

		/** UI에서 넘어온 데이터셋 정보를 담는 부분 **/
		for (int i=0;i<dset.length; i++) {
			dsetInfo.put(dataSetKey[i], new ArrayList());
		}
	}

	/**
	 * XML Document [시작]
	 */
	 public void startDocument(){
		 value.setLength(0);	// 초기화
	 }

	 
	 /**
	  * XML Document [끝]
	  */
	 public void endDocument(){
		 Iterator it = dsetInfo.keySet().iterator();

		 /** 데이터셋의 데이터를 클라이언트 전송 **/
		 while(it.hasNext()) {
			 System.out.println("it.hasNext()???");
			 String key = (String)it.next();
			 if ("fixData".equals(key)) {
				 List fix = (List)dsetInfo.get("fixData");
				 fix.add(fixRow);
			 }
		 }

		 /** 에러 메시지 및 성공 메시지에 대한 처리 **/
		 String code = resultCode.toString().trim();
		 String msg = resultMsg.toString().trim();
		 
		 dsetInfo.put("resultCode",  code);
		 dsetInfo.put("resultMsg",  msg);

	 }

	 /**
	  * 파싱할 엘리먼트 [시작]
	  */
	 public void startElement(String namespaceURI, String localName, String qName, Attributes attrs){
		 value.setLength(0);
		 if (dataset && record) {
			 column = qName;
			 tagName = "";
		 } else {
			 tagName = qName;
		 }
		 /** XML의 TAG가 데이터셋의 Key명칭과 일치하는지 판단 **/
		 if (dsetInfo.containsKey(qName)) {
			 //ds에 해당컬럼에 데이터담는다 아마도 addrow
			 ds = (List)dsetInfo.get(qName);
			 dataset = true;
			 record = true;
			 row = new HashMap();
		 //헤더부
		 } else if (serviceId.equals(qName)) {
			 service = true;	// 고정부 시작.
		 }
	}

	 /**
	  * 파싱할 엘리먼트 [끝]
	  * - 데이터셋에 FirstRow가 적용되어 addDataRow하는 시점에 특정 레코드 갯수가 입력이되면 클라이언트로 먼저 전송
	  */
	 public void endElement(String namesapceURI, String localName, String qName){
		 if (dsetInfo.containsKey(qName)) {
			 dataset = false;
			 record = false;
			 column = null;
			 /** 컬럼이 끝이면 생성된 레코드를 데이터셋에 추가 **/
			 ds.add(row);
		 } else if (service && !record && tagName.equals(qName)) {
			 // 고정부에 대한 추가.
			 if (fixDs != null) {
				 fixRow.put(qName, value.toString());
			 }
			 value.setLength(0);
	 	 } else {
			 /** 데이터 레코드(IXyncDataRow)에 값을 입력 **/
	 		 if (" ".equals(value.toString()))  {
	 			 value.trimToSize();
	 		 }
			 if(value.length() != 0){
				 if (record && dataset){
					row.put(column, value.toString());
					 value.setLength(0);
					 value.trimToSize();
				 }
			 }
		 }
	 }

	 
	 /**
	  * XML Element안의 Text를 파싱하여 담는 부분
	  */
	 public void characters(char[] ch, int start, int length){
		//tagName단일데이터 혹은 연쇄데이터시작 column연쇄데이터
		 if (dataset && record) {
			 String str = new String(ch, start, length);
			 value.append(str);
	     //이하는 단일데이터처리임 주로 헤더
		//전문통신결과코드	 
		 } else if ("resultCode".equals(tagName)) {
			 String str = new String(ch, start, length);
			 resultCode.append(str.trim());
			 //전문통신결과메세지
		 } else if ("resultMsg".equals(tagName)) {
			 String str = new String(ch, start, length);
			 resultMsg.append(str.trim());
		 //서비스시작됫고 반복부가 아닌 고정값 정해진 헤더외값들
		 } else if (service && !dataset) {
			 String str = new String(ch, start, length);
			 value.append(str.trim());
		 }

	 }
	 
}
