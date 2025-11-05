package com.codechallenge.transactional_portal_api.service.interfaces;

import com.codechallenge.transactional_portal_api.model.TransactionRechargeRequest;
import com.codechallenge.transactional_portal_api.model.TransactionRechargeResponse;
import com.codechallenge.transactional_portal_api.model.TransactionSupplier;
import com.codechallenge.transactional_portal_api.model.TransactionToken;

import java.util.List;

public interface TransactionService {
    TransactionToken getTransactionalToken();
    List<TransactionSupplier> getSuppliers();
    TransactionRechargeResponse createRecharge(TransactionRechargeRequest transactionRechargeRequest);
}
