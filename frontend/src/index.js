import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

console.log('正在启动应用...');
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);