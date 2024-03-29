package com.lv.currencycalculator.service;

import com.lv.currencycalculator.db.entities.ExchangeRate;
import com.lv.currencycalculator.db.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateParserService {
    private final ExchangeRateRepository exchangeRateRepository;

    private static final String AMOUNT_TAG = "Amt";
    private static final String CURRENCY_TAG = "Ccy";
    private static final String CURRENCY_RATE_TAG = "CcyAmt";
    private static final String DATE_TAG = "Dt";
    private static final String RATE_TAG = "FxRate";


    public void parseAndSaveExchangeRates(String exchangeRateUrl) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        URI tempUri = new URI(exchangeRateUrl);
        URLConnection connection = tempUri.toURL().openConnection();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document exchangeRateDoc = db.parse(connection.getInputStream());

        exchangeRateDoc.getDocumentElement().normalize();
        NodeList rates = exchangeRateDoc.getElementsByTagName(RATE_TAG);
        parseXmlToExchangeRates(rates).forEach(this::saveExchangeRate);
    }

    private void saveExchangeRate(ExchangeRate rate) {
        if (Objects.nonNull(rate)) {
            try {
                exchangeRateRepository.save(rate);
            } catch (Exception e) {
                log.error("Error during saving " + rate + " " + e.getMessage());
            }
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

                    ExchangeRate tempExchangeRate = ExchangeRate.builder()
                            .currencyCode(currency)
                            .rate(new BigDecimal(amount))
                            .date(LocalDate.parse(date))
                            .build();

                    resultList.add(tempExchangeRate);
                }
            }
        }
        return new ArrayList<>(resultList);
    }
}
