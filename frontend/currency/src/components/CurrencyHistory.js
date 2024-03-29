import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

function CurrencyHistory() {
    const { rateName } = useParams();
    const [history, setHistory] = useState([]);
    const [dateFrom, setDateFrom] = useState(new Date().toISOString().slice(0, 7) + '-01');
    const [dateTo, setDateTo] = useState(new Date().toISOString().slice(0, 10));

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/getRate', { params: { rateName, dateFrom, dateTo } })
            .then(response => setHistory(response.data))
            .catch(error => console.error('There was an error!', error));
    }, [rateName, dateFrom, dateTo]);

    return (
        <div>
            <h1>History for {rateName}</h1>
            <input type="date" value={dateFrom} onChange={e => setDateFrom(e.target.value)} />
            <input type="date" value={dateTo} onChange={e => setDateTo(e.target.value)} />
            <table>
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Rate</th>
                </tr>
                </thead>
                <tbody>
                {history.map((item, index) => (
                    <tr key={index}>
                        <td>{item.date}</td>
                        <td>{item.rate}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default CurrencyHistory;
