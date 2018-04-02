package ru.sbt.jschool.session3.problem1;

import java.util.*;

/**
 */
public class AccountServiceImpl implements AccountService {
    protected FraudMonitoring fraudMonitoring;
    private HashMap<Long, Account> accountHashMap = new HashMap<>();
    private HashSet<Long> paymentsHashSet = new HashSet<>();

    public AccountServiceImpl(FraudMonitoring fraudMonitoring) {
        this.fraudMonitoring = fraudMonitoring;
    }

    @Override
    public Result create(long clientID, long accountID, float initialBalance, Currency currency) {
        Account tmp = find(accountID);
        if (fraudMonitoring.check(clientID)) {
            return Result.FRAUD;
        }
        if (tmp != null) {
            return Result.ALREADY_EXISTS;
        }
        tmp = new Account(clientID, accountID, currency, initialBalance);
        accountHashMap.put(accountID, tmp);
        return Result.OK;
    }

    @Override
    public List<Account> findForClient(long clientID) {
        List<Account> listTmp = new ArrayList<>();
        for (Account acc : this.accountHashMap.values()) {
            if (acc.getClientID() == clientID) {
                listTmp.add(acc);
            }
        }
        return listTmp;
    }

    @Override
    public Account find(long accountID) {
        Account tmp = accountHashMap.get(accountID);
        return tmp;
    }

    @Override
    public Result doPayment(Payment payment) {
        if (fraudMonitoring.check(payment.getPayerID()) || fraudMonitoring.check(payment.getRecipientID())) {
            return Result.FRAUD;
        }
        if (paymentsHashSet.contains(payment.getOperationID())) {
            return Result.ALREADY_EXISTS;
        }
        Account payer = find(payment.getPayerAccountID());
        if (payer == null || payer.getClientID() != payment.getPayerID()) {
            return Result.PAYER_NOT_FOUND;
        }
        Account recipient = find(payment.getRecipientAccountID());
        if (recipient == null || recipient.getClientID() != payment.getRecipientID()) {
            return Result.RECIPIENT_NOT_FOUND;
        }
        if (payer.getBalance() < payment.getAmount()) {
            return Result.INSUFFICIENT_FUNDS;
        }
        Currency payerCurrency = payer.getCurrency();
        Currency recipientCurrency = recipient.getCurrency();
        float balance = payer.getBalance() - payment.getAmount();
        payer.setBalance(balance);
        if (payerCurrency == recipientCurrency) {
            balance = recipient.getBalance() + payment.getAmount();
            recipient.setBalance(balance);
        } else {
            balance = recipient.getBalance() + payerCurrency.to(payment.getAmount(), recipientCurrency);
            recipient.setBalance(balance);
        }
        paymentsHashSet.add(payment.getOperationID());
        return Result.OK;
    }
}
