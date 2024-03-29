import React, { useEffect, useState } from 'react';
import axios from 'axios';

function CurrencyConverter() {
    const [currencyCode, setCurrencyCode] = useState('');
    const [date, setDate] = useState('');
    const [amount, setAmount] = useState('');
    const [result, setResult] = useState(null);
    const [availableCurrencies, setAvailableCurrencies] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/getAvailableCurrencies')
            .then(response => {
                setAvailableCurrencies(response.data);
            })
            .catch(error => console.error('Error fetching currencies:', error));
    }, []);

    const convertCurrency = () => {
        axios.get('http://localhost:8080/api/v1/calculate', { params: { currencyCode, date, amount } })
            .then(response => {
                setResult(response.data);
            })
            .catch(error => console.error('Error converting currency:', error));
    };

    return (
        <div>
            <h1>Currency Converter</h1>
            <select value={currencyCode} onChange={e => setCurrencyCode(e.target.value)}>
                <option value="">Select Currency</option>
                {availableCurrencies.map((code) => (
                    <option key={code} value={code}>{code}</option>
                ))}
            </select>
            <input type="date" value={date} onChange={e => setDate(e.target.value)} />
            <input type="number" value={amount} onChange={e => setAmount(e.target.value)} placeholder="Amount" />
            <button onClick={convertCurrency}>Convert</button>
            {result && <p>Converted Amount: {result}</p>}
        </div>
    );
}

export default CurrencyConverter;
