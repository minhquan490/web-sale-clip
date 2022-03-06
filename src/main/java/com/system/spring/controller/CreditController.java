package com.system.spring.controller;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Credit;
import com.system.spring.entity.User;
import com.system.spring.exception.UnknowException;
import com.system.spring.request.CreditRequest;
import com.system.spring.response.CreditResponse;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.CreditService;

@Controller
@RequestMapping(ApiConfig.CREDIT_PATH)
public class CreditController {

	@Autowired
	private CreditService creditService;

	@PreAuthorize("hasAnyAuthority('viewer', 'actor', 'admin')")
	@PostMapping(ApiConfig.CREATE_CREDIT_INFO)
	@ResponseBody
	public ResponseEntity<ServerResponse> createCreditInfo(@RequestBody CreditRequest creditRequest)
			throws NullPointerException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		Credit credit = creditService.getCreditFromUser(user);
		credit.setCreditNumber(creditRequest.getCreditNumber());
		credit.setPassCode(Integer.parseInt(creditRequest.getPassCode()));
		credit.setCreditClassification(creditRequest.getClassification());
		credit.setExpirationMonth(creditRequest.getExpire());
		creditService.edit(credit);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ServerResponse(LocalDateTime.now(),
				HttpStatus.CREATED, new CreditResponse(credit.getCreditNumber())));
	}

	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping(ApiConfig.DELETE_CREDIT + "/{id}")
	@ResponseBody
	public ResponseEntity<ServerResponse> delete(@PathVariable("id") String id) throws EntityNotFoundException {
		Credit credit = creditService.get(Long.valueOf(id));
		if (creditService.delete(credit)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Delete success"));
		} else {
			throw new UnknowException("Something wrong", null);
		}
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor', 'admin')")
	@GetMapping(ApiConfig.CREDIT_INFO)
	@ResponseBody
	public ResponseEntity<ServerResponse> getCreditInfo() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		Credit credit = creditService.getCreditFromUser(user);
		return ResponseEntity.ok(
				new ServerResponse(LocalDateTime.now(), HttpStatus.OK, new CreditResponse(credit.getCreditNumber())));
	}
}
