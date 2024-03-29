package com.lv.currencycalculator.service;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.db.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExchangeRateParserService {
    private final ExchangeRateRepository exchangeRateRepository;

    private static final String EUR_CURRENCY = "EUR";
    private static final String AMOUNT_TAG = "Amt";
    private static final String CURRENCY_TAG = "Ccy";
    private static final String CURRENCY_RATE_TAG = "CcyAmt";
    private static final String DATE_TAG = "Dt";
    private static final String RATE_TAG = "FxRate";

    private boolean eurRateCreated = false;

    public void parseAndSaveExchangeRates(Document exchangeRateDoc) {
        exchangeRateDoc.getDocumentElement().normalize();
        NodeList rates = exchangeRateDoc.getElementsByTagName(RATE_TAG);
        parseXmlToExchangeRates(rates).forEach(this::saveExchangeRate);
    }

    private void saveExchangeRate(ExchangeRate rate) {
        if (Objects.nonNull(rate)) {
            exchangeRateRepository.save(rate);
        }
    }

    public List<ExchangeRate> parseXmlToExchangeRates(NodeList rates) {
        Set<ExchangeRate> resultList = new HashSet<>();

        for (int i = 0; i < rates.getLength(); i++) {
            Node fxRateNode = rates.item(i);

            if (fxRateNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fxRateElement = (Element) fxRateNode;

                String date = fxRateElement.getElementsByTagName(DATE_TAG).item(0).getTextContent();

                NodeList ccyAmtList = fxRateElement.getElementsByTagName(CURRENCY_RATE_TAG);
                for (int j = 0; j < ccyAmtList.getLength(); j++) {
                    Element ccyAmtElement = (Element) ccyAmtList.item(j);
                    String currency = ccyAmtElement.getElementsByTagName(CURRENCY_TAG).item(0).getTextContent();
                    String amount = ccyAmtElement.getElementsByTagName(AMOUNT_TAG).item(0).getTextContent();

                    if (EUR_CURRENCY.equals(currency) && eurRateCreated) {
                        continue;
                    }

                    ExchangeRate tempExchangeRate = ExchangeRate.builder()
                            .currencyCode(currency)
                            .rate(new BigDecimal(amount))
                            .date(LocalDate.parse(date))
                            .build();

                    resultList.add(tempExchangeRate);

                    if ("EUR".equals(currency)) {
                        eurRateCreated = true;
                    }
                }
            }
        }
        return new ArrayList<>(resultList);
    }
}
