package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.CreditCardJadgeResult;
import com.example.demo.domain.CreditCardPaymentInfomation;

/**
 * クレジットカード情報を受け取り適切な情報か判定するメソッド.
 * @author rks
 *
 */
@RestController
@Validated
public class CreditCardController {

	/**
	 * 判定を行うメソッド.
	 * @param info　クレジットカード情報
	 * @return　判定結果のJSON
	 */
	@RequestMapping(value = "/CreditCardApi", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CreditCardJadgeResult checker(@RequestBody CreditCardPaymentInfomation info) {
		CreditCardJadgeResult result = new CreditCardJadgeResult();
		int year = Integer.parseInt(info.getCard_exp_year());
		int month = Integer.parseInt(info.getCard_exp_month());
		int cvv = Integer.parseInt(info.getCard_cvv());
		LocalDate expiratioDate = LocalDate.of(year, month, 1);

		//今の日付が指定した日付より後かどうかを比較する
		if(LocalDate.now().isAfter(expiratioDate)) {
			result.setStatus("error");
			result.setMessage("The card is expired.");
			result.setError_code("E-01");
			
			return result;
		}
		
		//
		if (cvv != 123) {
			result.setStatus("error");
			result.setMessage("The card information is incorrect.");
			result.setError_code("E-02");
			
			return result;
		}

		if (!(info.getCard_exp_year().matches("^\\d{4}$")) || !(info.getCard_exp_month().matches("^\\d{2}$"))) {
			result.setStatus("error");
			result.setMessage("Error.");
			result.setError_code("E-03");
			
			return result;
		}

		result.setStatus("success");
		result.setMessage("OK.");
		result.setError_code("E-00");
		return result;
	}
}
