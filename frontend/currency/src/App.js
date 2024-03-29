import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import CurrencyConverter from './components/CurrencyConverter';
import CurrencyHistory from './components/CurrencyHistory';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} exact />
                <Route path="/converter" element={<CurrencyConverter />} />
                <Route path="/history/:rateName" element={<CurrencyHistory />} />
            </Routes>
        </Router>
    );
}

export default App;
