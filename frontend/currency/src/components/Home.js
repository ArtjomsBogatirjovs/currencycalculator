import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Home() {
    const [rates, setRates] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/getActualRates')
            .then(response => setRates(response.data))
            .catch(error => console.error('There was an error!', error));
    }, []);

    return (
        <div>
            <h1>Actual Exchange Rates</h1>
            <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                <Link to="/converter">Go to Converter</Link>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Currency Code</th>
                    <th>Rate</th>
                    <th>View History</th>
                </tr>
                </thead>
                <tbody>
                {rates.map(rate => (
                    <tr key={rate.id}>
                        <td>{rate.currencyCode}</td>
                        <td>{rate.rate}</td>
                        <td><Link to={`/history/${rate.currencyCode}`}>History</Link></td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default Home;
